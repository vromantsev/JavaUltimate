package com.bobocode.service;

public interface MessageService {

    String createMessage(String template);

    void sendMessage(String payload);

}
