package com.app.oudiac.payments.razorpay;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfiguration {
    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
//        System.out.println("RazorpayConfiguration.razorpayClient"+razorpayApiSecret);
//        System.out.println("RazorpayConfiguration.razorpayApiKey"+razorpayApiKey);
        return new RazorpayClient(razorpayApiKey,razorpayApiSecret);
    }

}
