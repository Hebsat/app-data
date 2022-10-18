package org.example.controllers;

import org.example.exceptions.ErrorMessages;
import org.example.model.Message;
import org.example.model.User;
import org.example.request.Auth;
import org.example.request.UserMessage;
import org.example.response.AuthResponse;
import org.example.services.AuthenticateServiceImpl;
import org.example.services.JWTTokenServiceImpl;
import org.example.services.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticateServiceImpl authenticateService;
    @MockBean
    private JWTTokenServiceImpl jwtTokenService;
    @MockBean
    private MessageServiceImpl messageService;

    @Test
    void loginWithInvalidUsername() throws Exception {
        when(authenticateService.getUser(any())).thenReturn(null);
        mockMvc.perform(post("/admin/login")
                        .content(objectMapper.writeValueAsString(new Auth()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(ErrorMessages.INVALID_USERNAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithInvalidPassword() throws Exception {
        when(authenticateService.getUser(any())).thenReturn(new User());
        when(authenticateService.validateUser(any(), any())).thenReturn(false);
        mockMvc.perform(post("/admin/login")
                        .content(objectMapper.writeValueAsString(new Auth()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(ErrorMessages.INVALID_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login() throws Exception {
        when(authenticateService.getUser(any())).thenReturn(new User());
        when(authenticateService.validateUser(any(), any())).thenReturn(true);
        when(jwtTokenService.createToken(any())).thenReturn(new AuthResponse("test_token"));
        mockMvc.perform(post("/admin/login")
                        .content(objectMapper.writeValueAsString(new Auth()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(status().isOk());
    }

    @Test
    void sendMessageWithInvalidToken() throws Exception {
        when(jwtTokenService.validateToken(any(), any())).thenReturn(false);
        mockMvc.perform(post("/admin/send")
                        .content(objectMapper.writeValueAsString(new UserMessage()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ""))
                .andDo(print())
                .andExpect(content().string(ErrorMessages.INVALID_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    void sendAnyMessage() throws Exception {
        when(jwtTokenService.validateToken(any(), any())).thenReturn(true);
        when(messageService.saveMessage(any())).thenReturn(new Message());
        mockMvc.perform(post("/admin/send")
                        .content(objectMapper.writeValueAsString(new UserMessage()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ""))
                .andDo(print())
                .andExpect(content().string(ErrorMessages.MESSAGE_SAVED))
                .andExpect(status().isOk());
    }

    @Test
    void sendHistoryMessage() throws Exception {
        when(jwtTokenService.validateToken(any(), any())).thenReturn(true);
        when(messageService.saveMessage(any())).thenReturn(new Message());
        when(messageService.messageResolver(any())).thenReturn(true);
        mockMvc.perform(post("/admin/send")
                        .content(objectMapper.writeValueAsString(new UserMessage()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ""))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(status().isOk());
    }
}