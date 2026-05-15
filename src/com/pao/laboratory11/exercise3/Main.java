package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> data = List.of(
                new Transaction(1,  new BigDecimal("999.99"),  LocalDate.of(2024, 1, 15), "US", "online"),
                new Transaction(2,  new BigDecimal("340.00"),  LocalDate.of(2024, 2, 3),  "UK", "online"),
                new Transaction(3,  new BigDecimal("89.99"),   LocalDate.of(2024, 2, 18), "CA", "online"),
                new Transaction(4,  new BigDecimal("520.75"),  LocalDate.of(2024, 3, 7),  "DE", "mobile"),
                new Transaction(5,  new BigDecimal("15.00"),   LocalDate.of(2024, 3, 22), "US", "in-store"),
                new Transaction(6,  new BigDecimal("230.40"),  LocalDate.of(2024, 4, 11), "FR", "online"),
                new Transaction(7,  new BigDecimal("1050.00"), LocalDate.of(2024, 4, 29), "US", "mobile"),
                new Transaction(8,  new BigDecimal("410.60"),   LocalDate.of(2024, 5, 5),  "CA", "in-store"),
                new Transaction(9,  new BigDecimal("410.60"),  LocalDate.of(2024, 5, 19), "AU", "online"),
                new Transaction(10, new BigDecimal("999.99"),  LocalDate.of(2024, 6, 1),  "US", "mobile")
        );
        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(5));

        System.out.println("Interogare 1: top tranzacții (din snapshot)");
        snap.getTopTransactions().forEach(System.out::println);
        System.out.println();

        System.out.println("Interogare 2: total pe țări (desc)");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).forEach(System.out::println);
        System.out.println();

        System.out.println("Interogare 3: canale ordonate după număr");
        snap.getCountByChannel().entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
        System.out.println();

        System.out.println("Interogare 4: total suma");
        System.out.println(snap.getTotalAmount());
    }
}
