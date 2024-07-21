package ru.nerzhaveyka.wallet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;
    @Column(nullable = false, name = "wallet_id", unique = true)
    private UUID walletId;
    @Column(nullable = false)
    private double balance;
    @Version
    private Long version;
}
