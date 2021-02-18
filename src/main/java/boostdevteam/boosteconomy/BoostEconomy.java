package boostdevteam.boosteconomy;

import boostdevteam.commands.*;
import boostdevteam.events.MobKillEvent;
import boostdevteam.events.PlayerJoinEvent;
import boostdevteam.events.PluginListener;
import boostdevteam.placeholderapi.Placeholders;
import boostdevteam.tabcompleter.*;
import boostdevteam.vaultapi.VEconomy;
import boostdevteam.vaultapi.VHook;
import com.google.common.io.Files;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class BoostEconomy extends JavaPlugin implements Listener {

    public static BoostEconomy plugin;
    public static Data data;
    public static MobFile mob;

    public static VEconomy veco;
    public static VHook hook;

    private final Pattern MONEY_PATTERN = Pattern.compile("((([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?\\d?\\d?)?$)");

    //The base banknote item
    private ItemStack base;

    // Economy instance
    private Economy economy;
     //The base lore for the item
    private List<String> baseLore;

    public String colorMessage(String message) {
        if (message == null) {
            return "null";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(String path) {
        if (!getConfig().isString(path)) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    public static void onReload(CommandSender sender) {
        long before = System.currentTimeMillis();
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            plugin.saveConfig();

            data = new Data();
            mob = new MobFile();

            new Data();
            new MobFile();
        }catch (Exception e){
            if (sender instanceof Player) {
                sender.sendMessage("§b§lBoostEconomy §8--> §cError while reloading the plugin!");
                e.printStackTrace();
            }

            Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError while reloading the plugin!");
            e.printStackTrace();

        }finally {
            if (sender instanceof Player) {
                sender.sendMessage(getInstance().getConfig().getString("Messages.General.Reload")
                        .replaceAll("&", "§")
                        .replaceAll("%time%", "" + (System.currentTimeMillis() - before)));
                playSuccessSound((Player) sender);
            }

            Bukkit.getConsoleSender().sendMessage("§7[BoostEconomy§7] §aPlugin reloaded with success! (" + (System.currentTimeMillis() - before) + "ms)");

        }
    }

    /**
     * Methods called when a player do something
     *
     */

    public static void playErrorSound (Player player) {
        if (getInstance().getConfig().getBoolean("Config.UseSounds")) {
            try {
                Sound x = Sound.valueOf(getInstance().getConfig().getString("Config.Sounds.Error", "ENTITY_VILLAGER_NO"));
                player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Error on the §cplayErrorSound§7! Check your config!");
                e.printStackTrace();
            }
        }
    }

    public static void playSuccessSound (Player player) {
        if (getInstance().getConfig().getBoolean("Config.UseSounds")) {
            try {
                Sound x = Sound.valueOf(getInstance().getConfig().getString("Config.Sounds.Success", "ENTITY_PLAYER_LEVELUP"));
                player.playSound(player.getPlayer().getLocation(), x, 1.0f, 1.0f);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §7Error on the §cplaySuccessSound§7! Check your config!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoad() {
        // Plugin load logic

        Bukkit.getConsoleSender().sendMessage("§7[BoostEconomy] §eLoading!");

        saveDefaultConfig();

        YamlConfiguration config = new YamlConfiguration();
        File file = new File(getDataFolder () + File.separator + "config.yml");
        try {
            config.loadFromString (Files.toString (file, StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace ();
        }
    }

    public static void useSounds () {
        if (Bukkit.getVersion().contains("1.9")
                || Bukkit.getVersion().contains("1.10")
                || Bukkit.getVersion().contains("1.11")
                || Bukkit.getVersion().contains("1.12")
                || Bukkit.getVersion().contains("1.13")
                || Bukkit.getVersion().contains("1.14")
                || Bukkit.getVersion().contains("1.15")
                || Bukkit.getVersion().contains("1.16")) {
            getInstance().getConfig().set("Config.UseSounds", true);
        } else {
            getInstance().getConfig().set("Config.UseSounds", false);
            Bukkit.getConsoleSender().sendMessage("§f-> §eThe sounds has been disabled to prevent errors, " +
                    "if you want to use the sounds you need to change that to true and set the sounds for your version!");
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        if (!setupEconomy() ) {
            onDisable();
        }else {
            long before = System.currentTimeMillis();
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("            §bBoostEconomy");
            Bukkit.getConsoleSender().sendMessage("              §aEnabling");
            Bukkit.getConsoleSender().sendMessage("§8");
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                if (getConfig().getBoolean("Config.Placeholders")) {
                    try {
                        new Placeholders(this).register();
                    } catch (NoClassDefFoundError e) {
                        Bukkit.getConsoleSender().sendMessage("§f-> §cError on hooking with PlaceholderAPI");
                    } finally {
                        Bukkit.getConsoleSender().sendMessage("§f-> §7Hooked with §aPlaceholderAPI§7!");
                        Bukkit.getConsoleSender().sendMessage("§f-> §7Loaded §e%boosteconomy_money% §7placeholder!");
                        Bukkit.getConsoleSender().sendMessage("§f-> §7Loaded §e%boosteconomy_servertotal% §7placeholder!");
                    }
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("§f-> §7Could not find §cPlaceholderAPI§7 for placeholders, no placeholders will be added!");
            }

            try {
                int pluginId = 9572;
                @SuppressWarnings("unused")
                MetricsLite metrics = new MetricsLite(this, pluginId);
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§f-> §cError with metrics!");
                e.printStackTrace();
            }
            //
            try {

                data = new Data();
                mob = new MobFile();

                new Data();
                new MobFile();

                try {
                    veco = new VEconomy(plugin);
                    hook = new VHook();

                    hook.onHook();
                }catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§f-> §cError hooking with Vault!");
                    Bukkit.getConsoleSender().sendMessage("§f-> §cIs Vault loaded?");
                } finally {
                    Bukkit.getConsoleSender().sendMessage("§f-> §7Hooked with §aVault§7!");
                }

                BoostEconomy.getInstance().getConfig().options().header(
                        "BoostEconomy"
                ).copyDefaults(true);

                useSounds();

                loadEvents();
                loadCommands();

                ConsoleUpdater();

            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§f-> §cUnexpected error!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("§7");
                Bukkit.getConsoleSender().sendMessage("§aPlugin loaded with success! (" + (System.currentTimeMillis() - before) + "ms)");
                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            }
        }
    }

    public static void ConsoleUpdater () {
        if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BoostEconomy.plugin, () -> {
                long before = System.currentTimeMillis();
                if (BoostEconomy.getInstance().getConfig().getBoolean("Config.CheckForUpdates.Console")) {
                    new UpdateChecker(plugin, 86591).getVersion(version -> {
                        if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                            Bukkit.getConsoleSender().sendMessage("            §bBoostEconomy");
                            Bukkit.getConsoleSender().sendMessage("            §eUpdater (" + (System.currentTimeMillis() - before) + "ms)");
                            Bukkit.getConsoleSender().sendMessage("§8");
                            Bukkit.getConsoleSender().sendMessage("§f-> §aNo new version available!");
                            Bukkit.getConsoleSender().sendMessage("§8");
                            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                        } else {
                            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                            Bukkit.getConsoleSender().sendMessage("            §bBoostEconomy");
                            Bukkit.getConsoleSender().sendMessage("            §eUpdater (" + (System.currentTimeMillis() - before) + "ms)");
                            Bukkit.getConsoleSender().sendMessage("§8");
                            Bukkit.getConsoleSender().sendMessage("§f-> New version available! §av" + version);
                            Bukkit.getConsoleSender().sendMessage("§f-> You have §cv" + plugin.getDescription().getVersion());
                            Bukkit.getConsoleSender().sendMessage("§f-> §eDownload it at https://www.spigotmc.org/resources/86591");
                            Bukkit.getConsoleSender().sendMessage("§8");
                            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                        }
                    });
                }
            }, 40);
        }else {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("            §bBoostEconomy");
            Bukkit.getConsoleSender().sendMessage("               §eUpdater");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§f-> You are using a server version not compatible with the updater! §c(Works with 1.12+)");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        }
    }

    public String formatDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

        int max = getConfig().getInt("Banknotes.Maximum-Float-Amount");
        int min = getConfig().getInt("Banknotes.Minimum-Float-Amount");

        nf.setMaximumFractionDigits(max);
        nf.setMinimumFractionDigits(min);
        return nf.format(value);
    }

    public void loadEvents() {
        // OnJoin and OnQuit data saves
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
        // CheckForUpdates event
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        // InventoryClick Event
        Bukkit.getPluginManager().registerEvents(new Money(), this);
        // Banknotes claim event
        Bukkit.getPluginManager().registerEvents(new boostdevteam.events.Banknotes(this), this);
        // Entity kill reward
        this.getServer().getPluginManager().registerEvents(new MobKillEvent(this), this);

    }

    @NotNull
    @SuppressWarnings("all")
    public void loadCommands() {
        getCommand("money").setExecutor(new Money());
        getCommand("money").setTabCompleter(new MoneyTabCompleter());

        getCommand("eco").setExecutor(new Eco());
        getCommand("eco").setTabCompleter(new EcoTabCompleter());

        getCommand("be").setExecutor(new BE());
        getCommand("be").setTabCompleter(new BETabCompleter());

        getCommand("pay").setExecutor(new Pay());
        getCommand("pay").setTabCompleter(new PayTabCompleter());

        getCommand("ecoreset").setExecutor(new EcoReset());
        getCommand("ecoreset").setTabCompleter(new EcoResetTabCompleter());

        getCommand("baltop").setExecutor(new BalTop());
        getCommand("baltop").setTabCompleter(new BalTopTabCompleter());

        getCommand("withdraw").setExecutor(new Withdraw(this));
        getCommand("withdraw").setTabCompleter(new WithdrawTabCompleter());

        getCommand("deposit").setExecutor(new Deposit(this));
        getCommand("deposit").setTabCompleter(new DepositTabCompleter());

        getCommand("banknotes").setExecutor(new Banknotes(this));
        getCommand("banknotes").setTabCompleter(new BanknotesTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("           §bBoostEconomy");
            Bukkit.getConsoleSender().sendMessage("             §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§f-> §cDisabled due to no Vault dependency found!");
            Bukkit.getConsoleSender().sendMessage("§8");
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        } else {
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            Bukkit.getConsoleSender().sendMessage("           §bBoostEconomy");
            Bukkit.getConsoleSender().sendMessage("             §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§8");

            try {
                hook.offHook();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§f-> §cError on unhooking from Vault!");
                Bukkit.getConsoleSender().sendMessage("§f-> §cIs Vault loaded?");
                Bukkit.getConsoleSender().sendMessage("§8");
            } finally {
                Bukkit.getConsoleSender().sendMessage("§f-> §7UnHooked from §aVault§7!");
                Bukkit.getConsoleSender().sendMessage("§f-> §cPlugin disabled with success!");
                Bukkit.getConsoleSender().sendMessage("§8");
            }
            Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
        }
    }

    public static BoostEconomy getInstance() {
        return plugin;
    }

    public static Data getData() {

        if (data == null) {
            data = new Data();
            new Data();
        }
        return data;
    }

    private boolean setupEconomy() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }


    public static String getVersion() {
        return Bukkit.getBukkitVersion();
    }

    public Economy getEconomy() {
        return economy;
    }

    public void loadItem () {
        base = new ItemStack(Material.getMaterial(BoostEconomy.getInstance().getConfig().getString("Banknotes.Material")), 1, (short) getConfig().getInt("Banknotes.Data"));
        ItemMeta meta = base.getItemMeta();
        meta.setDisplayName(colorMessage(getConfig().getString("Banknotes.Name", "&9Banknote")));
        base.setItemMeta(meta);

        // Load the base lore
        baseLore = getConfig().getStringList("Banknotes.Lore");
    }

    public ItemStack createBanknote(String creatorName, double amount) {
        loadItem();
        if (creatorName.equals("CONSOLE")) {
            creatorName = getConfig().getString("Banknotes.Console-Name");
        }
        List<String> formatLore = new ArrayList<String>();

        // Format the base lore
        for (String baseLore : this.baseLore) {
            formatLore.add(colorMessage(baseLore.replace("%money%", "" + amount).replace("%player%", creatorName)));
        }

        // Add the base lore to the item
        ItemStack ret = base.clone();
        ItemMeta meta = ret.getItemMeta();
        meta.setLore(formatLore);
        ret.setItemMeta(meta);

        return ret;
    }

    /**
     * Returns whether an ItemStack is a banknote
     *
     * @param itemstack The item that may or may not be a note
     * @return True if the item represents a note, false otherwise
     */
    public boolean isBanknote(ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else if (itemstack.getType() == Material.AIR) {
            return false;
        } else if (!(itemstack.getItemMeta().hasDisplayName())) {
            return false;
        } else if (!(itemstack.getItemMeta().hasLore())) {
            return false;
        } else if (!(itemstack.getItemMeta().getDisplayName().equals(getConfig().getString("Banknotes.Name").replaceAll("&", "§")))) {
            return false;
        } else return itemstack.getType().equals(Material.getMaterial(BoostEconomy.getInstance().getConfig().getString("Banknotes.Material")));
    }

    /**
     * Returns the amount of money that the banknote holds
     *
     * @param itemstack The banknote
     * @return The amount of money that the note holds, 0 if the
     * item isn't a note
     */

    public double getBanknoteAmount(ItemStack itemstack) {
        if (itemstack.getItemMeta().hasDisplayName()) {
            String display = itemstack.getItemMeta().getDisplayName();
            List<String> lore = itemstack.getItemMeta().getLore();

            if (display.equals(this.getMessage("Banknotes.Name").replaceAll("&", "§"))) {
                for (String money : lore) {
                    Matcher matcher = MONEY_PATTERN.matcher(money);

                    if (matcher.find()) {
                        String amount = matcher.group(1);
                        return Double.parseDouble(amount.replaceAll(",", ""));
                    }
                }
            }
        }
        return 0;
    }

    public boolean isLegacy () {
        if (BoostEconomy.getVersion().contains("1.13")
                || BoostEconomy.getVersion().contains("1.14")
                || BoostEconomy.getVersion().contains("1.15")
                || BoostEconomy.getVersion().contains("1.16")) {
            return false;
        } else {
            return true;
        }
    }
}