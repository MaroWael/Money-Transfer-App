package com.transfer.service;

import com.transfer.dto.ChangePasswordDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.UpdateCustomerDTO;
import com.transfer.entity.Customer;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"))
                .toDTO();
    }

    @Override
    public CustomerDTO updateCustomerProfile(Long customerId, UpdateCustomerDTO updateCustomerDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Update customer fields
        customer.setName(updateCustomerDTO.getName() != null ? updateCustomerDTO.getName() : customer.getName());
        customer.setEmail(updateCustomerDTO.getEmail() != null ? updateCustomerDTO.getEmail() : customer.getEmail());
        customer.setCountry(updateCustomerDTO.getCountry() != null ? updateCustomerDTO.getCountry() : customer.getCountry());
        customer.setDateOfBirth(updateCustomerDTO.getDateOfBirth() != null ? updateCustomerDTO.getDateOfBirth() : customer.getDateOfBirth());

        // Save updated customer
        Customer updatedCustomer = customerRepository.save(customer);

        return updatedCustomer.toDTO();
    }

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void changeCustomerPassword(Long customerId, ChangePasswordDTO changePasswordDTO)
            throws ResourceNotFoundException, InvalidOldPasswordException {

        // Fetch customer from the repository
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Validate the old password
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), customer.getPassword())) {
            throw new InvalidOldPasswordException("Old password is incorrect");
        }

        // Update the password and save
        customer.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        customerRepository.save(customer);
    }

}
