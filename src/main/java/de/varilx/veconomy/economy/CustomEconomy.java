package de.varilx.veconomy.economy;


import de.varilx.database.repository.Repository;
import de.varilx.utils.language.LanguageUtils;
import de.varilx.veconomy.VEconomy;
import de.varilx.veconomy.user.EconomyUser;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomEconomy implements Economy {

    VEconomy plugin;
    Repository<EconomyUser, UUID> repository;

    public CustomEconomy(VEconomy plugin) {
        this.plugin = plugin;
        this.repository = (Repository<EconomyUser, UUID>) plugin.getDatabaseService().getRepository(EconomyUser.class);
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "VEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double v) {
        return String.format("%.2f", v);
    }

    @Override
    public String currencyNamePlural() {
        return LanguageUtils.getMessageString("currency_name_plural");
    }

    @Override
    public String currencyNameSingular() {
        return LanguageUtils.getMessageString("currency_name_singular");
    }

    @SneakyThrows
    @Override
    public boolean hasAccount(String s) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user != null;
    }

    @SneakyThrows
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        return user != null;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return true;
    }

    @SneakyThrows
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user != null;
    }

    @SneakyThrows
    @Override
    public double getBalance(String s) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user.getBalance();
    }

    @SneakyThrows
    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        return user.getBalance();
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @SneakyThrows
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user.getBalance();
    }

    @SneakyThrows
    @Override
    public boolean has(String s, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user.getBalance() >= v;
    }

    @SneakyThrows
    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        return user.getBalance() >= v;
    }

    @SneakyThrows
    @Override
    public boolean has(String s, String s1, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user.getBalance() >= v;
    }

    @SneakyThrows
    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        return user.getBalance() >= v;
    }

    @SneakyThrows
    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        if(user.getBalance() - v < 0)
            return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        if(user.getBalance() - v < 0)
            return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        EconomyUser user = repository.findByFieldName("name", s).get();
        if(user.getBalance() - v < 0)
            return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @SneakyThrows
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        EconomyUser user = repository.findFirstById(offlinePlayer.getUniqueId()).get();
        if(user.getBalance() - v < 0)
            return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.FAILURE, null);
        user.removeBalance(v);
        repository.save(user);
        return new EconomyResponse(v, user.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
