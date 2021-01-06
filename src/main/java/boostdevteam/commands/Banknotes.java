package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Banknotes implements CommandExecutor {

    /*
     * The plugin instance
     */
    private BoostEconomy plugin;

    /**
     * Creates the "/deposit" command handler
     */
    public Banknotes(BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            if (!sender.hasPermission("boosteconomy.banknotes.give")) {
                sender.sendMessage(plugin.getMessage("Messages.General.NoPerms"));
                Player player = (Player) sender;
                BoostEconomy.playErrorSound(player);
            } else {
                // give player amount
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(plugin.getMessage("Banknotes.Messages.Player-Not-Found"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                    return true;
                }

                double amount;
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(plugin.getMessage("Banknotes.Messages.Invalid-Number"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                    return true;
                }

                if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                    sender.sendMessage(plugin.getMessage("Banknotes.Messages.Invalid-Number"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                } else {
                    ItemStack banknote = plugin.createBanknote(sender.getName(), amount);
                    target.getInventory().addItem(banknote);

                    //Use console-name if the note is given by a console command
                    String senderName = sender instanceof ConsoleCommandSender ? plugin.getMessage("Banknotes.Console-Name") : sender.getName();
                    target.sendMessage(plugin.getMessage("Banknotes.Messages.Note-Received")
                            .replace("%money%", "" + amount)
                            .replace("%player%", senderName));
                    sender.sendMessage(plugin.getMessage("Banknotes.Messages.Note-Given")
                            .replace("%money%", "" + amount)
                            .replace("%player%", target.getName()));

                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playSuccessSound(player);
                    }
                }
            }
            return true;
        }
        return false;
    }
}