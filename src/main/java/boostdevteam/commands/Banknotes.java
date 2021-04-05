package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Banknotes implements CommandExecutor {

    /*
     * The plugin instance
     */
    private final BoostEconomy plugin;

    /**
     * Creates the "/deposit" command handler
     */
    public Banknotes(BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if (!plugin.getConfig().getBoolean("Banknotes.UseBanknotes")) {
            sender.sendMessage("§b§lBanknotes §8» §7The banknotes are disabled!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.InvalidArgs").replaceAll("&", "§"));
            if (sender instanceof Player) {
                Player p = (Player) sender;
                BoostEconomy.playErrorSound(p);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            if (sender.hasPermission("boosteconomy.banknotes.give") || sender.hasPermission("boosteconomy.*")) {
                // give player amount
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Player-Not-Found").replaceAll("&", "§"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                    return true;
                }

                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Number").replaceAll("&", "§"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                    return true;
                }

                if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Number").replaceAll("&", "§"));
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playErrorSound(player);
                    }
                } else {
                    ItemStack banknote = plugin.createBanknote(sender.getName(), amount);
                    target.getInventory().addItem(banknote);

                    //Use console-name if the note is given by a console command
                    String senderName = sender instanceof ConsoleCommandSender ? plugin.getMessage("Banknotes.Console-Name") : sender.getName();
                    target.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Received")
                            .replaceAll("%money%", "" + amount)
                            .replaceAll("%player%", senderName)
                            .replaceAll("&", "§"));
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Given")
                            .replaceAll("%money%", "" + amount)
                            .replaceAll("%player%", target.getName())
                            .replaceAll("&", "§"));

                    BoostEconomy.saveLog(senderName + " gave to " + target.getName() + " a banknote of " + amount + "$");

                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BoostEconomy.playSuccessSound(player);
                    }
                }
            } else {
                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                Player player = (Player) sender;
                BoostEconomy.playErrorSound(player);
            }
            return true;
        } else {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.InvalidArgs").replaceAll("&", "§"));
            if (sender instanceof Player) {
                Player p = (Player) sender;
                BoostEconomy.playErrorSound(p);
            }
        }

        return false;
    }
}