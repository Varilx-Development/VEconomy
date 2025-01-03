package de.varilx.veconomy;

import de.varilx.BaseAPI;
import de.varilx.BaseSpigotAPI;
import de.varilx.command.registry.VaxCommandRegistry;
import de.varilx.database.Service;
import de.varilx.veconomy.commands.MoneyAdminCommand;
import de.varilx.veconomy.commands.MoneyCommand;
import de.varilx.veconomy.economy.CustomEconomy;
import de.varilx.veconomy.listener.ConnectionListener;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class VEconomy extends JavaPlugin {

    Service databaseService;

    @Override
    public void onEnable() {
        new BaseSpigotAPI(this, 24306).enable();
        initializeDatabaseService();
        registerListener();
        registerCommands();
        registerProvider();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        VaxCommandRegistry registry = new VaxCommandRegistry();
        registry.registerCommand(new MoneyCommand(this));
        registry.registerCommand(new MoneyAdminCommand(this));
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(this), this);
    }

    private void initializeDatabaseService() {
        databaseService = Service.load(Objects.requireNonNull(BaseAPI.get().getDatabaseConfiguration()), getClassLoader());
        databaseService.create(EconomyUser.class, UUID.class);
    }

    private void registerProvider() {
        getServer().getServicesManager().register(Economy.class, new CustomEconomy(this), this, ServicePriority.Highest);
    }

}
