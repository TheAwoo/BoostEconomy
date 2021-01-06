package boostdevteam.events;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
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

    /**
     * Creates the note listener
     */
    public Banknotes(BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerClaimNote(PlayerInteractEvent event) {
        // Check if we need to use /deposit or if we can right click
        if (!plugin.getConfig().getBoolean("Banknotes.Allow-Right-Click-To-Deposit-Notes", true)) {
            return;
        }
        if (!(BoostEconomy.getInstance().isLegacy())) {
            // Check the action
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            // Check if the player is allowed to deposit bank notes
            if (!event.getPlayer().hasPermission("boosteconomy.banknotes.deposit")) {
                return;
            }

            Player player = event.getPlayer();
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            // Verify that this is a real banknote
            if (item == null || !plugin.isBanknote(item)) {
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

            // Deposit the money
            player.sendMessage(plugin.getMessage("Banknotes.Messages.Note-Redeemed").replace("%money%", plugin.formatDouble(amount)));
            BoostEconomy.playSuccessSound(player);

            // Remove the slip
            if (item.getAmount() <= 1) {
                event.getPlayer().getInventory().removeItem(item);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        } else {
            // Check the action
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            // Check if the player is allowed to deposit bank notes
            if (!event.getPlayer().hasPermission("boosteconomy.banknotes.deposit")) {
                return;
            }

            Player player = event.getPlayer();
            ItemStack item = event.getPlayer().getInventory().getItemInHand();

            // Verify that this is a real banknote
            if (item == null || !plugin.isBanknote(item)) {
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

            // Deposit the money
            player.sendMessage(plugin.getMessage("Banknotes.Messages.Note-Redeemed").replace("%money%", plugin.formatDouble(amount)));
            BoostEconomy.playSuccessSound(player);

            // Remove the slip
            if (item.getAmount() <= 1) {
                event.getPlayer().getInventory().removeItem(item);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
}
