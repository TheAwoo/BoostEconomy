package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (BoostEconomy.getInstance().getConfig().getBoolean("Config.CheckForUpdates.Player")) {
            if (e.getPlayer().hasPermission("boosteconomy.checkforupdates")) {
                new UpdateChecker(BoostEconomy.plugin, 86591).getVersion(version -> {
                    if (BoostEconomy.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                        p.sendMessage("§b§lBoostEconomy §8--> §aNo new version available!");
                    } else {
                        p.sendMessage("§b§lBoostEconomy §8--> §7New version available! §av" + version);
                        p.sendMessage("§b§lBoostEconomy §8--> §7You have §cv" + BoostEconomy.plugin.getDescription().getVersion());
                        p.sendMessage("§b§lBoostEconomy §8--> §eDownload it at https://www.spigotmc.org/resources/86591");
                    }
                });
            }
        }
    }
}
