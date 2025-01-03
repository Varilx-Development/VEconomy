package de.varilx.veconomy.listener;


import de.varilx.BaseAPI;
import de.varilx.database.repository.Repository;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Project: VEconomy
 * Package: de.varilx.veconomy.listener
 * <p>
 * Author: ShadowDev1929
 * Created on: 31.12.2024
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConnectionListener implements Listener {

    VEconomy plugin;
    Repository<EconomyUser, UUID> repository;

    public ConnectionListener(VEconomy plugin) {
        this.plugin = plugin;
        this.repository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        repository.exists(player.getUniqueId()).thenAccept(exists -> {
           if(exists) {
               repository.findFirstById(player.getUniqueId()).thenAccept(user -> {
                  if(!user.getName().contentEquals(player.getName())) {
                      user.setName(player.getName());
                      repository.save(user);
                  }
               });
               return;
           }

           EconomyUser user = new EconomyUser();
           user.setBalance(BaseAPI.get().getConfiguration().getInt("start_balance"));
           user.setName(player.getName());
           user.setUniqueId(player.getUniqueId());
           user.setTransactions(new ArrayList<>());

           repository.insert(user);
        });
    }

}
