package com.github.fehwilinando.statemachine.model;

import com.github.fehwilinando.statemachine.controller.input.TransactionRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Payment {
    private UUID id;
    private BigDecimal value;
    private PaymentStatus status;
    private LocalDate date;

    private Payment(LocalDate date, BigDecimal value) {
        this.id = UUID.randomUUID();
        this.status = PaymentStatus.SUBMITED;
        this.value = value;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public static Payment from(TransactionRequest transaction) {
        return new Payment(transaction.getDate(), transaction.getValue());
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
