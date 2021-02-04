package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import boostdevteam.misc.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EcoReset implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("ecoreset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("boosteconomy.ecoreset")) {
                    if (args.length == 0) {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Reset").replaceAll("&", "§"));

                        if (sender instanceof Player) {
                            BoostEconomy.playErrorSound(player);
                        }

                    } else if (args.length == 1) {
                        if (sender.hasPermission("boosteconomy.ecoreset")) {
                            Player p = Bukkit.getServer().getPlayer(args[0]);
                            if (p != null) {
                                Data data = BoostEconomy.getData();

                                Economy eco = new Economy(p, 0);

                                data.saveData(p, BoostEconomy.getInstance().getConfig().getDouble("Config.StartMoney"));

                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Done").replaceAll("&", "§"));
                                p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Resetted").replaceAll("&", "§").replaceAll("%money%", "" + eco.getBalance()));

                                if (sender instanceof Player) {
                                    BoostEconomy.playSuccessSound(player);
                                    BoostEconomy.playSuccessSound(p);
                                }

                                return true;

                            } else {

                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));

                                if (sender instanceof Player) {
                                    BoostEconomy.playErrorSound(player);
                                }
                            }
                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                            if (sender instanceof Player) {
                                BoostEconomy.playErrorSound(player);
                            }
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.InvalidArgs.Reset").replaceAll("&", "§"));

                        if (sender instanceof Player) {
                            BoostEconomy.playErrorSound(player);
                        }
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));

                    if (sender instanceof Player) {
                        BoostEconomy.playErrorSound(player);
                    }
                }
            } else {
                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
            }
        }
        return false;
    }
}
