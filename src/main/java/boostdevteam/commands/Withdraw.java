package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Withdraw implements CommandExecutor {

    /**
     * The plugin instance
     */
    private BoostEconomy plugin;

    /**
     * Creates the "/withdraw <amount>" command handler
     */
    public Withdraw(BoostEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoConsole").replaceAll("&", "§"));
        } else if (!sender.hasPermission("boosteconomy.banknotes.withdraw") || !sender.hasPermission("boosteconomy.*")) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
            BoostEconomy.playErrorSound((Player) sender);
        } else if (args.length == 0) {
            sender.sendMessage(BoostEconomy.getLanguage().getString("Banknotes.Messages.Invalid-Number").replaceAll("&", "§"));
            BoostEconomy.playErrorSound((Player) sender);
        } else {

            if (!plugin.getConfig().getBoolean("Banknotes.UseBanknotes")) return false;

            Player player = (Player) sender;
            long amount;

            try {
                Economy eco = new Economy(player, 0);
                amount = args[0].equalsIgnoreCase("all") ? eco.getBalance() : Long.parseLong(args[0]);
            } catch (NumberFormatException invalidNumber) {
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Number").replaceAll("&", "§"));
                BoostEconomy.playErrorSound(player);
                return true;
            }

            Economy eco = new Economy(player, amount);
            long min = plugin.getConfig().getLong("Banknotes.Minimum-Withdraw-Amount");
            long max = plugin.getConfig().getLong("Banknotes.Maximum-Withdraw-Amount");

            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Invalid-Number").replaceAll("&", "§"));
                BoostEconomy.playErrorSound(player);
            } else if (BoostEconomy.getInstance().getConfig().getBoolean("Banknotes.Enable-Maximum")) {
                if (amount > max) {
                    player.sendMessage(BoostEconomy.getLanguage().getString("Banknotes.Messages.More-Than-Maximum")
                            .replace("%max%", "" + max)
                    .replaceAll("&", "§"));

                    BoostEconomy.playErrorSound(player);
                }
            } else if (BoostEconomy.getInstance().getConfig().getBoolean("Banknotes.Enable-Minimum")) {
                if (amount < min) {
                    player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Less-Than-Minimum")
                            .replace("%min%", "" + min)
                    .replaceAll("&", "§"));

                    BoostEconomy.playErrorSound(player);
                }
            } else if (eco.getBalance() < amount) {
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Insufficient-Funds").replaceAll("&", "§"));
                BoostEconomy.playErrorSound(player);
            } else if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Inventory-Full").replaceAll("&", "§"));
                BoostEconomy.playErrorSound(player);
            } else {
                ItemStack banknote = plugin.createBanknote(player.getName(), amount);

                double x = eco.getBalance();
                double y = amount;
                double res = x - y;
                Economy money = new Economy(player, res);
                money.setBalance();

                player.getInventory().addItem(banknote);
                player.sendMessage(BoostEconomy.getLanguage().getString("Messages.Banknotes.Note-Created")
                        .replace("%money%", "" + amount)
                        .replaceAll("&", "§"));
                BoostEconomy.playSuccessSound(player);

                String senderName = sender instanceof ConsoleCommandSender ? "Console" : sender.getName();
                BoostEconomy.saveLog(senderName + " have withdrawn a banknote of " + amount + "$");
            }
        }

        return true;
    }
}
