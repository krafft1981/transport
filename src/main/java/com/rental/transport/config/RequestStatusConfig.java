package com.rental.transport.config;

import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.events.RequestEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
public class RequestStatusConfig {

    public void configure(StateMachineStateConfigurer<RequestStatusEnum, RequestEvent> states) throws Exception {
        states.withStates()
                .initial(RequestStatusEnum.NEW)
                .states(EnumSet.allOf(RequestStatusEnum.class));
    }

    public void configure(StateMachineTransitionConfigurer<RequestStatusEnum, RequestEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(RequestStatusEnum.NEW)
                .target(RequestStatusEnum.EXPIRED)
                .event(RequestEvent.JOIN);
//                .action(myActions.enterDataAction());
    }
}
