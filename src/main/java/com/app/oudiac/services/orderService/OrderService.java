package com.app.oudiac.services.orderService;

import com.app.oudiac.dtos.OrderDtos.OrderItemDTO;
import com.app.oudiac.dtos.OrderDtos.OrderRequestDto;
import com.app.oudiac.dtos.OrderDtos.OrderResponseDto;
import com.app.oudiac.dtos.productDtos.ProductResponseDto;
import com.app.oudiac.exceptions.*;
import com.app.oudiac.models.*;
import com.app.oudiac.models.enums.OrderStatus;
import com.app.oudiac.models.enums.PaymentGateway;
import com.app.oudiac.models.enums.PaymentMethod;
import com.app.oudiac.models.enums.PaymentStatus;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapter;
import com.app.oudiac.payments.adapter.PaymentGatewayAdapterFactory;
import com.app.oudiac.repositories.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductVariantRepository productVariantRepository;

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final PaymentGatewayAdapterFactory paymentGatewayAdapterFactory;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderChargesRepository orderChargesRepository;

    @Transactional
    public ResponseEntity<OrderResponseDto> placeOrder(OrderRequestDto request, String username, String  idempotencyKey,String adapter,String currency) throws Exception {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        //Prevent duplicate orders
        Optional<Order> ordered = orderRepository.findByIdempotencyKey(idempotencyKey);
        if(ordered.isPresent()) {
            throw new ItemAlreadyExitException("Order already exists");
        }
        OrderCharges orderCharges=orderChargesRepository.getCharges(false);
        if(orderCharges==null){
            throw new ItemNotFoundException("Charges Not Found");
        }

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setIdempotencyKey(idempotencyKey);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal subtotalAmount = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : request.getItems()) {

            Optional<Product> product=productRepository.findById(itemDTO.getProductId());
            if(product.isEmpty()){
                throw new ItemNotFoundException("Product not found");
            }

            //Filter varient
            ProductVariant productVariant = product.get().getProductVariants()
                    .stream()
                    .filter(variant -> Objects.equals(variant.getId(), itemDTO.getVariantId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Variant ID does not belong to this product!"));

            OrderItem item = new OrderItem();
            item.setUrl(product.get().getImageUrl());
            item.setProductVariant(productVariant);
            item.setQuantity(itemDTO.getQuantity());

            BigDecimal price = productVariant.getSellingPrice(); // always from DB
            item.setUnitPrice(price);
            item.setVariantType(productVariant.getVariantType());
            item.setProductName(product.get().getName());

            subtotalAmount =subtotalAmount.add(price.multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            item.setOrder(newOrder);
            orderItems.add(item);
        }
        newOrder.setItems(orderItems);

        // ==========================================
        // 📌 Calculate Pricing with discount and gst
        // ==========================================

        newOrder.setSubTotal(subtotalAmount);

        /** Calculating discount */
        BigDecimal discount=new BigDecimal(0); // Amount Rs=0 For now, later calculate via coupon code
        newOrder.setDiscount(discount);

        /** Calculating Shipping Fee*/
        BigDecimal shippingFee;
        // .compareTo returns 1 if subTotal is greater than 2000
        if (subtotalAmount.compareTo(orderCharges.getFreeShippingThreshold()) >= 0) {
            shippingFee = BigDecimal.ZERO; // Free shipping!
        } else {
            shippingFee = orderCharges.getShippingFee();
        }
        newOrder.setShippingFee(shippingFee);
        BigDecimal taxableAmount=subtotalAmount.subtract(discount).add(shippingFee);

        BigDecimal taxRate = orderCharges.getGst().divide(BigDecimal.valueOf(100));
        BigDecimal taxAmount = taxableAmount.multiply(taxRate)
                .setScale(2, RoundingMode.HALF_UP); // Rounds to 2 decimal places
        newOrder.setTaxAmount(taxAmount);


        // 4. Calculate Final Total (SubTotal + Tax + Shipping)
        BigDecimal totalAmount = taxableAmount.add(taxAmount);
        newOrder.setTotalAmount(totalAmount);

        // ==========================================
        // 📌 Add Shipping Address
        // ==========================================
        Optional<Address> address=addressRepository.findById(request.getAddressId());
        if(address.isEmpty()){
            throw new AddressNotFoundException("Shipping Address is required");
        }
        newOrder.setShippingAddress(address.get().getShippingAddress());
        newOrder.setShippingCity(address.get().getCity());
        newOrder.setShippingPincode(address.get().getPinCode());
        newOrder.setShippingPhone(address.get().getPhoneNumber());
        newOrder.setShippingName(address.get().getFullName());
        newOrder.setShippingState(address.get().getState());
        newOrder.setCountry(address.get().getCountry());

        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setPaymentStatus(PaymentStatus.PENDING);
        newOrder.setCourierPartner(null);
        newOrder.setTrackingNumber(null);
        newOrder.setExpectedDeliveryDate(null);


        // ==========================================
        // 📌 Create Order in Razorpay Client,  ex Razorpay
        // ==========================================
        PaymentGatewayAdapter paymentGatewayAdapter=paymentGatewayAdapterFactory.getAdapter(adapter);
        newOrder.setOrderNumber(generateCustomOrderNumber());
        String rzpOrderId=paymentGatewayAdapter.createOrder(newOrder.getOrderNumber(),totalAmount,currency);
        newOrder.setOrderId(rzpOrderId);


        // ==========================================
        // 📌 Create Payment with PENDING Status
        // ==========================================
        List<Payment> payments=new ArrayList<>();
        Payment payment=new Payment();
        payment.setOrder(newOrder);
        payment.setUser(user);
        payment.setGatewayTransactionId(null);
        payment.setPaymentMethod(PaymentMethod.COD);
        payment.setPaymentGateway(PaymentGateway.RAZORPAY);
        payment.setAmount(totalAmount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setErrorMessage(null);  //Update after payment
        payments.add(payment);
        newOrder.setPayments(payments);

        // ==========================================
        // 📌 Create History with PENDING Status
        // ==========================================
        OrderHistory newOrderHistory=new OrderHistory();
        newOrderHistory.setStatus(OrderStatus.PENDING);
        newOrderHistory.setChangedBy("CUSTOMER");
        newOrderHistory.setTitle("Order Placed");
        newOrderHistory.setChangedByUserId(user.getId());
        newOrderHistory.setOrder(newOrder);
        newOrderHistory.setCreated_at(new Date());
        newOrderHistory.setUpdated_at(new Date());

         orderRepository.save(newOrder);  //create order repo
         orderHistoryRepository.save(newOrderHistory);
         System.out.println("Order Created with order number :: "+newOrder.getOrderId());

        OrderResponseDto response=OrderResponseDto.from(newOrder);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<OrderResponseDto> getOrder(String orderNumber, Principal principal) {
        Optional<User> user = userRepository.findByEmail(principal.getName());
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        Optional<Order> order=orderRepository.findByOrderNumber(orderNumber);
        if(order.isEmpty()){
            throw new OrderNotFoundException("Invalid Order");
        }
        OrderResponseDto response=OrderResponseDto.from(order.get());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public Page<OrderResponseDto> getOrders(int page, int size, Principal principal) {
        Page<Order> orders=orderRepository.findAll(PageRequest.of(page, size));
        return orders.map(OrderResponseDto::from);
    }

    // Automatically generate the public Order ID
    private String generateCustomOrderNumber() {
        return "OUD-" + java.time.Year.now().getValue() + "-" +
                java.util.UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    public Page<OrderResponseDto> getOrdersByUserId(int page, int size, Principal principal) {
        Optional<User> user = userRepository.findByEmail(principal.getName());
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        Page<Order> orders=orderRepository.findByUserId(user.get().getId(),PageRequest.of(page, size));
        return orders.map(OrderResponseDto::from);
    }


    public Page<OrderResponseDto> getCurrDayOrders(int page, int size, Principal principal) {
        LocalDate today = LocalDate.now();

        Date startOfDay = Date.from(
                today.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        Date startOfNextDay = Date.from(
                today.plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );

        Page<Order> orders=orderRepository.findCurrDayOrders(startOfDay,startOfNextDay,PageRequest.of(page, size));
        return orders.map(OrderResponseDto::from);
    }

    public ResponseEntity<OrderResponseDto> updateOrder(Long orderId,OrderStatus newStatus,String title,Principal principal) {
        Optional<Admin> user=adminRepository.findByEmail(principal.getName());
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        Optional<Order> order=orderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new InvalidOrderException("Invalid Order");
        }
        order.get().setStatus(newStatus);

        //Create new History
        OrderHistory newOrderHistory=new OrderHistory();
        newOrderHistory.setStatus(newStatus);
        newOrderHistory.setChangedBy(user.get().getRole().toString().toUpperCase());
        newOrderHistory.setTitle(title);
        newOrderHistory.setChangedByUserId(user.get().getId());
        newOrderHistory.setOrder(order.get());
        newOrderHistory.setCreated_at(new Date());
        newOrderHistory.setUpdated_at(new Date());

        orderRepository.save(order.get());
        orderHistoryRepository.save(newOrderHistory);
        OrderResponseDto response=OrderResponseDto.from(order.get());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
