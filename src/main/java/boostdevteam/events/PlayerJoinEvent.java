package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player p = e.getPlayer();

        BoostEconomy.data.data.set("Data." + p.getName() + ".Last-IP", p.getAddress());

        if (BoostEconomy.getInstance().getConfig().getBoolean("Config.CheckForUpdates.Player")) {
            if (p.hasPermission("boosteconomy.checkforupdates") || p.hasPermission("boosteconomy.*")) {
                if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
                    new boostdevteam.boosteconomy.UpdateChecker(BoostEconomy.plugin, 86591).getVersion(version -> {
                        if (BoostEconomy.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                            p.sendMessage("§b§lBoostEconomy §8--> §aNo new version available!");
                        } else {
                            p.sendMessage("§b§lBoostEconomy §8--> §7New version available! §av" + version);
                            p.sendMessage("§b§lBoostEconomy §8--> §7You have §cv" + BoostEconomy.plugin.getDescription().getVersion());
                            p.sendMessage("§b§lBoostEconomy §8--> §eDownload it at https://www.spigotmc.org/resources/86591");
                        }
                    });
                }else {
                    p.sendMessage("§b§lBoostEconomy §8--> §7 You are using a server version not compatible with the updater! §c(Works with 1.12+)");
                }
            }
        }
    }
}
