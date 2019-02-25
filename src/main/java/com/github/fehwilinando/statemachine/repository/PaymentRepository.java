package com.github.fehwilinando.statemachine.repository;

import com.github.fehwilinando.statemachine.controller.input.TransactionRequest;
import com.github.fehwilinando.statemachine.model.Payment;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepository {

    private static Map<UUID, Payment> database = new HashMap<>();

    static {
        TransactionRequest transaction = new TransactionRequest(BigDecimal.TEN);
        Payment payment = Payment.from(transaction);

        database.put(payment.getId(), payment);
    }


    public void save(Payment payment) {
        database.put(payment.getId(), payment);
    }

    public Optional<Payment> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<Payment> findAll() {
        return new ArrayList<>(database.values());
    }

    public boolean exists(UUID id) {
        return database.containsKey(id);
    }
}
