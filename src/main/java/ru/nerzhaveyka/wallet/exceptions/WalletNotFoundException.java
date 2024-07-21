package ru.nerzhaveyka.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException(String message){
        super(message);
    }
}
