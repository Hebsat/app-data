package org.example.repositories;

import org.example.model.Message;
import org.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.findByUsername("test_user1");
        message = new Message("test_message", user);
        messageRepository.save(message);
    }

    @AfterEach
    void tearDown() {
        message = null;
        user = null;
    }

    @Test
    void findLastMessages() {
        assertEquals(1, messageRepository.findLastMessages(1).size());
        assertTrue(messageRepository.findLastMessages(1).get(0).getMessage().matches(message.getMessage()));
    }
}