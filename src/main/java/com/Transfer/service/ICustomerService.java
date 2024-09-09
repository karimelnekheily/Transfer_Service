package com.Transfer.service;

import com.Transfer.DTO.CustomerDTO;
import com.Transfer.exception.custom.ResourceNotFoundException;

public interface ICustomerService {

    /**
     * Get customer by id
     *
     * @param customerId the customer id
     * @return the created customer
     * @throws ResourceNotFoundException if the customer is not found
     */
    CustomerDTO getCustomerById(Long customerId) throws ResourceNotFoundException;
}