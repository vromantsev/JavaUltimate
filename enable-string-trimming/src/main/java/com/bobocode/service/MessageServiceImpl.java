package com.bobocode.service;

import com.magic.trimming.annotation.Trimmed;
import org.springframework.stereotype.Service;

@Trimmed
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public String createMessage(String template) {
        return "     Message from: %s".formatted(template);
    }

    @Override
    public void sendMessage(String payload) {
        System.out.println(payload);
    }
}
