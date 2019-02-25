package com.github.fehwilinando.statemachine.controller.input;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.StringJoiner;

public class TransactionRequest {
    @NotNull
    @Future
    private LocalDate date;

    @NotNull
    @DecimalMin("5.0")
    private BigDecimal value;

    /**
     * @deprecated frameworks eyes only
     */
    @Deprecated
    private TransactionRequest() {
    }

    public TransactionRequest(BigDecimal value) {
        this.date = LocalDate.now();
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionRequest.class.getSimpleName() + "[", "]")
                .add("date=" + date)
                .add("value=" + value)
                .toString();
    }
}
