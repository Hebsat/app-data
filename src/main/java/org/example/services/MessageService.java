package org.example.services;

import org.example.model.Message;
import org.example.model.User;
import org.example.repositories.MessageRepository;
import org.example.repositories.UserRepository;
import org.example.request.UserMessage;
import org.example.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public Message saveMessage(UserMessage userMessage) {
        User user = userRepository.findByUsername(userMessage.getName());
        return messageRepository.save(new Message(userMessage.getMessage(), user));
    }

    public boolean messageResolver(String message) {
        return message.matches("history [\\d]+");
    }

    public List<ResponseMessage> getHistory(String message) {
        int count = historyMessageParser(message);
        List<Message> messageList = new ArrayList<>(messageRepository.findLastMessages(count));
        return getResponseMessages(messageList);
    }

    private int historyMessageParser(String message) {
        String[] msg = message.split(" ");
        return Integer.parseInt(msg[1]);
    }

    private List<ResponseMessage> getResponseMessages(List<Message> messageList) {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        messageList.forEach(message -> {
            ResponseMessage responseMessage = new ResponseMessage(
                    message.getId(),
                    message.getUser().getUsername(),
                    message.getMessage());
            responseMessageList.add(responseMessage);
        });
        return responseMessageList;
    }
}
