package com.github.fehwilinando.statemachine.paymentstate;

import com.github.fehwilinando.statemachine.model.PaymentStatus;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@EnableStateMachine
public class PaymentStateMachineConfig extends EnumStateMachineConfigurerAdapter<PaymentStatus, PaymentEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStatus, PaymentEvent> config) throws Exception {
        config.withConfiguration()
                    .autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PaymentStatus, PaymentEvent> states) throws Exception {
        states
            .withStates()
                .initial(PaymentStatus.SUBMITED)
                .end(PaymentStatus.CONFIRMED)
                .end(PaymentStatus.CANCELLED)
                .states(EnumSet.allOf(PaymentStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStatus, PaymentEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(PaymentStatus.SUBMITED)
                .target(PaymentStatus.CONFIRMED)
                .event(PaymentEvent.PAY).and()
            .withExternal()
                .source(PaymentStatus.SUBMITED)
                .target(PaymentStatus.CANCELLED)
                .event(PaymentEvent.CANCEL);
    }

}
