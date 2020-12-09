package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
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
                                    return true;

                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("give")) {
                                if (sender.hasPermission("boosteconomy.money.give")) {

                                    money.addBalance();
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                                    return true;

                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    return true;
                                }
                            } else if (args[1].equalsIgnoreCase("take")) {
                                if (sender.hasPermission("boosteconomy.money.take")) {

                                    money.takeBalance();
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Refreshed").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));

                                }else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));
                            return true;
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                        return true;
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Eco").replaceAll("&", "§"));
                }
            }
        }
        return true;
    }
}
