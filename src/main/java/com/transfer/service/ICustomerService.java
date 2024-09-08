package com.transfer.service;

import com.transfer.dto.ChangePasswordDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.UpdateCustomerDTO;
import com.transfer.exception.custom.InvalidOldPasswordException;
import com.transfer.exception.custom.ResourceNotFoundException;

public interface ICustomerService {

    /**
     * Get customer by id
     *
     * @param customerId the customer id
     * @return the created customer
     * @throws ResourceNotFoundException if the customer is not found
     */
    CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException;

    /**
     * Update customer profile
     *
     * @param customerId the customer id
     * @param updateCustomerDTO the data to update
     * @return the updated customer
     * @throws ResourceNotFoundException if the customer is not found
     */
    CustomerDTO updateCustomerProfile(Long customerId, UpdateCustomerDTO updateCustomerDTO) throws ResourceNotFoundException;

    /**
     * Change customer password
     *
     * @param customerId the customer id
     * @param changePasswordDTO the DTO with old and new password
     * @throws ResourceNotFoundException if the customer is not found
     * @throws InvalidOldPasswordException if the old password is incorrect
     */
    void changeCustomerPassword(Long customerId, ChangePasswordDTO changePasswordDTO)
            throws ResourceNotFoundException, InvalidOldPasswordException;
}
