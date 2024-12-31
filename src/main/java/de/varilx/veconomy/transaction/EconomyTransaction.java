package de.varilx.veconomy.transaction;


import de.varilx.database.id.MongoId;
import de.varilx.veconomy.transaction.type.TransactionType;
import de.varilx.veconomy.user.EconomyUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.transaction
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EconomyTransaction {

    @MongoId
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID transactionId;

    UUID accountId;
    double amount, balance;
    TransactionType type;
    long time;

    @ManyToOne
    EconomyUser user;

}
