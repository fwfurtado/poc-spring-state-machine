package com.github.fehwilinando.statemachine.paymentstate;

import com.github.fehwilinando.statemachine.model.PaymentStatus;
import com.github.fehwilinando.statemachine.repository.PaymentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
public class PaymentStateHandler {

    private final StateMachine<PaymentStatus, PaymentEvent> machine;
    private final PaymentRepository repository;
    private final PaymentStateInterceptor interceptor;

    public PaymentStateHandler(StateMachine<PaymentStatus, PaymentEvent> machine, PaymentRepository repository, PaymentStateInterceptor interceptor) {
        this.machine = machine;
        this.repository = repository;
        this.interceptor = interceptor;
    }


    public void pay(UUID id) {


        repository.findById(id).ifPresent(payment -> {

//            StateMachine<PaymentStatus, PaymentEvent> machine = factoryStateMachineBy(payment);

            Message<PaymentEvent> event = MessageBuilder.withPayload(PaymentEvent.PAY)
                    .setHeaderIfAbsent("payment", payment)
                    .build();

            machine.sendEvent(event);

        });

    }


    public void cancel(UUID id) {
        repository.findById(id).ifPresent(payment -> {
//            StateMachine<PaymentStatus, PaymentEvent> machine = factoryStateMachineBy(payment);

            Message<PaymentEvent> event = MessageBuilder.withPayload(PaymentEvent.CANCEL)
                    .setHeaderIfAbsent("payment", payment)
                    .build();

            machine.sendEvent(event);
        });
    }


    @PostConstruct
    private StateMachine<PaymentStatus, PaymentEvent> factoryStateMachineBy() {

//        StateMachine<PaymentStatus, PaymentEvent> machine = machine.getStateMachine(payment.getId());

//        machine.stop();

        machine
                .getStateMachineAccessor()
                .doWithAllRegions(stateMachineAccess -> {
                    stateMachineAccess.addStateMachineInterceptor(interceptor);
//                    stateMachineAccess.resetStateMachine(new DefaultStateMachineContext<>(payment.getStatus(), null, null, null));
                });


//        machine.start();


        return machine;
    }
}