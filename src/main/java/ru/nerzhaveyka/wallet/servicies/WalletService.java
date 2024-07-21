package ru.nerzhaveyka.wallet.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nerzhaveyka.wallet.OperationTypes;
import ru.nerzhaveyka.wallet.entities.Wallet;
import ru.nerzhaveyka.wallet.exceptions.InsufficientFundsException;
import ru.nerzhaveyka.wallet.exceptions.WalletNotFoundException;
import ru.nerzhaveyka.wallet.repositories.WalletRepository;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {
    private WalletRepository walletRepository;

    @Autowired
    public void setWalletRepository(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    public Wallet getWalletByWalletId(UUID walletId) {
        return walletRepository.findByWalletId(walletId);
    }
    public Wallet createWallet() {
        double startBalance = 0.0;
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setBalance(startBalance);
        return walletRepository.save(wallet);
    }
    public Wallet getWalletById(Long id){
        return walletRepository.getWalletById(id);
    }
    public List<Wallet> getAll(){
        return walletRepository.findAll();
    }
    @Transactional
    public Wallet operationWithBalanceOfWallet(UUID walletId, OperationTypes operationType, double amount) throws Exception {
        validateOperationParameters(walletId, operationType, amount);

        Wallet wallet = walletRepository.findByWalletId(walletId);
        if (wallet == null) {
            throw new WalletNotFoundException("Кошелек с wallet_id " + walletId + " не найден");
        }

        long version = wallet.getVersion();

        double newBalance = operationType == OperationTypes.DEPOSIT ? wallet.getBalance() + amount : wallet.getBalance() - amount;
        if (newBalance <= 0) {
            throw new InsufficientFundsException("Недостаточно средств для вывода");
        }

        wallet.setBalance(newBalance);
        wallet.setVersion(version + 1);

        try{
            walletRepository.save(wallet);
        }
        catch (OptimisticLockingFailureException ex){
            throw new ConcurrentModificationException("Обнаружена одновременная попытка изменения баланса кошелька");
        }
        return wallet;
    }
    private void validateOperationParameters(UUID walletId, OperationTypes operationType, double amount) {
        if (walletId == null || walletId.toString().isEmpty()) {
            throw new IllegalArgumentException("walletId не может быть null или пустым");
        }
        if (operationType == null) {
            throw new IllegalArgumentException("operationType не может быть null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount должен быть положительным");
        }
    }
}
