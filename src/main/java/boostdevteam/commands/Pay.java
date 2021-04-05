package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.vaultapi.misc.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandlabel, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("pay")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("boosteconomy.pay") || sender.hasPermission("boosteconomy.*")) {
                    if (args.length == 2) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null) {
                            if (!(p == sender)) {
                                try {
                                    if (NumberUtils.isNumber(args[1])) {
                                        if (Long.parseLong(args[1]) > 0L) {
                                            Economy money = new Economy((Player) sender, Long.parseLong(args[1]));
                                            if (money.detractable()) {

                                                Economy ecoTarget = new Economy(p, Double.parseDouble(args[1]));
                                                money.takeBalance();
                                                ecoTarget.addBalance();

                                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Pay.Send")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%money%", "" + Long.parseLong(args[1]))
                                                        .replaceAll("%target%", "" + p.getName()));
                                                p.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Pay.Received")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%money%", "" + Long.parseLong(args[1]))
                                                        .replaceAll("%player%", "" + sender.getName()));

                                                BoostEconomy.playSuccessSound(p);
                                                BoostEconomy.playSuccessSound(player);

                                                String senderName = sender instanceof ConsoleCommandSender ? "Console" : sender.getName();
                                                BoostEconomy.saveLog(senderName + " has sent " + Long.parseLong(args[1]) + "$ to " + p.getName());
                                                BoostEconomy.saveLog(p.getName() + " received " + Long.parseLong(args[1]) + "$ from " + senderName);

                                            } else {
                                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.NoMoney").replaceAll("&", "§"));
                                                BoostEconomy.playErrorSound(player);
                                            }

                                        } else {
                                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                                            BoostEconomy.playErrorSound(player);
                                        }
                                    } else {
                                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                                        BoostEconomy.playErrorSound(player);
                                    }
                                    return true;
                                }catch (NumberFormatException e) {
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                                }

                            }else {
                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PayYourself").replaceAll("&", "§"));
                                BoostEconomy.playErrorSound(player);
                            }

                        } else {
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                            BoostEconomy.playErrorSound(player);
                            return true;
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                        BoostEconomy.playErrorSound(player);
                        return true;
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                    BoostEconomy.playErrorSound(player);
                    return true;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoConsole").replaceAll("&", "§"));
            }
            return true;
        }
        return false;
     }
}
