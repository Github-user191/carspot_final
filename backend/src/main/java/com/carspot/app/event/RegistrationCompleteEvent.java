package com.carspot.app.event;

import com.carspot.app.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

// Application events allows us to throw and listen to specific application events that can be processed further
// Here i'm creating an event to be handled when a user registers
// The EventListener listens for ApplicationEvents and performs actions once an Event occurs
// It is Synchronous, meaning when a user registers, execution will pause and the EventListener will first
// generate the confirmation token with Service method and then send the confirmation email before the account is created


@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    // URL we create for the user to confirm account
    // confirm?token=xxx
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
