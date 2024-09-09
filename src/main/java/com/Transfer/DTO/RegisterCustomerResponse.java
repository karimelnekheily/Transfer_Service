package com.Transfer.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterCustomerResponse {

    private Long id;

    private String name;

    private String email;


    private String phonenumber;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}