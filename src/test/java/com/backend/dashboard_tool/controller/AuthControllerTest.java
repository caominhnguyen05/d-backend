package com.backend.dashboard_tool.controller;

import com.backend.dashboard_tool.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenUtil jwtTokenUtil;

    // Use a spy to override passwordEncoder.matches for test
    private final BCryptPasswordEncoder passwordEncoder = spy(new BCryptPasswordEncoder());

    /**
     * Test login with correct password.
     */
    @Test
    void loginSuccess() throws Exception {
        String rawPassword = "metadata@Ret2025";
        String passwordJson = "\"" + rawPassword + "\"";
        String fakeToken = "fake.jwt.token";

        // Simulate passwordEncoder.matches returning true
        AuthController controller = new AuthController();
        // Use reflection to set the passwordEncoder field for the test instance
        java.lang.reflect.Field encoderField = AuthController.class.getDeclaredField("passwordEncoder");
        encoderField.setAccessible(true);
        encoderField.set(controller, passwordEncoder);

        when(passwordEncoder.matches(rawPassword, "$2a$12$djVp7pPvgWxHSj9f8nVRjubnS5FMcyG0sjHkIn1hn.gFmmMLCG.26")).thenReturn(true);
        when(jwtTokenUtil.generateToken()).thenReturn(fakeToken);

        mockMvc.perform(post("/api/auth/login")
                .content(passwordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(fakeToken))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    /**
     * Test login with incorrect password.
     */
    @Test
    void loginFailure() throws Exception {
        String wrongPassword = "wrongpassword";
        String passwordJson = "\"" + wrongPassword + "\"";

        // Simulate passwordEncoder.matches returning false
        AuthController controller = new AuthController();
        java.lang.reflect.Field encoderField = AuthController.class.getDeclaredField("passwordEncoder");
        encoderField.setAccessible(true);
        encoderField.set(controller, passwordEncoder);

        when(passwordEncoder.matches(wrongPassword, "$2a$12$djVp7pPvgWxHSj9f8nVRjubnS5FMcyG0sjHkIn1hn.gFmmMLCG.26")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .content(passwordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password"));
    }
}
