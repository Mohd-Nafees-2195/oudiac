package com.app.oudiac.models.enums;

import java.util.HashMap;
import java.util.Map;

public enum PaymentMethod {
    COD,
    UPI,
    CARD,
    NET_BANKING,
    CREDIT_CARD,
    DEBIT_CARD;

    private static final Map<String, PaymentMethod> lookupMap = new HashMap<>();

    static {
        lookupMap.put("card", CARD);
        lookupMap.put("upi", UPI);
        lookupMap.put("netbanking", NET_BANKING);
//        lookupMap.put("wallet", WALLET);
//        lookupMap.put("emi", EMI);
//        lookupMap.put("paylater", PAYLATER);
//        lookupMap.put("bank_transfer", BANK_TRANSFER);
    }

    public static PaymentMethod fromString(String method) {
//        if (method == null) return UNKNOWN;
        return lookupMap.getOrDefault(method.toLowerCase(), COD);
    }
}
