package org.ura;

import org.ura.entity.Taxpayer;
import org.ura.service.TaxpayerService;

public class Main {
    public static void main(String[] args) {
        TaxpayerService service = new TaxpayerService();
        Taxpayer t = service.register("1000123456", "Joel", "Kabenge",
                "joel@example.com", "0700000000", "password123");
        System.out.println("Registered taxpayer with id: " + t.getId());
    }
}