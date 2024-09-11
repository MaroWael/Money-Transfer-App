package com.transfer.service;

import com.transfer.dto.LoginRequestDTO;
import com.transfer.dto.LoginResponseDTO;
import com.transfer.dto.RegisterCustomerRequest;
import com.transfer.dto.RegisterCustomerResponse;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.CustomerAlreadyExistException;
import com.transfer.repository.CustomerRepository;
import com.transfer.service.security.AuthServiceImpl;
import com.transfer.service.security.JwtUtils;
import com.transfer.service.security.CustomerDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Test
    void testRegisterSuccess() throws CustomerAlreadyExistException {
        RegisterCustomerRequest request = new RegisterCustomerRequest(
                "John Doe", "USA", "john@example.com", "password123", LocalDate.of(1990, 1, 1)
        );

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(1L);
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            return customer;
        });

        RegisterCustomerResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("USA", response.getCountry());
    }

    @Test
    void testRegisterCustomerAlreadyExists() {
        RegisterCustomerRequest request = new RegisterCustomerRequest(
                "John Doe", "USA", "john@example.com", "password123", LocalDate.of(1990, 1, 1)
        );

        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(CustomerAlreadyExistException.class, () -> authService.register(request));
    }

    @Test
    void testLoginSuccess() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("john@example.com", "password123");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String jwtToken = "mock-jwt-token";
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwtToken);

        CustomerDetailsImpl userDetails = new CustomerDetailsImpl(1L, "","");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        LoginResponseDTO response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("Login Successful", response.getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getStatus());
        assertEquals("Bearer", response.getTokenType());
    }
}
