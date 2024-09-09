package com.Transfer.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CustomerDTO {

    private Long CustomerId;

    private String name;

    private String email;

    private String phonenumber;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<AccountDTO> accounts;
}