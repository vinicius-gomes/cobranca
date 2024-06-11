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
public class UserDetails {

    private String firstName;
    private String surname;
    private String email;
    private String telephone;
    private String address;
    private String cpf;

}
