package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKillEvent implements Listener {

    private BoostEconomy plugin;
    public MobKillEvent (BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKill (final EntityDeathEvent event) {
        if (plugin.getConfig().getBoolean("Entity.Enable")) {

            final Player killer = event.getEntity().getKiller();
            if (killer == null) {
                return;
            }

            final LivingEntity victim = event.getEntity();
            String section = victim.getType().toString();

            if (!(killer.hasPermission("boosteconomy.earn") || killer.hasPermission("boosteconomy.*"))) {
                return;
            }

            if (!(BoostEconomy.mob.mobData.getBoolean("Mobs." + section + ".Enabled"))) {
                return;
            }

            Economy eco = new Economy(killer, BoostEconomy.mob.mobData.getDouble("Mobs." + section + ".Reward"));
            eco.addBalance();

            // Chat message

            if (BoostEconomy.getInstance().getConfig().getBoolean("Entity.Message")) {
                killer.sendMessage(BoostEconomy.getLanguage().getString("Messages.KillMessage").replaceAll("&", "§")
                        .replaceAll("%mob%", "" + event.getEntityType())
                        .replaceAll("%money%", "" + BoostEconomy.mob.mobData.getDouble("Mobs." + section + ".Reward"))
                .replaceAll("&", "§"));
            }

            // ActionBar Message

            if(BoostEconomy.getInstance().getConfig().getBoolean("Entity.ActionBar")) {
                String message = BoostEconomy.getLanguage().getString("Messages.KillMessage").replaceAll("&", "§")
                        .replaceAll("%mob%", "" + event.getEntityType())
                        .replaceAll("%money%", "" + BoostEconomy.mob.mobData.getDouble("Mobs." + section + ".Reward"));

                killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            }
        }
    }
}
