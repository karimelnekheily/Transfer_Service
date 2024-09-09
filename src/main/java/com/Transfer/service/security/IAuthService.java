package com.Transfer.service.security;

import com.Transfer.DTO.LoginRequestDTO;
import com.Transfer.DTO.LoginResponseDTO;
import com.Transfer.DTO.RegisterCustomerRequest;
import com.Transfer.DTO.RegisterCustomerResponse;
import com.Transfer.exception.custom.CustomerAlreadyExistException;
import com.Transfer.exception.custom.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface IAuthService {

    /**
     * Register a new customer
     *
     * @param customer the customer to be registered
     * @return the registered customer
     * @throws CustomerAlreadyExistException if the customer already exists
     */
    RegisterCustomerResponse register(RegisterCustomerRequest customer) throws CustomerAlreadyExistException;


    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    /**
     *
     * @param email
     * @param oldPassword
     * @param newPassword
     * @throws ResourceNotFoundException
     */

    @Transactional
    void updatePassword(String email, String oldPassword, String newPassword) throws ResourceNotFoundException;

}
