package boostdevteam.vaultapi;

import boostdevteam.boosteconomy.BoostEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VHook {

    private BoostEconomy plugin = BoostEconomy.getInstance();
    private Economy veco;

    public void onHook() {
        veco = BoostEconomy.veco;
        plugin.getServer().getServicesManager().register(Economy.class, veco, plugin, ServicePriority.Normal);
    }

    public void offHook() {
        plugin.getServer().getServicesManager().unregister(Economy.class, BoostEconomy.veco);
    }

}
