package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pay")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("boosteconnomy.pay")) {
                    if (args.length == 2) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null) {
                            if (!(p == sender)) {
                                if (NumberUtils.isNumber(args[1])) {
                                    Economy money = new Economy((Player) sender, Double.parseDouble(args[1]));
                                    if (money.detractable()) {

                                        Economy ecoTarget = new Economy(p, Double.parseDouble(args[1]));
                                        money.takeBalance();
                                        ecoTarget.addBalance();

                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Pay.Send")
                                                .replaceAll("&", "§")
                                                .replaceAll("%money%", "" + Double.parseDouble(args[1]))
                                                .replaceAll("%target%", "" + p.getName()));
                                        p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Pay.Received")
                                                .replaceAll("&", "§")
                                                .replaceAll("%money%", "" + Double.parseDouble(args[1]))
                                                .replaceAll("%player%", "" + sender.getName()));

                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            player.playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            p.playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                        }


                                        return true;

                                    } else {
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.NoMoney").replaceAll("&", "§"));
                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                        }
                                    }
                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                    }
                                    return true;
                                }
                            }else {
                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PayYourself").replaceAll("&", "§"));
                                if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                    player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                }
                            }

                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                            if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Pay").replaceAll("&", "§"));
                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                    }
                    return true;
                }
                return true;
            } else {
                Bukkit.getConsoleSender().sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
            }
        }
        return true;
     }
}
