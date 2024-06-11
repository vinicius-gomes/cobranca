package com.bancobacana.cobranca.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DebtStatus {
    @JsonProperty("expired")
    EXPIRED,
    @JsonProperty("closed")
    CLOSED,
    @JsonProperty("opened")
    OPENED;
}
