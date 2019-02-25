package com.github.fehwilinando.statemachine.controller;

import com.github.fehwilinando.statemachine.paymentstate.PaymentEvent;
import com.github.fehwilinando.statemachine.controller.input.TransactionRequest;
import com.github.fehwilinando.statemachine.model.Payment;
import com.github.fehwilinando.statemachine.paymentstate.PaymentStateHandler;
import com.github.fehwilinando.statemachine.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("payments")
public class PaymentController {
    private final PaymentRepository repository;
    private final PaymentStateHandler handler;

    public PaymentController(PaymentRepository repository, PaymentStateHandler handler) {
        this.repository = repository;
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Payment> save(@RequestBody @Valid TransactionRequest transaction) {

        Payment payment = Payment.from(transaction);

        repository.save(payment);

        URI uri = UriComponentsBuilder.fromPath("/payments/{id}").build(payment.getId());

        return created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Payment> show(@PathVariable UUID id) {

        return repository
                .findById(id)
                    .map(ok()::body)
                        .orElseGet(notFound()::build);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> list() {

        List<Payment> payments = repository.findAll();

        if (payments.isEmpty()) {
            return notFound().build();
        }

        return ok(payments);
    }


    @PutMapping("{id}")
    public ResponseEntity<Payment> pay(@PathVariable UUID id) {

        if (!repository.exists(id)) {
            return notFound().build();
        }

        handler.pay(id);

        return noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Payment> cancel(@PathVariable UUID id) {

        if(!repository.exists(id)) {
            return notFound().build();
        }

        handler.cancel(id);

        return noContent().build();
    }
}
