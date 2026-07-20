package com.app.oudiac.payments.adapter;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentGatewayAdapterFactory {
    // Central registry mapping gateway names to their concrete class implementations
    private final Map<String, PaymentGatewayAdapter> gatewayRegistry = new ConcurrentHashMap<>();

    // Constructor Injection: Spring automatically finds and injects ALL implementations of PaymentGatewayAdapter
    public PaymentGatewayAdapterFactory(List<PaymentGatewayAdapter> adapters) {
        for (PaymentGatewayAdapter adapter : adapters) {
            gatewayRegistry.put(adapter.getGatewayName().toUpperCase(), adapter);
        }
    }

    /**
     * Dynamically fetches the correct payment gateway instance.
     * Throws an exception if the gateway is missing or misspelled.
     */
    public PaymentGatewayAdapter getAdapter(String gatewayName) {
        return Optional.ofNullable(gatewayRegistry.get(gatewayName.toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment gateway '" + gatewayName + "' is not supported or misconfigured."
                ));
    }
}
