package ru.nerzhaveyka.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConcurrentModificationException extends Exception {
    public ConcurrentModificationException(String message){
        super(message);
    }
}
