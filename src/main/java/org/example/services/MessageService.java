package org.example.services;

import org.example.model.Message;
import org.example.request.UserMessage;
import org.example.response.ResponseMessage;

import java.util.List;

public interface MessageService {
    Message saveMessage(UserMessage userMessage);

    boolean messageResolver(String message);

    List<ResponseMessage> getHistory(String message);
}
