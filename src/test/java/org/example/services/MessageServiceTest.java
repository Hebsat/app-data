package org.example.services;

import org.example.request.UserMessage;
import org.example.response.ResponseMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class MessageServiceTest {

    private final MessageService messageService;

    @Autowired
    MessageServiceTest(MessageService messageService) {
        this.messageService = messageService;
    }

    UserMessage userMessage1;
    UserMessage userMessage2;

    @BeforeEach
    void setUp() {
        userMessage1 = new UserMessage("test_user1", "User1's_message");
        userMessage2 = new UserMessage("test_user2", "history 2");
    }

    @AfterEach
    void tearDown() {
        userMessage1 = null;
        userMessage2 = null;
    }

    @Test
    void saveMessage() {
        assertTrue(messageService.saveMessage(userMessage1).getMessage().matches(userMessage1.getMessage()));
    }

    @Test
    void messageResolver() {
        assertTrue(messageService.messageResolver(userMessage2.getMessage()));
        assertFalse(messageService.messageResolver(userMessage1.getMessage()));
    }

    @Test
    void getHistory() {
        messageService.saveMessage(userMessage1);
        messageService.saveMessage(userMessage2);
        List<ResponseMessage> messageList = messageService.getHistory(userMessage2.getMessage());
        assertNotNull(messageList);
        assertEquals(2, messageList.size());
        System.out.println(messageList.size());
        assertTrue(messageList.get(0).getUsername().matches(userMessage2.getName()) &&
                messageList.get(0).getMessage().matches(userMessage2.getMessage()) &&
                messageList.get(1).getUsername().matches(userMessage1.getName()) &&
                messageList.get(1).getMessage().matches(userMessage1.getMessage()));
    }
}