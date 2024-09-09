package com.Transfer.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private Long senderAccountId;
    private Long recipientAccountId;
    private Double amount;
    private String currency;
    private String status;
    private String description;


}