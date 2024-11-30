package com.transfer.service.security;

import com.transfer.constants.ApplicationConstants;
import com.transfer.dto.LoginRequestDTO;
import com.transfer.dto.LoginResponseDTO;
import com.transfer.dto.RegisterCustomerRequest;
import com.transfer.dto.RegisterCustomerResponse;
import com.transfer.entity.Account;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.CustomerAlreadyExistException;
import com.transfer.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;


    @Transactional
    public RegisterCustomerResponse register(RegisterCustomerRequest customerRequest) throws CustomerAlreadyExistException {

        if (Boolean.TRUE.equals(this.customerRepository.existsByEmail(customerRequest.getEmail()))) {
            throw new CustomerAlreadyExistException(ApplicationConstants.CUSTOMER_ALREADY_EXISTS + " " + customerRequest.getEmail());
        }

        Customer customer = Customer.builder()
                .email(customerRequest.getEmail())
                .password(this.passwordEncoder.encode(customerRequest.getPassword()))
                .name(customerRequest.getName())
                .country(customerRequest.getCountry())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .build();

        Customer savedCustomer = customerRepository.save(customer); // Save customer first to get customer ID


        Account account = Account.builder()
                .id(customer.getId())
                .balance(0.0)
                .accountName(ApplicationConstants.BANK_ACCOUNT.toString())
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .customer(customer)
                .build();

        savedCustomer.setAccount(account);

        customerRepository.save(savedCustomer);

        return savedCustomer.toResponse();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomerDetailsImpl userDetails = (CustomerDetailsImpl) authentication.getPrincipal();
        Long customerId = userDetails.getId();  // Get customer ID

        return LoginResponseDTO.builder()
                .token(jwt)
                .id(customerId)  // Add customer ID to response
                .message(ApplicationConstants.LOGIN_SUCCESSFULLY.toString())
                .status(HttpStatus.ACCEPTED)
                .tokenType(ApplicationConstants.BEARER.toString())
                .build();
    }

    @Override
    public String parseJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
