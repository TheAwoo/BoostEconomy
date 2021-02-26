package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BalTop implements CommandExecutor {

    public ItemStack item (int i) {
        Data data = BoostEconomy.getData();
        Data.BoostPlayerData pData = data.getBalTop().get(i);

        String name = pData.getName();
        double money = pData.getMoney();

        if (!(BoostEconomy.getInstance().isLegacy())) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD, (i + 1));
            ItemMeta Meta = item.getItemMeta();
            Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Number")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1)));

            List<String> lore = new ArrayList<String>();
            lore.add("§7");
            lore.add(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Lore")
                    .replaceAll("&", "§")
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + money)
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(", ", ""));

            Meta.setLore(lore);
            item.setItemMeta(Meta);

            return item;
        }else {
            ItemStack item = new ItemStack(Material.getMaterial("SKULL_ITEM"), (i +1), (short) SkullType.PLAYER.ordinal());

            SkullMeta Meta = (SkullMeta) item.getItemMeta();
            Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Number")
                    .replaceAll("&", "§")
                    .replaceAll("%number%", "" + (i + 1)));

            List<String> lore = new ArrayList<String>();
            lore.add("§7");
            lore.add(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Lore")
                    .replaceAll("&", "§")
                    .replaceAll("%player%", "" + name)
                    .replaceAll("%money%", "" + money)
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll(", ", ""));

            Meta.setLore(lore);
            item.setItemMeta(Meta);

            return item;
        }
    }

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
                            FileConfiguration config = BoostEconomy.getInstance().getConfig();
                            Data data = BoostEconomy.getData();
                            Inventory balTopGui = Bukkit.createInventory(p, 54, config.getString("GUI.BalTop.Title")
                                    .replaceAll("&", "§"));

                            for (int i = 0, pointer = 1; i < data.getBalTop().size() && i < 11; i++, pointer++) {

                                if (pointer == 1) {
                                    balTopGui.setItem(13, this.item(i));
                                } else if (pointer == 2) {
                                    balTopGui.setItem(21, this.item(i));
                                } else if (pointer == 3) {
                                    balTopGui.setItem(23, this.item(i));
                                } else if (pointer == 4) {
                                    balTopGui.setItem(27, this.item(i));
                                } else if (pointer == 5) {
                                    balTopGui.setItem(28, this.item(i));
                                } else if (pointer == 6) {
                                    balTopGui.setItem(29, this.item(i));
                                } else if (pointer == 7) {
                                    balTopGui.setItem(33, this.item(i));
                                } else if (pointer == 8) {
                                    balTopGui.setItem(34, this.item(i));
                                } else if (pointer == 9) {
                                    balTopGui.setItem(35, this.item(i));
                                } else if (pointer == 10) {
                                    balTopGui.setItem(40, this.item(i));
                                }
                            }

                            if (!(BoostEconomy.getInstance().isLegacy())) {
                                ItemStack fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                                ItemMeta fillerMeta = fillerItem.getItemMeta();
                                fillerMeta.setDisplayName("§7");

                                for (int filler = 0; filler < balTopGui.getSize(); filler++) {
                                    if (balTopGui.getItem(filler) == null || balTopGui.getItem(filler).getType().equals(Material.AIR)) {
                                        balTopGui.setItem(filler, fillerItem);
                                    }
                                }
                            } else {
                                for (int filler = 0; filler < balTopGui.getSize(); filler++) {
                                    if (balTopGui.getItem(filler) == null || balTopGui.getItem(filler).getType().equals(Material.AIR)) {
                                        balTopGui.setItem(filler, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 15));
                                    }
                                }
                            }

                            BoostEconomy.playSuccessSound(p);

                            p.openInventory(balTopGui);
                            return true;
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
