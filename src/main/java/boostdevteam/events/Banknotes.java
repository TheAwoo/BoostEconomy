package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Banknotes implements Listener {

    /*
     * The plugin instance
     */
    private BoostEconomy plugin;

    public Banknotes(BoostEconomy plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerClaimNote(PlayerInteractEvent event) {
        if (!(plugin.getConfig().getBoolean("Banknotes.UseBanknotes", true))) {
            return;
        }
        // Check if we need to use /deposit or if we can right click
        if (!plugin.getConfig().getBoolean("Banknotes.Allow-Right-Click-To-Deposit-Notes", true)) {
            return;
        }

        if (!(BoostEconomy.getInstance().isLegacy())) {
            try {
                // Check the action
                if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                    return;
                }

                // Check if the player is allowed to deposit bank notes
                if (!event.getPlayer().hasPermission("boosteconomy.banknotes.deposit") || !event.getPlayer().hasPermission("boosteconomy.*")) {
                    return;
                }

                Player player = event.getPlayer();
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

                // Verify that this is a real banknote
                if (item != null && plugin.isBanknote(item)) {
                } else {
                    return;
                }

                long amount = 0;
                amount = (long) plugin.getBanknoteAmount(item);

                // Negative banknotes are not allowed
                if (Double.compare(amount, 0) < 0) {
                    return;
                }

                // Double check the response
                Economy eco = new Economy(player, amount);
                double x = eco.getBalance();
                double y = amount;
                double res = x + y;
                Economy money = new Economy(player, res);
                money.setBalance();

                // Deposit the money
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Redeemed")
                        .replace("%money%", "" + amount)
                .replaceAll("&", "§"));
                BoostEconomy.playSuccessSound(player);

                BoostEconomy.saveLog(player.getName() + " redeemed a note of " + amount + "$");

                // Remove the slip
                if (item.getAmount() <= 1) {
                    event.getPlayer().getInventory().removeItem(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }

                if(event.hasBlock()) {
                    event.setCancelled(true);
                }

            } catch (Exception e) {
                event.getPlayer().sendMessage("§b§lBoostEconomy §8--> §cError on claiming the note!");
                event.getPlayer().sendMessage("§b§lBoostEconomy §8--> §cReport it to an administrator!");
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §c" + event.getPlayer().getName() + " failed to claim the note!");
                BoostEconomy.saveLog(event.getPlayer().getName() + " failed to claim the note!");
                e.printStackTrace();
                BoostEconomy.playErrorSound(event.getPlayer());
            }
        } else {
            try {
                // Check the action
                if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                    return;
                }

                // Check if the player is allowed to deposit bank notes
                if (!event.getPlayer().hasPermission("boosteconomy.banknotes.deposit") || !event.getPlayer().hasPermission("boosteconomy.*")) {
                    return;
                }

                Player player = event.getPlayer();
                ItemStack item = event.getPlayer().getInventory().getItemInHand();

                // Verify that this is a real banknote
                if (item != null && plugin.isBanknote(item)) {
                } else {
                    return;
                }

                double amount = 0;
                amount = plugin.getBanknoteAmount(item);

                // Negative banknotes are not allowed
                if (Double.compare(amount, 0) < 0) {
                    return;
                }

                // Double check the response
                Economy eco = new Economy(player, amount);
                double x = eco.getBalance();
                double y = amount;
                double res = x + y;
                Economy money = new Economy(player, res);
                money.setBalance();

                BoostEconomy.saveLog(player.getName() + " redeemed a note of " + amount + "$");

                // Deposit the money
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Redeemed")
                        .replace("%money%", "" + amount)
                        .replaceAll("&", "§"));
                BoostEconomy.playSuccessSound(player);

                // Remove the slip
                if (item.getAmount() <= 1) {
                    event.getPlayer().getInventory().removeItem(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }

                if(event.hasBlock()) {
                    event.setCancelled(true);
                }

            } catch (Exception e) {
                event.getPlayer().sendMessage("§b§lBoostEconomy §8--> §cError on claiming the note! Report it to an admin!");
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §c" + event.getPlayer().getName() + " failed to claim the note!");
                e.printStackTrace();
                BoostEconomy.playErrorSound(event.getPlayer());
            }
        }
    }
}
