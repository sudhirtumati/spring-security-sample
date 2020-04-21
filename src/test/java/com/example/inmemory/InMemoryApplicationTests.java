package com.example.inmemory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class InMemoryApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_401_when_credentials_are_not_provided() throws Exception {
        mockMvc.perform(get("/greetings"))
                .andExpect(unauthenticated());
    }

    @Test
    void should_return_401_when_username_is_not_found() throws Exception {
        mockMvc.perform(get("/greetings")
                .with(httpBasic("invalid_user", "password1")))
                .andExpect(unauthenticated());
    }

    @Test
    void should_return_401_when_password_is_wrong() throws Exception {
        mockMvc.perform(get("/greetings")
                .with(httpBasic("user1", "password123")))
                .andExpect(unauthenticated());
    }

    @Test
    void should_return_200_when_credentials_are_valid() throws Exception {
        mockMvc.perform(get("/greetings")
                .with(httpBasic("user1", "password1")))
                .andExpect(authenticated().withUsername("user1"));
    }
}
