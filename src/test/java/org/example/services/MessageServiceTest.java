package org.example.services;

import org.example.model.Message;
import org.example.model.User;
import org.example.repositories.MessageRepository;
import org.example.request.UserMessage;
import org.example.response.ResponseMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {

    private final MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    MessageServiceTest(MessageService messageService) {
        this.messageService = messageService;
    }

    UserMessage userMessage1;
    UserMessage userMessage2;
    List<Message> messageList;

    @BeforeEach
    void setUp() {
        userMessage1 = new UserMessage("test_user1", "User1's_message");
        userMessage2 = new UserMessage("test_user2", "history 2");
        messageList = new ArrayList<>();
        messageList.add(new Message(userMessage1.getMessage(), new User()));
        messageList.add(new Message(userMessage2.getMessage(), new User()));
    }

    @AfterEach
    void tearDown() {
        userMessage1 = null;
        userMessage2 = null;
        messageList = null;
    }

    @Test
    void saveMessage() {
        when(messageRepository.save(any())).thenReturn(new Message(userMessage1.getMessage(), new User()));
        assertTrue(messageService.saveMessage(userMessage1).getMessage().matches(userMessage1.getMessage()));
    }

    @Test
    void messageResolver() {
        assertTrue(messageService.messageResolver(userMessage2.getMessage()));
        assertFalse(messageService.messageResolver(userMessage1.getMessage()));
    }

    @Test
    void getHistory() {
        when(messageRepository.findLastMessages(2)).thenReturn(messageList);
        List<ResponseMessage> messageList = messageService.getHistory(userMessage2.getMessage());
        assertNotNull(messageList);
        assertEquals(2, messageList.size());
        assertTrue(messageList.get(0).getMessage().matches(userMessage1.getMessage()) &&
                messageList.get(1).getMessage().matches(userMessage2.getMessage()));
    }
}