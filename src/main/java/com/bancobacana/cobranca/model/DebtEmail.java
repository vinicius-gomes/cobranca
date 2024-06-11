package com.bancobacana.cobranca.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DebtEmail {

    String costumerName;
    String emailAddress;
    String ownedAmount;
    String expirationDate;

    public static DebtEmail convert(Debt debt) {
        return DebtEmail.builder()
                .costumerName(debt.getUserDetails().getFirstName() + " " + debt.getUserDetails().getSurname())
                .emailAddress(debt.getUserDetails().getEmail())
                .ownedAmount(debt.getOwnedAmount())
                .expirationDate(debt.getExpirationDate())
                .build();
    }
}
