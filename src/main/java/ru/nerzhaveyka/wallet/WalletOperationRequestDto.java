package ru.nerzhaveyka.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletOperationRequestDto {
    @JsonProperty("walletId")
    private UUID walletId;
    @JsonProperty("operationType")
    private OperationTypes operationType;
    @JsonProperty("amount")
    private double amount;
}
