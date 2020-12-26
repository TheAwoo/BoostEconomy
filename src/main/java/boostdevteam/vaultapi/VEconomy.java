package boostdevteam.vaultapi;

import boostdevteam.boosteconomy.BoostEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VEconomy implements Economy {

    private BoostEconomy plugin;
    private final String name = "BoostEconomy";

    public VEconomy (BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String p) {
        if (BoostEconomy.getData().hasBalance(Bukkit.getPlayer(p))) {
            return Double.parseDouble(BoostEconomy.getData().getValue(Bukkit.getPlayer(p)));
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer arg0) {
        return getBalance(arg0.getName());
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        return getBalance(arg0);
    }

    @Override
    public double getBalance(OfflinePlayer arg0, String arg1) {
        return getBalance(arg0.getName());
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        if(getBalance(arg0)-arg1 >= 0 ) {
            BoostEconomy.getData().saveData(Bukkit.getPlayer(arg0), getBalance(arg0) - arg1);
        } else {
            BoostEconomy.getData().saveData(Bukkit.getPlayer(arg0), 0);
        }
        return new EconomyResponse(arg1, getBalance(arg0), EconomyResponse.ResponseType.SUCCESS, "bank not yet supported");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
        return withdrawPlayer(arg0.getName(),arg1);
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        return withdrawPlayer(arg0, arg2);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        return withdrawPlayer(arg0.getName(), arg2);
    }

    @Override
    public EconomyResponse depositPlayer(String p, double money) {
        BoostEconomy.getData().saveData(Bukkit.getPlayer(p), money + getBalance(p));
        return new EconomyResponse(money, getBalance(p), EconomyResponse.ResponseType.SUCCESS, "bank not yet supported");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
        return depositPlayer(arg0.getName(), arg1);
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        return depositPlayer(arg0, arg2);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        return depositPlayer(arg0.getName(), arg2);
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
        return null;
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