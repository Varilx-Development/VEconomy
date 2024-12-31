package de.varilx.veconomy.user;


import de.varilx.database.id.MongoId;
import de.varilx.veconomy.transaction.EconomyTransaction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.user
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EconomyUser {

    @MongoId
    @jakarta.persistence.Id
    UUID uniqueId;

    String name;
    double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<EconomyTransaction> transactions;

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void removeBalance(double amount) {
        this.balance -= amount;
    }

    public void addTransaction(EconomyTransaction transaction) {
        this.transactions.add(transaction);
    }

}
