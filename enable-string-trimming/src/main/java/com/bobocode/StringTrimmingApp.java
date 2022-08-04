package com.bobocode;

import com.bobocode.service.MessageService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StringTrimmingApp {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("com.bobocode");
        final MessageService messageService = context.getBean(MessageService.class);
        final String message = messageService.createMessage(" test ");
        System.out.println(message);
    }
}
