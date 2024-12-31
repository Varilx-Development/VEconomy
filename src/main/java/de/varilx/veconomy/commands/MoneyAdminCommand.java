package de.varilx.veconomy.commands;


import de.varilx.command.VaxCommand;
import de.varilx.database.repository.Repository;
import de.varilx.utils.language.LanguageUtils;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
        }

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }
}
