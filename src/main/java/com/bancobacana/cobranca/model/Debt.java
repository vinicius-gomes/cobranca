package com.bancobacana.cobranca.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Debt {

    private Long id;
    private UserDetails userDetails;
    private String ownedAmount;
    private DebtStatus status;
    private String expirationDate;
}
