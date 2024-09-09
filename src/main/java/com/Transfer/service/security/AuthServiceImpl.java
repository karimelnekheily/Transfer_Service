package com.Transfer.service.security;


import com.Transfer.DTO.LoginRequestDTO;
import com.Transfer.DTO.LoginResponseDTO;
import com.Transfer.DTO.RegisterCustomerRequest;
import com.Transfer.DTO.RegisterCustomerResponse;
import com.Transfer.DTO.enums.AccountCurrency;
import com.Transfer.DTO.enums.AccountType;
import com.Transfer.entity.Account;
import com.Transfer.entity.Customer;
import com.Transfer.exception.custom.CustomerAlreadyExistException;
import com.Transfer.exception.custom.ResourceNotFoundException;
import com.Transfer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomerAlreadyExistException("Customer with email " + customerRequest.getEmail() + " already exists");
        }

        Customer customer = Customer.builder()
                .email(customerRequest.getEmail())
                .password(this.passwordEncoder.encode(customerRequest.getPassword()))
                .name(customerRequest.getName())
                .phonenumber(customerRequest.getPhonenumber())
                .country(customerRequest.getCountry())
                .build();

        Account account = Account.builder()
                .balance(0.0)
                .accountType(AccountType.SAVINGS)
                .accountDescription("Savings Account")
                .accountName("Savings Account")
                .currency(AccountCurrency.EGP)
                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
                .customer(customer)
                .build();

        customer.getAccounts().add(account);

        Customer savedCustomer = customerRepository.save(customer);

        return savedCustomer.toResponse();
    }
    @Override
    @Transactional
    public void updatePassword(String email, String oldPassword, String newPassword) throws     ResourceNotFoundException {
        // Validate password strength (optional)
        validatePasswordStrength(newPassword);

        Customer customer = customerRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Customer with email %s not found", email)));

        if (!passwordEncoder.matches(oldPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, customer.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

    private void validatePasswordStrength(String password) {
        // Example password strength validation
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        // Add more validations as needed (e.g., uppercase, numbers, special characters)
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();
    }
}