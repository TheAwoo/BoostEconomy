package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(sender.hasPermission("boosteconomy.money")) {
                if(cmd.getName().equalsIgnoreCase("money")) {
                    if (args.length == 0) {
                        Economy money = new Economy((Player) sender, 0);
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                    }else if (args.length == 1) {
                            if (sender.hasPermission("boosteconomy.money.others")) {
                                Player p = Bukkit.getServer().getPlayer(args[0]);
                                if (p != null) {
                                    if (!(p == sender)) {
                                        Economy ecoTarget = new Economy(p, 0);
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            player.playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            }
                                        }else {
                                        Economy money = new Economy((Player) sender, 0);
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                                    }
                                    }else {
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                    }
                                }
                        }else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                    player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                }
                        }
                    }
                }
            }
        }else {
            if(cmd.getName().equalsIgnoreCase("money")) {
                if (args.length == 0) {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                } else if (args.length == 1) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null) {
                            Economy ecoTarget = new Economy(p, 0);
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%player%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                        }else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                        }
                    }
                }
            }
        return true;
    }
}
