package ru.nerzhaveyka.wallet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nerzhaveyka.wallet.WalletOperationRequestDto;
import ru.nerzhaveyka.wallet.entities.Wallet;
import ru.nerzhaveyka.wallet.exceptions.InsufficientFundsException;
import ru.nerzhaveyka.wallet.exceptions.WalletNotFoundException;
import ru.nerzhaveyka.wallet.servicies.WalletService;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class MainController {
    private WalletService walletService;

    @Autowired
    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createWallet() {
        walletService.createWallet();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/balance/{walletId}")
    public ResponseEntity<String> getWalletBalance(@PathVariable String walletId) {
        try {
            Wallet wallet = walletService.getWalletByWalletId(UUID.fromString(walletId));
            return ResponseEntity.ok("Баланс кошелька " + walletId + " равен: " + wallet.getBalance());
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("Кошелек не найден");
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Wallet>> getAll(){
        return new ResponseEntity<>(walletService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/operation")
    public ResponseEntity<String> operationWithBalanceOfWallet(@RequestBody WalletOperationRequestDto requestDto){
        try{
            walletService.operationWithBalanceOfWallet(requestDto.getWalletId(), requestDto.getOperationType(), requestDto.getAmount());
            return ResponseEntity.ok("Операция по изменению баланса прошла успешно");
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (WalletNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (InsufficientFundsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (ConcurrentModificationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
        catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
