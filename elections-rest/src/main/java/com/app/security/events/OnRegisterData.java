package com.app.security.events;

import com.app.model.security.User;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegisterData extends ApplicationEvent {

    private User user;
    private String url;

    public OnRegisterData(User user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
