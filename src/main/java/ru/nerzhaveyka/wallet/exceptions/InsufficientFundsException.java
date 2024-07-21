package ru.nerzhaveyka.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message){
        super(message);
    }
}