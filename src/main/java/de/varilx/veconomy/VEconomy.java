package de.varilx.veconomy;

import de.varilx.BaseAPI;
import de.varilx.BaseSpigotAPI;
import de.varilx.command.registry.VaxCommandRegistry;
import de.varilx.database.Service;
import de.varilx.veconomy.commands.MoneyAdminCommand;
import de.varilx.veconomy.commands.MoneyCommand;
import de.varilx.veconomy.commands.PayCommand;
import de.varilx.veconomy.economy.CustomEconomy;
import de.varilx.veconomy.listener.ConnectionListener;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class VEconomy extends JavaPlugin {

    Service databaseService;
    Economy economy;


    @Override
    public void onEnable() {
        Thread.currentThread().setContextClassLoader(getClassLoader());
        new BaseSpigotAPI(this, -1).enable();
        initializeDatabaseService();
        registerListener();
        registerCommands();
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerProvider();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        VaxCommandRegistry registry = new VaxCommandRegistry();
        registry.registerCommand(new MoneyCommand(this));
        registry.registerCommand(new MoneyAdminCommand(this));
        registry.registerCommand(new PayCommand(this));
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
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        getLogger().info("Economy provider registered: " + economy.getName());
    }

    private boolean setupEconomy() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }
}