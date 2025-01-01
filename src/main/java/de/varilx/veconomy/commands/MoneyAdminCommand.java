package de.varilx.veconomy.commands;


import de.varilx.command.VaxCommand;
import de.varilx.database.repository.Repository;
import de.varilx.utils.NumberUtils;
import de.varilx.utils.language.LanguageUtils;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.gui.TransactionsGui;
import de.varilx.veconomy.transaction.EconomyTransaction;
import de.varilx.veconomy.transaction.type.TransactionType;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.commands
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MoneyAdminCommand extends VaxCommand {

    VEconomy plugin;
    Repository<EconomyUser, UUID> repository;

    public MoneyAdminCommand(VEconomy plugin) {
        super(LanguageUtils.getMessageString("Commands.MoneyAdmin.Name"));
        this.plugin = plugin;
        this.repository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(sender instanceof Player player) {
            if(!player.hasPermission(LanguageUtils.getMessageString("Commands.MoneyAdmin.Permission"))) {
                player.sendMessage(LanguageUtils.getMessage("no_permission"));
                return false;
            }

            if(args.length == 0) {
                LanguageUtils.getMessageList("Commands.MoneyAdmin.Usage").forEach(player::sendMessage);
            } else if(args.length == 2) {
                if(args[0].toLowerCase().contentEquals(LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Reset"))) {
                    handleReset(player, args[1]);
                } else if(args[0].toLowerCase().contentEquals(LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Transactions"))) {
                    handleTransactions(player, args[1]);
                }
            } else if(args.length == 3) {
                if(args[0].toLowerCase().contentEquals(LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Add"))) {
                    String user = args[1];

                    if(!NumberUtils.isNumeric(args[2])) {
                        player.sendMessage(LanguageUtils.getMessage("no_valid_number"));
                        return false;
                    }
                    double amount = Double.parseDouble(args[2]);
                    handleAdd(player, user, amount);
                } else if(args[0].toLowerCase().contentEquals(LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Remove"))) {
                    String user = args[1];

                    if(!NumberUtils.isNumeric(args[2])) {
                        player.sendMessage(LanguageUtils.getMessage("no_valid_number"));
                        return false;
                    }
                    double amount = Double.parseDouble(args[2]);
                    handleRemove(player, user, amount);
                } else if(args[0].toLowerCase().contentEquals(LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Set"))) {
                    String user = args[1];

                    if(!NumberUtils.isNumeric(args[2])) {
                        player.sendMessage(LanguageUtils.getMessage("no_valid_number"));
                        return false;
                    }
                    double amount = Double.parseDouble(args[2]);
                    handleSet(player, user, amount);
                }
            }

        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(sender instanceof Player player && player.hasPermission(LanguageUtils.getMessageString("Commands.MoneyAdmin.Permission"))) {
            if(args.length == 1) {
                return List.of(
                        LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Add"),
                        LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Remove"),
                        LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Set"),
                        LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Reset"),
                        LanguageUtils.getMessageString("Commands.MoneyAdmin.Arguments.Transactions")
                );
            } else if(args.length == 2) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
        }
        return super.tabComplete(sender, alias, args);
    }

    private void handleRemove(Player player, String user, double amount) {
        repository.findByFieldName("name", user).thenAccept(target -> {
            if(target == null) {
                player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                return;
            }

            if((target.getBalance() - amount) < 0) {
                player.sendMessage(LanguageUtils.getMessage("command_moneyadmin_remove_not_enough_money"));
                return;
            }

            target.removeBalance(amount);
            EconomyTransaction transaction = new EconomyTransaction();
            transaction.setAccountId(target.getUniqueId());
            transaction.setTime(System.currentTimeMillis());
            transaction.setAmount(amount);
            transaction.setType(TransactionType.ADMIN_REMOVE);
            transaction.setReceiverId(target.getUniqueId());
            transaction.setUser(target);
            target.addTransaction(transaction);
            repository.save(target);
            player.sendMessage(LanguageUtils.getMessage("command_moneyadmin_remove_success",
                    Placeholder.parsed("balance", String.valueOf(amount)),
                    Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name"))
            ));
        });
    }

    private void handleSet(Player player, String user, double amount) {
        repository.findByFieldName("name", user).thenAccept(target -> {
            if(target == null) {
                player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                return;
            }

            target.setBalance(amount);
            EconomyTransaction transaction = new EconomyTransaction();
            transaction.setAccountId(target.getUniqueId());
            transaction.setTime(System.currentTimeMillis());
            transaction.setAmount(amount);
            transaction.setType(TransactionType.ADMIN_SET);
            transaction.setReceiverId(target.getUniqueId());
            transaction.setUser(target);
            target.addTransaction(transaction);
            repository.save(target);
            player.sendMessage(LanguageUtils.getMessage("command_moneyadmin_set_success",
                    Placeholder.parsed("balance", String.valueOf(amount)),
                    Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name"))
            ));
        });
    }

    private void handleAdd(Player player, String user, double amount) {
        repository.findByFieldName("name", user).thenAccept(target -> {
            if(target == null) {
                player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                return;
            }

            target.addBalance(amount);
            EconomyTransaction transaction = new EconomyTransaction();
            transaction.setAccountId(target.getUniqueId());
            transaction.setTime(System.currentTimeMillis());
            transaction.setAmount(amount);
            transaction.setType(TransactionType.ADMIN_ADD);
            transaction.setReceiverId(target.getUniqueId());
            transaction.setUser(target);
            target.addTransaction(transaction);
            repository.save(target);

            player.sendMessage(LanguageUtils.getMessage("command_moneyadmin_add_success",
                    Placeholder.parsed("balance", String.valueOf(amount)),
                    Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name"))
            ));
        });
    }

    private void handleReset(Player player, String user) {
        repository.findByFieldName("name", user).thenAccept(target -> {
            if(target == null) {
                player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                return;
            }

            target.setBalance(0);

            EconomyTransaction transaction = new EconomyTransaction();
            transaction.setAccountId(target.getUniqueId());
            transaction.setTime(System.currentTimeMillis());
            transaction.setAmount(0);
            transaction.setType(TransactionType.ADMIN_RESET);
            transaction.setReceiverId(target.getUniqueId());
            transaction.setUser(target);
            target.addTransaction(transaction);
            repository.save(target);
            player.sendMessage(LanguageUtils.getMessage("command_moneyadmin_reset_success",
                    Placeholder.parsed("username", user)));
        });
    }

    private void handleTransactions(Player player, String user) {
        repository.findByFieldName("name", user).thenAccept(target -> {
            if(target == null) {
                player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                return;
            }

            new TransactionsGui(player, target.getUniqueId(), plugin);
        });
    }

}
