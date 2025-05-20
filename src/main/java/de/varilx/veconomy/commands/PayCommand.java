package de.varilx.veconomy.commands;

import de.varilx.command.VaxCommand;
import de.varilx.database.repository.Repository;
import de.varilx.utils.MathUtils;
import de.varilx.utils.NumberUtils;
import de.varilx.utils.language.LanguageUtils;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.transaction.EconomyTransaction;
import de.varilx.veconomy.transaction.type.TransactionType;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayCommand extends VaxCommand {

    VEconomy plugin;
    Repository<EconomyUser, UUID> repository;

    public PayCommand(VEconomy plugin) {
        super(LanguageUtils.getMessageString("Commands.Pay.Name"));
        this.plugin = plugin;
        this.repository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if(args.length == 2) {
                String user = args[0];

                if(!NumberUtils.isNumeric(args[1])) {
                    player.sendMessage(LanguageUtils.getMessage("no_valid_number"));
                    return false;
                }

                double amount = Double.parseDouble(args[1]);

                repository.findByFieldName("name", user).thenAccept(target -> {
                    if(target == null) {
                        player.sendMessage(LanguageUtils.getMessage("user_not_found"));
                        return;
                    }
                    if(player.getUniqueId().equals(target.getUniqueId())) {
                        player.sendMessage(LanguageUtils.getMessage("command_money_pay_self"));
                        return;
                    }

                    repository.findFirstById(player.getUniqueId()).thenAccept(senderUser -> {
                        if(senderUser.getBalance() < amount) {
                            player.sendMessage(LanguageUtils.getMessage("command_money_pay_not_enough_money"));
                            return;
                        }

                        senderUser.removeBalance(amount);
                        target.addBalance(amount);

                        EconomyTransaction senderTransaction = new EconomyTransaction();
                        senderTransaction.setAccountId(senderUser.getUniqueId());
                        senderTransaction.setTime(System.currentTimeMillis());
                        senderTransaction.setAmount(amount);
                        senderTransaction.setType(TransactionType.PAY);
                        senderTransaction.setReceiverId(target.getUniqueId());
                        senderTransaction.setUser(senderUser);

                        senderUser.addTransaction(senderTransaction);
                        repository.save(senderUser);

                        EconomyTransaction receiverTransaction = new EconomyTransaction();
                        receiverTransaction.setAccountId(target.getUniqueId());
                        receiverTransaction.setTime(System.currentTimeMillis());
                        receiverTransaction.setAmount(amount);
                        receiverTransaction.setType(TransactionType.PAY_RECEIVE);
                        receiverTransaction.setReceiverId(senderUser.getUniqueId());
                        receiverTransaction.setUser(target);

                        target.addTransaction(receiverTransaction);

                        repository.save(target);

                        player.sendMessage(LanguageUtils.getMessage("command_money_pay_success",
                                Placeholder.parsed("receiver", target.getName()),
                                Placeholder.parsed("amount", MathUtils.formatNumber(amount)),
                                Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name"))
                        ));

                        Player receiver = plugin.getServer().getPlayer(target.getUniqueId());
                        if(receiver == null) return;

                        receiver.sendMessage(LanguageUtils.getMessage("command_money_pay_success_receiver",
                                Placeholder.parsed("sender", senderUser.getName()),
                                Placeholder.parsed("amount", MathUtils.formatNumber(amount)),
                                Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name"))
                        ));

                    });

                });
                return true;
            } else {
                player.sendMessage(LanguageUtils.getMessage("command_money_usage_pay"));
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(args.length == 1 && sender instanceof Player) {
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return super.tabComplete(sender, alias, args);
    }
}
