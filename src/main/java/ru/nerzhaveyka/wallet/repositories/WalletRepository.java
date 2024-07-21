package ru.nerzhaveyka.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nerzhaveyka.wallet.entities.Wallet;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    public Wallet findByWalletId(UUID walletId);
    public Wallet getWalletById(Long id);
}
