package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaveData implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
            if (sender instanceof ConsoleCommandSender || sender instanceof Player) {
                if (sender.hasPermission("boosteconomy.save") || sender.hasPermission("boosteconomy.*")) {
                    if (args.length == 2) {
                        //Economy eco = new Economy(Bukkit.getPlayer(args[0]), Double.parseDouble(args[1]));
                        //eco.setBalance();
                        BoostEconomy.getInstance().getRDatabase().setTokens(args[0], Long.parseLong(args[1]));
                        sender.sendMessage("§b§lSave §8» §7Saved " + Long.valueOf(args[1]) + "$ for the player " + args[0]);
                        return true;
                    } else {
                        sender.sendMessage("§b§lSave §8» §7Not enough arguments! /save <player> <money>");
                    }

                } else {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        BoostEconomy.playErrorSound(p);
                    }
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    BoostEconomy.playSuccessSound(p);
                }
                return true;
            }
        return true;
    }
}
