package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginListener implements Listener{
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        Data data = BoostEconomy.getData();

        if (!data.hasBalance(p)) {
            data.saveData(p, BoostEconomy.getInstance().getConfig().getDouble("Config.StartMoney"));
            if (BoostEconomy.getInstance().getConfig().getBoolean("Config.ConsoleSaveMessage")) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Saving data for §3" + e.getPlayer().getName());
            }

        }
    }
}