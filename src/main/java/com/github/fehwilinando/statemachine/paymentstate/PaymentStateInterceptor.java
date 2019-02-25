package com.github.fehwilinando.statemachine.paymentstate;

import com.github.fehwilinando.statemachine.model.Payment;
import com.github.fehwilinando.statemachine.model.PaymentStatus;
import com.github.fehwilinando.statemachine.repository.PaymentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Configuration
class PaymentStateInterceptor extends StateMachineInterceptorAdapter<PaymentStatus, PaymentEvent> {

    private final PaymentRepository repository;

    PaymentStateInterceptor(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void preStateChange(State<PaymentStatus, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentStatus, PaymentEvent> transition, StateMachine<PaymentStatus, PaymentEvent> stateMachine) {

        if (isNull(message)) {
            return;
        }

        Optional<Payment> optionalPayment = Optional.ofNullable(message.getHeaders().get("payment", Payment.class));

        optionalPayment.ifPresent(payment -> payment.setStatus(state.getId()) );
    }
}
