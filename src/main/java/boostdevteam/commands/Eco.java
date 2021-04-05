package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.vaultapi.misc.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Eco implements CommandExecutor {

    public Eco() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandlabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eco")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (args.length == 3) {
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    if (p != null) {
                        if (NumberUtils.isNumber(args[2])) {
                            Economy money = new Economy(p, Double.parseDouble(args[2]));
                            if (args[1].equalsIgnoreCase("set")) {
                                if (sender.hasPermission("boosteconomy.money.set") || sender.hasPermission("boosteconomy.*")) {

                                    money.setBalance();
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + args[2]));

                                    String senderName = sender instanceof ConsoleCommandSender ? "Console" : sender.getName();
                                    BoostEconomy.saveLog(senderName + " have changed the money of " + p.getName() + " to " + args[2] + "$");
                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                        BoostEconomy.playSuccessSound(p);
                                    }

                                } else {

                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                    }
                                }
                                return true;
                            } else if (args[1].equalsIgnoreCase("give")) {
                                if (sender.hasPermission("boosteconomy.money.give") || sender.hasPermission("boosteconomy.*")) {

                                    money.addBalance();
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + (money.getBalance() + Long.parseLong(args[2]))));

                                    String senderName = sender instanceof ConsoleCommandSender ? "Console" : sender.getName();
                                    BoostEconomy.saveLog(senderName + " gived " + args[(2)] + "$ to " + p.getName());
                                    if (sender instanceof Player) {
                                        BoostEconomy.playSuccessSound(p);
                                        BoostEconomy.playSuccessSound((Player) sender);
                                    }

                                } else {
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                    }
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("take")) {
                                if (sender.hasPermission("boosteconomy.money.take") || sender.hasPermission("boosteconomy.*")) {

                                    money.takeBalance();
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + Math.max(money.getBalance() - Long.valueOf(args[2]), 0)));

                                    String senderName = sender instanceof ConsoleCommandSender ? "Console" : sender.getName();
                                    BoostEconomy.saveLog(senderName + " removed " + args[(2)] + "$ from " + p.getName());

                                    if (sender instanceof Player) {
                                        BoostEconomy.playSuccessSound(p);
                                        BoostEconomy.playSuccessSound((Player) sender);
                                    }

                                }else {
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                                    if (sender instanceof Player) {
                                        Player player = (Player) sender;
                                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                    }
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));

                            if (sender instanceof Player) {
                                BoostEconomy.playErrorSound((Player) sender);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));

                        if (sender instanceof Player) {
                            BoostEconomy.playErrorSound((Player) sender);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));

                    if (sender instanceof Player) {
                        BoostEconomy.playErrorSound((Player) sender);
                    }
                }
            }
        }
        return true;
    }
}
