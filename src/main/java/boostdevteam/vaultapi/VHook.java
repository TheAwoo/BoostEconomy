package boostdevteam.vaultapi;

import boostdevteam.boosteconomy.BoostEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VHook {

    public BoostEconomy plugin = BoostEconomy.getInstance();
    public Economy veco;

    public void onHook() {
        veco = new VEconomy(plugin);
        plugin.getServer().getServicesManager().register(Economy.class, BoostEconomy.veco, plugin, ServicePriority.Highest);
    }

    public void offHook() {
        plugin.getServer().getServicesManager().unregister(Economy.class, BoostEconomy.veco);
    }
}
