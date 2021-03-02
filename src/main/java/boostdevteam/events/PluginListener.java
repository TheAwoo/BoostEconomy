package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListener implements Listener{
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        Data data = BoostEconomy.getData();

        if (!data.hasBalance(p)) {
            data.saveData(p, BoostEconomy.getInstance().getConfig().getLong("Config.StartMoney"));
            if (BoostEconomy.getInstance().getConfig().getBoolean("Config.ConsoleSaveMessage")) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Saving data for §c" + e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        Data data = BoostEconomy.getData();

        Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Saving data for §c" + p.getName() + "§a (" + data.getValue(p) + "$)");

        try {
            data.saveData(p, data.getValue(p));
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Could not save data for §c" + p.getName() + " §7because the data.yml does not contains the player data!");
        }
    }
}