package de.varilx.veconomy.transaction.type;


import de.varilx.utils.language.LanguageUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.transaction.type
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TransactionType {

    ADD(LanguageUtils.getMessageString("add")),
    REMOVE(LanguageUtils.getMessageString("remove")),
    ADMIN_ADD(LanguageUtils.getMessageString("admin_add")),
    ADMIN_REMOVE(LanguageUtils.getMessageString("admin_remove")),
    ADMIN_SET(LanguageUtils.getMessageString("admin_set")),
    ADMIN_RESET(LanguageUtils.getMessageString("admin_reset"));

    String displayName;

}
