package com.app.security.events;

import com.app.exceptions.MyException;
import com.app.model.security.User;
import com.app.service.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RegisterListener implements ApplicationListener<OnRegisterData> {

    private static final String SUBJECT = "ELECTIONS-APP registration confirmation message";
    private static final String MESSAGE_TEMPLATE = "CLICK ACTIVATION LINK: ";

    private UserService userService;
    private JavaMailSender javaMailSender;

    public RegisterListener(UserService userService, @Qualifier("javaMailSender") JavaMailSender javaMailSender) {
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(OnRegisterData data) {
        createVerificationTokenAndSendEmail(data);
    }

    private void createVerificationTokenAndSendEmail(OnRegisterData data) {
        try {
            Optional.ofNullable(data).orElseThrow(() -> new MyException("OnRegisterData is null"));
            User user = Optional.ofNullable(data.getUser()).orElseThrow(() -> new MyException(""));
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(user, token);

            String recipientAddress = Optional.ofNullable(user.getEmail()).orElseThrow(() -> new MyException("Missing user email"));

            String confirmationUrl = data.getUrl() + "users/registrationConfirm?token=" + token;

            javaMailSender.send(createSimpleMailMessage(recipientAddress, MESSAGE_TEMPLATE + confirmationUrl));
        } catch (Exception e) {
            throw new MyException("Failed to create verification token and send email");
        }
    }

    private static SimpleMailMessage createSimpleMailMessage(final String recipientAddress, final String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipientAddress);
        simpleMailMessage.setSubject(SUBJECT);
        simpleMailMessage.setText(message);
        return simpleMailMessage;
    }
}
