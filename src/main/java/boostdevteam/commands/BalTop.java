package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BalTop implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("baltop")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("boosteconomy.baltop")) {
                    if (!(BoostEconomy.getInstance().getConfig().getBoolean("GUI.BalTop.UseGUI"))) {
                        p.sendMessage("§8§m§l+------------------------+");
                        p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.BalTop.Header").replaceAll("&", "§"));
                        p.sendMessage("§8§m§l+------------------------+");

                        Data data = BoostEconomy.getData();

                        int page = 1;
                        int pageSize = 10;
                        int start = (page - 1) * pageSize;

                        // Send the message
                        for ( int i = start; i < data.getBalTop().size() && i < start + pageSize; i++ ) {
                            Data.BoostPlayerData pData = data.getBalTop().get(i);
                            FileConfiguration config = BoostEconomy.getInstance().getConfig();

                            String name = pData.getName();
                            double money = pData.getMoney();

                            p.sendMessage(config.getString("Messages.BalTop.PlayerFormat")
                                    .replaceAll("&", "§")
                                    .replaceAll("%number%", "" + (i + 1))
                                    .replaceAll("%player%", "" + name)
                                    .replaceAll("%money%", "" + money));
                        }

                        p.sendMessage("§8§m§l+------------------------+");

                        BoostEconomy.playSuccessSound(p);

                        return true;
                    } else {
                        if (Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
                            FileConfiguration config = BoostEconomy.getInstance().getConfig();
                            Data data = BoostEconomy.getData();
                            Inventory balTopGui = Bukkit.createInventory(p, 54, config.getString("GUI.BalTop.Title")
                                    .replaceAll("&", "§"));

                            for (int i = 0, pointer = 1; i < data.getBalTop().size() && i < 11; i++, pointer++) {
                                Data.BoostPlayerData pData = data.getBalTop().get(i);

                                String name = pData.getName();
                                double money = pData.getMoney();

                                ItemStack item = new ItemStack(Material.PLAYER_HEAD, (i + 1));
                                ItemMeta Meta = item.getItemMeta();
                                Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Number")
                                        .replaceAll("&", "§")
                                        .replaceAll("%number%", "" + (i + 1)));

                                List<String> lore = new ArrayList<String>();
                                lore.add("§7");
                                lore.add(config.getString("GUI.BalTop.Lore")
                                        .replaceAll("&", "§")
                                        .replaceAll("%player%", "" + name)
                                        .replaceAll("%money%", "" + money)
                                        .replaceAll("\\[", "")
                                        .replaceAll("\\]", "")
                                        .replaceAll(", ", ""));

                                Meta.setLore(lore);
                                item.setItemMeta(Meta);

                                if (pointer == 1) {
                                    balTopGui.setItem(13, item);
                                } else if (pointer == 2) {
                                    balTopGui.setItem(21, item);
                                } else if (pointer == 3) {
                                    balTopGui.setItem(23, item);
                                } else if (pointer == 4) {
                                    balTopGui.setItem(27, item);
                                } else if (pointer == 5) {
                                    balTopGui.setItem(28, item);
                                } else if (pointer == 6) {
                                    balTopGui.setItem(29, item);
                                } else if (pointer == 7) {
                                    balTopGui.setItem(33, item);
                                } else if (pointer == 8) {
                                    balTopGui.setItem(34, item);
                                } else if (pointer == 9) {
                                    balTopGui.setItem(35, item);
                                } else if (pointer == 10) {
                                    balTopGui.setItem(40, item);
                                }
                            }

                            ItemStack fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                            ItemMeta fillerMeta = fillerItem.getItemMeta();
                            fillerMeta.setDisplayName("§7");

                            for (int filler = 0; filler < balTopGui.getSize(); filler++) {
                                if (balTopGui.getItem(filler) == null || balTopGui.getItem(filler).getType().equals(Material.AIR)) {
                                    balTopGui.setItem(filler, fillerItem);
                                }
                            }

                            BoostEconomy.playSuccessSound(p);

                            p.openInventory(balTopGui);
                            return true;
                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("GUI.InvalidVersion").replaceAll("&", "§"));
                            sender.sendMessage("§b§lGUI §8--> §7The config GUI has been reset to false! §c(Only for 1.14+)");
                            BoostEconomy.playErrorSound(p);
                            try {
                                BoostEconomy.getInstance().getConfig().set("GUI.BalTop.UseGUI", false);
                                BoostEconomy.getInstance().saveDefaultConfig();
                                BoostEconomy.getInstance().saveConfig();
                            }catch (Exception e) {
                                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] Error while resetting the §cconfig.yml");
                            }finally {
                                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] Reset has been successfully done on GUI.UseGUI in the §cconfig.yml");
                            }
                        }
                    }
                }else {
                        Player player = (Player) sender;
                        player.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                        BoostEconomy.playErrorSound(player);
                }
                return true;
            }else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                }
            }
        return false;
    }
}
