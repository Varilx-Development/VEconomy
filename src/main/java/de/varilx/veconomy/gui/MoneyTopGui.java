package de.varilx.veconomy.gui;


import de.varilx.BaseAPI;
import de.varilx.database.repository.Repository;
import de.varilx.inventory.GameInventory;
import de.varilx.inventory.builder.GameInventoryBuilder;
import de.varilx.inventory.item.ClickableItem;
import de.varilx.utils.MathUtils;
import de.varilx.utils.itembuilder.ItemBuilder;
import de.varilx.utils.itembuilder.SkullBuilder;
import de.varilx.utils.language.LanguageUtils;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.gui
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MoneyTopGui {

    VEconomy plugin;
    Player holder;
    Repository<EconomyUser, UUID> repository;

    static String[] PATTERN = new String[] {
            "YYYYYYYYY",
            "YXXXXXXXY",
            "YX12345XY",
            "YX67890XY",
            "YXXXXXXXY",
            "YYYYYYYYY"
    };


    public MoneyTopGui(VEconomy plugin, Player holder) {
        this.plugin = plugin;
        this.holder = holder;
        this.repository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
        openGui();
    }

    private void openGui() {
        GameInventory gameInventory = new GameInventoryBuilder(BaseAPI.get())
                .pattern(PATTERN)
                .inventoryName(LanguageUtils.getMessage("Gui.Top.Title"))
                .size(6 * 9)
                .addItem('Y', new ClickableItem(new ItemBuilder(Material.valueOf(LanguageUtils.getMessageString("Gui.Top.Items.Placeholders.1")))
                        .name(Component.empty()).build()) {
                    @Override
                    public void handleClick(InventoryClickEvent inventoryClickEvent) {}
                })
                .addItem('X', new ClickableItem(new ItemBuilder(Material.valueOf(LanguageUtils.getMessageString("Gui.Top.Items.Placeholders.2")))
                        .name(Component.empty()).build()) {
                    @Override
                    public void handleClick(InventoryClickEvent inventoryClickEvent) {}
                })
                .holder(holder)
                .build();


        repository.sortAll("balance", false, 10).thenAccept(users -> {
           int count = 0;
            for (EconomyUser user : users) {
                count++;
                char countChar = ' ';
                if(count < 10) {
                    countChar = (char) ('0' + count);
                } else {
                    countChar = '0';
                }

                gameInventory.addItem(countChar, new ClickableItem(new SkullBuilder()
                        .name(LanguageUtils.getMessage("top_item_name",
                                Placeholder.parsed("currency_name", LanguageUtils.getMessageString("currency_name")),
                                Placeholder.parsed("count", String.valueOf(count)),
                                Placeholder.parsed("username", user.getName()),
                                Placeholder.parsed("balance", MathUtils.formatNumber(user.getBalance()))))
                        .playerProfile(Bukkit.getOfflinePlayer(user.getUniqueId()).getPlayerProfile())
                        .build()) {
                    @Override
                    public void handleClick(InventoryClickEvent inventoryClickEvent) {

                    }
                });
            }
            plugin.getServer().getScheduler().runTask(plugin, gameInventory::open);
            holder.playSound(holder, Sound.BLOCK_CHEST_OPEN, 1, 1.5f);
        });
    }

}
