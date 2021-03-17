package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Money implements CommandExecutor, Listener {

    public ItemStack createButton(Material id, short data, int amount, List<String> lore, String display) {

        ItemStack item = new ItemStack(id, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(BoostEconomy.getInstance().getConfig().getBoolean("GUI.Money.UseGUI"))) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("boosteconomy.money") || sender.hasPermission("boosteconomy.*")) {
                    if (cmd.getName().equalsIgnoreCase("money")) {
                        if (args.length == 0) {
                            Economy money = new Economy(player, 0);
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                        } else if (args.length == 1) {
                            if (sender.hasPermission("boosteconomy.money.others") || sender.hasPermission("boosteconomy.*")) {
                                Player p = Bukkit.getServer().getPlayer(args[0]);
                                if (p != null) {
                                    if (!(p == sender)) {
                                        Economy ecoTarget = new Economy(p, 0);
                                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                                        BoostEconomy.playSuccessSound(player);
                                    } else {
                                        Economy money = new Economy((Player) sender, 0);

                                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                                        BoostEconomy.playSuccessSound(player);
                                    }
                                } else {
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                                    BoostEconomy.playErrorSound(player);
                                }
                            } else {
                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                BoostEconomy.playErrorSound(player);
                            }
                        }
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                    BoostEconomy.playErrorSound(player);
                }
            } else {
                if (cmd.getName().equalsIgnoreCase("money")) {
                    if (args.length == 0) {
                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                    } else if (args.length == 1) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null) {
                            Economy ecoTarget = new Economy(p, 0);
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));
                        } else {
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                        }
                    }
                }
            }
        }
        //
        if (BoostEconomy.getInstance().getConfig().getBoolean("GUI.Money.UseGUI")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (sender.hasPermission("boosteconomy.money") || sender.hasPermission("boosteconomy.*")) {
                        if (cmd.getName().equalsIgnoreCase("money")) {
                            if (args.length == 0) {

                                Inventory money = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                Economy eco = new Economy(player, 0);

                                if (!(BoostEconomy.getInstance().isLegacy())) {
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

                                    SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                    Meta.setOwningPlayer(player);
                                    Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.YourHead")
                                            .replaceAll("&", "§")
                                            .replaceAll("%money%", "" + eco.getBalance()));
                                    skull.setItemMeta(Meta);

                                    money.setItem(13, skull);

                                } else {
                                    ItemStack skull = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());

                                    SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                    Meta.setOwner("" + player);
                                    Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.YourHead")
                                            .replaceAll("&", "§")
                                            .replaceAll("%money%", "" + eco.getBalance()));
                                    skull.setItemMeta(Meta);

                                    money.setItem(13, skull);
                                }

                                for (int i = 0; i < money.getSize(); i++) {
                                    if (money.getItem(i) == null || money.getItem(i).getType().equals(Material.AIR)) {
                                        if (!(BoostEconomy.getInstance().isLegacy())) {
                                            money.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 15, 1, new ArrayList<String>(), "§a"));
                                        } else {
                                            money.setItem(i, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 15));
                                        }
                                    }
                                }

                                BoostEconomy.playSuccessSound(player);

                                player.openInventory(money);

                            } else if (args.length == 1) {
                                if (sender.hasPermission("boosteconomy.money.others") || sender.hasPermission("boosteconomy.*")) {
                                    Player p = Bukkit.getServer().getPlayer(args[0]);
                                    if (p != null) {
                                        if (!(p == sender)) {
                                            Inventory moneyTarget = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                            Economy ecoTarget = new Economy(p, 0);

                                            if (!(BoostEconomy.getInstance().isLegacy())) {

                                                ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                                                SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                                Meta.setOwningPlayer(p);
                                                Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.OthersHead")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%target%", "" + p.getName())
                                                        .replaceAll("%money%", "" + ecoTarget.getBalance()));
                                                skull.setItemMeta(Meta);

                                                moneyTarget.setItem(13, skull);

                                            } else {

                                                ItemStack skullItem = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());

                                                SkullMeta Meta = (SkullMeta) skullItem.getItemMeta();
                                                Meta.setOwner("" + player);
                                                Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.OthersHead")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%target%", "" + p.getName())
                                                        .replaceAll("%money%", "" + ecoTarget.getBalance()));
                                                skullItem.setItemMeta(Meta);

                                                moneyTarget.setItem(13, skullItem);
                                            }

                                            //

                                            for (int i = 0; i < moneyTarget.getSize(); i++) {
                                                if (moneyTarget.getItem(i) == null || moneyTarget.getItem(i).getType().equals(Material.AIR)) {
                                                    if (!(BoostEconomy.getInstance().isLegacy())) {
                                                        moneyTarget.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 15, 1, new ArrayList<String>(), "§a"));
                                                    } else {
                                                        moneyTarget.setItem(i, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 15));
                                                    }
                                                }
                                            }

                                            BoostEconomy.playSuccessSound(player);

                                            player.openInventory(moneyTarget);

                                        } else {

                                            Inventory money = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                            Economy eco = new Economy(player, 0);

                                            if (!(BoostEconomy.getInstance().isLegacy())) {
                                                ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

                                                SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                                Meta.setOwningPlayer(player);
                                                Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.YourHead")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%money%", "" + eco.getBalance()));
                                                skull.setItemMeta(Meta);

                                                money.setItem(13, skull);

                                            } else {
                                                ItemStack skull = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());

                                                SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                                Meta.setDisplayName(BoostEconomy.getInstance().getConfig().getString("GUI.Money.YourHead")
                                                        .replaceAll("&", "§")
                                                        .replaceAll("%money%", "" + eco.getBalance()));
                                                skull.setItemMeta(Meta);

                                                money.setItem(13, skull);
                                            }



                                            for (int i = 0; i < money.getSize(); i++) {
                                                if (money.getItem(i) == null || money.getItem(i).getType().equals(Material.AIR)) {
                                                    if (!(BoostEconomy.getInstance().isLegacy())) {
                                                        money.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 15, 1, new ArrayList<String>(), "§a"));
                                                    } else {
                                                        money.setItem(i, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 15));
                                                    }
                                                }
                                            }

                                            BoostEconomy.playSuccessSound(player);

                                            player.openInventory(money);
                                        }
                                    } else {
                                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                                        BoostEconomy.playErrorSound(player);
                                    }
                                } else {
                                    sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    BoostEconomy.playErrorSound(player);
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                        BoostEconomy.playErrorSound(player);
                    }
                } else {
                    if (cmd.getName().equalsIgnoreCase("money") || sender.hasPermission("boosteconomy.*")) {
                        if (args.length == 0) {
                            sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                        } else if (args.length == 1) {
                            Player p = Bukkit.getServer().getPlayer(args[0]);
                            if (p != null) {
                                Economy ecoTarget = new Economy(p, 0);
                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));
                            } else {
                                sender.sendMessage(BoostEconomy.getLanguage().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                            }
                        }
                    }
                }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)

    public void cancelError (InventoryClickEvent e) {

        if (e.getCurrentItem() == null) {
            return;
        }
        if (!(BoostEconomy.getInstance().isLegacy())) {

            if (e.getView().getTitle().equals(BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"))) {
                e.setCancelled(true);
            }
            if (e.getView().getTitle().equals(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Title").replaceAll("&", "§"))) {
                e.setCancelled(true);
            }
        } else {
            if (e.getClickedInventory().getTitle().equals(BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"))) {
                e.setCancelled(true);
            }
            if (e.getClickedInventory().getTitle().equals(BoostEconomy.getInstance().getConfig().getString("GUI.BalTop.Title").replaceAll("&", "§"))) {
                e.setCancelled(true);
            }
        }
    }
}