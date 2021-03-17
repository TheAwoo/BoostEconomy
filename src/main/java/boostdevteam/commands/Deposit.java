package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Deposit implements CommandExecutor {

    /*
     * The plugin instance
     */
    private BoostEconomy plugin;

    /**
     * Creates the "/deposit" command handler
     */
    public Deposit(BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoConsole").replaceAll("&", "§"));
        } else if (!sender.hasPermission("boosteconomy.banknotes.deposit") || !sender.hasPermission("boosteconomy.*")) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
            BoostEconomy.playErrorSound((Player) sender);
        } else {
            if (!(BoostEconomy.getInstance().isLegacy())) {
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();

                if (item != null && plugin.isBanknote(item)) {
                    double amount = plugin.getBanknoteAmount(item);

                    if (amount >= 0) {
                        // Double check the response
                        Economy eco = new Economy(player, amount);
                        double x = eco.getBalance();
                        double y = amount;
                        double res = x + y;
                        Economy money = new Economy(player, res);
                        money.setBalance();

                        BoostEconomy.saveLog(player.getName() + " redeemed a note of " + amount + "$");
                        player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Redeemed")
                                .replace("%money%", "" + amount)
                                .replaceAll("&", "§")
                        );
                    } else {
                        player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Note").replaceAll("&", "§"));
                        BoostEconomy.playErrorSound((Player) sender);
                    }

                    // Remove the slip
                    if (item.getAmount() <= 1) {
                        player.getInventory().removeItem(item);
                    } else {
                        item.setAmount(item.getAmount() - 1);
                    }

                    BoostEconomy.playSuccessSound((Player) sender);

                } else {
                    player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Nothing-In-Hand").replaceAll("&", "§"));
                    BoostEconomy.playErrorSound((Player) sender);
                }
            } else {
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInHand();

                if (item != null && plugin.isBanknote(item)) {
                    double amount = plugin.getBanknoteAmount(item);

                    if (amount >= 0) {
                        // Double check the response
                        Economy eco = new Economy(player, amount);
                        double x = eco.getBalance();
                        double y = amount;
                        double res = x + y;
                        Economy money = new Economy(player, res);
                        money.setBalance();

                        player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Redeemed").replace("%money%", "" + amount)
                                .replaceAll("&", "§"));
                    } else {
                        player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Note").replaceAll("&", "§"));
                        BoostEconomy.playErrorSound((Player) sender);
                    }

                    // Remove the slip
                    if (item.getAmount() <= 1) {
                        player.getInventory().removeItem(item);
                    } else {
                        item.setAmount(item.getAmount() - 1);
                    }

                    BoostEconomy.playSuccessSound((Player) sender);

                } else {
                    player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Nothing-In-Hand").replaceAll("&", "§"));
                    BoostEconomy.playErrorSound((Player) sender);
                }
            }
        }
        return true;
    }
}
