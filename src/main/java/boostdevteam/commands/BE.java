package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BE implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("be")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (args.length == 0) {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.InvalidArgs").replaceAll("&", "§"));
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        BoostEconomy.playErrorSound(p);
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if(sender.hasPermission("boosteconomy.reload")) {
                            try {
                                BoostEconomy.onReload();
                            }catch (Exception e) {
                                Bukkit.getLogger().severe("[BoostEconomy] §cError while reloading the plugin!");
                                e.printStackTrace();
                            }finally {
                                if (sender instanceof Player) {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.Reload").replaceAll("&", "§"));
                                    Player p = (Player) sender;
                                    BoostEconomy.playSuccessSound(p);
                                }
                            }
                        }else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playErrorSound(p);
                            }
                        }
                    }else if (args[0].equalsIgnoreCase("help")) {
                        if (sender.hasPermission("boosteconomy.help")) {
                            sender.sendMessage("§8§l§m+---------------------------+");
                            sender.sendMessage("§b§l/be <reload/help/debug/checkforupdates> §7The main command");
                            sender.sendMessage("§b§l/pay <player> <money> §7Send money to a player");
                            sender.sendMessage("§b§l/money [player] §7Show the money of a player");
                            sender.sendMessage("§b§l/eco <player> <set/give/take> <money> §7Commands for admin");
                            sender.sendMessage("§b§l/ecoreset <player> §7Resets the money of a player");
                            sender.sendMessage("§b§l/baltop §7Show the top balances of the server");
                            sender.sendMessage("§b§l/banknotes <give> <player> <money> §7Main command for the banknotes");
                            sender.sendMessage("§b§l/deposit §7Deposit the banknote in your hand in your bank");
                            sender.sendMessage("§b§l/withdraw <monbey> §7Create a banknote");
                            sender.sendMessage("§8§l§m+---------------------------+");
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playSuccessSound(p);
                            }
                        }else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playErrorSound(p);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("debug")) {
                        if (sender instanceof ConsoleCommandSender) {
                            Data data = new Data();
                            sender.sendMessage("§8+------------------------------------+");
                            sender.sendMessage("             §bBoostEconomy");
                            sender.sendMessage("                §4Debug");
                            sender.sendMessage("§a");
                            sender.sendMessage("§f-> §7MC-Version of the server: §c" + Bukkit.getBukkitVersion());
                            sender.sendMessage("§f-> §7Version of the plugin: §e" + BoostEconomy.plugin.getDescription().getVersion());
                            sender.sendMessage("§f-> §7Legacy: §a" + BoostEconomy.getInstance().isLegacy());
                            sender.sendMessage("§a");
                            sender.sendMessage("§f-> §7Server software: §6" + Bukkit.getName());
                            sender.sendMessage("§f-> §7Software version: §6" + Bukkit.getVersion());
                            sender.sendMessage("§a");
                            sender.sendMessage("§f-> §7Online players: §3" + Bukkit.getServer().getOnlinePlayers().size());
                            sender.sendMessage("§f-> §7Players (data.yml): §3" + data.getBalTop().size());
                            if (BoostEconomy.getInstance().getConfig().getBoolean("Config.AdvancedDebug", false)) {
                                sender.sendMessage("§a");
                                sender.sendMessage("§f-> §7PlaceholderAPI: §a" + Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"));
                            }
                            sender.sendMessage("§8+------------------------------------+");
                        }else {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playErrorSound(p);
                            }
                        }
                    }else if (args[0].equalsIgnoreCase("checkforupdates")) {
                        if (sender.hasPermission("boosteconomy.checkforupdates")) {
                            if (sender instanceof ConsoleCommandSender) {
                                if (!(BoostEconomy.getInstance().isLegacy())) {
                                    new boostdevteam.boosteconomy.UpdateChecker(BoostEconomy.plugin, 86591).getVersion(version -> {
                                        if (BoostEconomy.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                                            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aNo new version available!");
                                        } else {
                                            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] New version available! §av" + version);
                                            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] You have §cv" + BoostEconomy.plugin.getDescription().getVersion());
                                            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §eDownload it at https://www.spigotmc.org/resources/86591");
                                        }
                                    });
                                } else {
                                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] You can't use the updater in this version! §c(Works with 1.12+)");
                                }
                            } else {
                                if (!(BoostEconomy.getInstance().isLegacy())) {
                                    new boostdevteam.boosteconomy.UpdateChecker(BoostEconomy.plugin, 86591).getVersion(version -> {
                                        if (BoostEconomy.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                                            sender.sendMessage("§b§lBoostEconomy §8--> §aNo new version available!");
                                        } else {
                                            sender.sendMessage("§b§lBoostEconomy §8--> §7New version available! §av" + version);
                                            sender.sendMessage("§b§lBoostEconomy §8--> §7You have §cv" + BoostEconomy.plugin.getDescription().getVersion());
                                            sender.sendMessage("§b§lBoostEconomy §8--> §eDownload it at https://www.spigotmc.org/resources/86591");
                                        }
                                    });

                                    BoostEconomy.playSuccessSound((Player) sender);
                                } else {
                                    sender.sendMessage("§b§lBoostEconomy §8--> §7You can't use the updater in this version! §c(Works with 1.12+)");
                                }
                            }
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playSuccessSound(p);
                            }
                        }else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                BoostEconomy.playErrorSound(p);
                            }
                        }
                    }else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.InvalidArgs").replaceAll("&", "§"));
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            BoostEconomy.playErrorSound(p);
                        }
                    }
                }
            }
        }
        return false;
    }
}