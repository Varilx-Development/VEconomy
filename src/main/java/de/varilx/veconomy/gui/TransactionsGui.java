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
import de.varilx.veconomy.transaction.EconomyTransaction;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.gui
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionsGui {

    Repository<EconomyUser, UUID> userRepository;
    Player holder;
    UUID target;
    VEconomy plugin;
    DateFormat dateFormat;

    static String[] PATTERN = new String[] {
            "XXXXXXXXX",
            "XTTTTTTTX",
            "XTTTTTTTX",
            "XTTTTTTTX",
            "XTTTTTTTX",
            "BXXXXXXXN"
    };


    public TransactionsGui(Player holder, UUID target, VEconomy plugin) {
        this.holder = holder;
        this.plugin = plugin;
        this.target = target;
        this.dateFormat = new SimpleDateFormat(LanguageUtils.getMessageString("date_format"));
        this.userRepository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
        openGui();
    }

    private void openGui() {
        userRepository.findFirstById(target).thenAccept(user -> {
            List<EconomyTransaction> transactions = user.getTransactions();
            transactions.sort(Comparator.comparingLong(EconomyTransaction::getTime).reversed());

            List<ClickableItem> paginationItems = new ArrayList<>();

            transactions.forEach(transaction -> {
                paginationItems.add(transactionItem(transaction));
            });

            GameInventory inventory = new GameInventoryBuilder(BaseAPI.get())
                    .inventoryName(LanguageUtils.getMessage("Gui.Transactions.Title"))
                    .holder(holder)
                    .pattern(PATTERN)
                    .size(6 * 9)
                    .addItem('X', new ClickableItem(new ItemBuilder(Material.valueOf(LanguageUtils.getMessageString("Gui.Transactions.Items.Placeholders.1.Material"))).name(Component.empty()).build()) {
                        @Override
                        public void handleClick(InventoryClickEvent inventoryClickEvent) {}
                    })
                    .enablePagination('T', 28, paginationItems)
                    .build();

            inventory.addItem('B', new ClickableItem(new SkullBuilder()
                    .setSkullTextureFromUrl(LanguageUtils.getMessageString("back_skull"))
                    .name(LanguageUtils.getMessage("Gui.Transactions.Items.BackItem.Name"))
                    .lore(LanguageUtils.getMessageList("Gui.Transactions.Items.Back.Lore"))
                    .build()) {
                @Override
                public void handleClick(InventoryClickEvent inventoryClickEvent) {
                    inventory.previousPage();
                }
            });
            inventory.addItem('N', new ClickableItem(new SkullBuilder()
                    .setSkullTextureFromUrl(LanguageUtils.getMessageString("next_skull"))
                    .name(LanguageUtils.getMessage("Gui.Transactions.Items.NextItem.Name"))
                    .lore(LanguageUtils.getMessageList("Gui.Transactions.Items.NextItem.Lore"))
                    .build()) {
                @Override
                public void handleClick(InventoryClickEvent inventoryClickEvent) {
                    inventory.nextPage();
                }
            });
            plugin.getServer().getScheduler().runTask(plugin, inventory::open);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private ClickableItem transactionItem(EconomyTransaction transaction) {

        String skinUrl = "";

        switch (transaction.getType()) {
            case ADD, ADMIN_ADD, ADMIN_SET, PAY_RECEIVE -> {
                skinUrl = LanguageUtils.getMessageString("transaction_item_add_skull");
            }
            case REMOVE, ADMIN_REMOVE, PAY ->  {
                skinUrl = LanguageUtils.getMessageString("transaction_item_remove_skull");
            }
            case ADMIN_RESET -> {
                skinUrl = LanguageUtils.getMessageString("admin_reset_skull");
            }
        }

        return new ClickableItem(new SkullBuilder()
                .setSkullTextureFromUrl(skinUrl)
                .name(LanguageUtils.getMessage("Gui.Transactions.Items.TransactionItem.Name", Placeholder.parsed("date", dateFormat.format(transaction.getTime()))))
                .lore(LanguageUtils.getMessageList("Gui.Transactions.Items.TransactionItem.Lore",
                        Placeholder.parsed("amount", MathUtils.formatNumber(transaction.getAmount())),
                        Placeholder.parsed("balance", MathUtils.formatNumber(transaction.getBalance())),
                        Placeholder.parsed("type", transaction.getType().getDisplayName()),
                        Placeholder.parsed("username", transaction.getUser().getName())
                ))
                .build()) {
            @Override
            public void handleClick(InventoryClickEvent inventoryClickEvent) {

            }
        };
    }

}
