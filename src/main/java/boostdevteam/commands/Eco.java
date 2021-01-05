package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Eco implements CommandExecutor {

    public Eco() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eco")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (args.length == 3) {
                    Player p = Bukkit.getServer().getPlayer(args[0]);
                    if (p != null) {
                        if (NumberUtils.isNumber(args[2])) {
                            Economy money = new Economy(p, Double.parseDouble(args[2]));
                            if (args[1].equalsIgnoreCase("set")) {
                                if (sender.hasPermission("boosteconomy.money.set")) {

                                    money.setBalance();
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));

                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                        BoostEconomy.playSuccessSound(p);
                                    }

                                    return true;

                                } else {

                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                    }
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("give")) {
                                if (sender.hasPermission("boosteconomy.money.give")) {

                                    money.addBalance();
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));

                                    if (sender instanceof Player) {
                                        BoostEconomy.playSuccessSound(p);
                                        BoostEconomy.playSuccessSound((Player) sender);
                                    }

                                    return true;

                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                                    if (sender instanceof Player) {
                                        BoostEconomy.playErrorSound((Player) sender);
                                    }
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("take")) {
                                if (sender.hasPermission("boosteconomy.money.take")) {

                                    money.takeBalance();
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));

                                    if (sender instanceof Player) {
                                        BoostEconomy.playSuccessSound(p);
                                        BoostEconomy.playSuccessSound((Player) sender);
                                    }

                                }else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                                    if (sender instanceof Player) {
                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            Player player = (Player) sender;
                                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                        }
                                    }
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));

                            if (sender instanceof Player) {
                                BoostEconomy.playErrorSound((Player) sender);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));

                        if (sender instanceof Player) {
                            BoostEconomy.playErrorSound((Player) sender);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));

                    if (sender instanceof Player) {
                        BoostEconomy.playErrorSound((Player) sender);
                    }
                }
            }
        }
        return false;
    }
}
