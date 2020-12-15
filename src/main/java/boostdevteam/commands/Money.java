package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.misc.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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

        @SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(id, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(BoostEconomy.getInstance().getConfig().getBoolean("GUI.UseGUI"))) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("boosteconomy.money")) {
                    if (cmd.getName().equalsIgnoreCase("money")) {
                        if (args.length == 0) {
                            Economy money = new Economy((Player) sender, 0);
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                        } else if (args.length == 1) {
                            if (sender.hasPermission("boosteconomy.money.others")) {
                                Player p = Bukkit.getServer().getPlayer(args[0]);
                                if (p != null) {
                                    if (!(p == sender)) {
                                        Economy ecoTarget = new Economy(p, 0);
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            player.playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                        }
                                    } else {
                                        Economy money = new Economy((Player) sender, 0);
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Chat").replaceAll("&", "§").replaceAll("%money%", "" + money.getBalance()));
                                    }
                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                    }
                                }
                            } else {
                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                    player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                } else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                    }
                }
            } else {
                if (cmd.getName().equalsIgnoreCase("money")) {
                    if (args.length == 0) {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                    } else if (args.length == 1) {
                        Player p = Bukkit.getServer().getPlayer(args[0]);
                        if (p != null) {
                            Economy ecoTarget = new Economy(p, 0);
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                        } else {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                        }
                    }
                }
            }
        }
        //
        if (BoostEconomy.getInstance().getConfig().getBoolean("GUI.UseGUI")) {
            if (Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (sender.hasPermission("boosteconomy.money")) {
                        if (cmd.getName().equalsIgnoreCase("money")) {
                            if (args.length == 0) {

                                Inventory money = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                Economy eco = new Economy(player, 0);

                                ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                                SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                Meta.setOwningPlayer(player);
                                Meta.setDisplayName("§cYou have " + eco.getBalance() + "$");
                                skull.setItemMeta(Meta);

                                money.setItem(13, skull);
                                //money.setItem(13, createButton(Material.PLAYER_HEAD, (short) 0, 1, new ArrayList<String>(), "§cYou have " + eco.getBalance() + "$"));
                                //

                                for (int i = 0; i < money.getSize(); i++) {
                                    if (money.getItem(i) == null || money.getItem(i).getType().equals(Material.AIR)) {
                                        money.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 0, 1, new ArrayList<String>(), "§a"));
                                    }
                                }

                                if (sender instanceof Player) {
                                    Player p = (Player) sender;
                                    p.playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                }

                                player.openInventory(money);

                            } else if (args.length == 1) {
                                if (sender.hasPermission("boosteconomy.money.others")) {
                                    Player p = Bukkit.getServer().getPlayer(args[0]);
                                    final String eventPlayer = p.getName();
                                    if (p != null) {
                                        if (!(p == sender)) {
                                            Inventory moneyTarget = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                            Economy ecoTarget = new Economy(p, 0);

                                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                                            SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                            Meta.setOwningPlayer(p);
                                            Meta.setDisplayName("§c" + p.getName() + " has " + ecoTarget.getBalance() + "$");
                                            skull.setItemMeta(Meta);

                                            moneyTarget.setItem(13, skull);
                                            //

                                            for (int i = 0; i < moneyTarget.getSize(); i++) {
                                                if (moneyTarget.getItem(i) == null || moneyTarget.getItem(i).getType().equals(Material.AIR)) {
                                                    moneyTarget.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 0, 1, new ArrayList<String>(), "§a"));
                                                }
                                            }

                                            if (sender instanceof Player) {
                                                p.playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            }

                                            player.openInventory(moneyTarget);

                                            if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                                player.playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            }
                                        } else {

                                            Inventory money = Bukkit.createInventory(player, 27, BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"));
                                            Economy eco = new Economy(player, 0);

                                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
                                            SkullMeta Meta = (SkullMeta) skull.getItemMeta();
                                            Meta.setOwningPlayer(player);
                                            Meta.setDisplayName("§cYou have " + eco.getBalance() + "$");
                                            skull.setItemMeta(Meta);

                                            money.setItem(13, skull);

                                            for (int i = 0; i < money.getSize(); i++) {
                                                if (money.getItem(i) == null || money.getItem(i).getType().equals(Material.AIR)) {
                                                    money.setItem(i, createButton(Material.BLACK_STAINED_GLASS_PANE, (short) 0, 1, new ArrayList<String>(), "§a"));
                                                }
                                            }

                                            if (sender instanceof Player) {
                                                p.playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            }

                                            player.openInventory(money);
                                        }
                                    } else {
                                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                        }
                                    }
                                } else {
                                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                                    if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                                        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                                    }
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                        }
                    }
                } else {
                    if (cmd.getName().equalsIgnoreCase("money")) {
                        if (args.length == 0) {
                            sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                        } else if (args.length == 1) {
                            Player p = Bukkit.getServer().getPlayer(args[0]);
                            if (p != null) {
                                Economy ecoTarget = new Economy(p, 0);
                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.Others").replaceAll("&", "§").replaceAll("%target%", "" + p.getName()).replaceAll("%money%", "" + ecoTarget.getBalance()));

                            } else {
                                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.Money.PlayerNotFound").replaceAll("&", "§"));
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("GUI.InvalidVersion").replaceAll("&", "§"));
                sender.sendMessage("§b§lMoney §8--> §7The config GUI has been reset to false! §c(Only for 1.14+)");
                try {
                    BoostEconomy.getInstance().getConfig().set("GUI.UseGUI", false);
                    BoostEconomy.getInstance().saveDefaultConfig();
                    BoostEconomy.getInstance().saveConfig();
                }catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] Error while resetting the §cconfig.yml");
                }finally {
                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] Reset has been successfully done on GUI.UseGUI in the §cconfig.yml");
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

        if (e.getView().getTitle().equals(BoostEconomy.getInstance().getConfig().getString("GUI.Money.Title").replaceAll("&", "§"))) {
            e.setCancelled(true);
        }
    }
}
