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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static boostdevteam.boosteconomy.LogFile.LogFileData;

public final class BoostEconomy extends JavaPlugin implements Listener {

    // Files
    public static BoostEconomy plugin;
    public static Data data;
    public static MobFile mob;
    public static LogFile log;
    
    // Startup variables
    private static int errors, warning = 0;
    private static boolean sounds, placeholderapi, configOutDated, essentials;

    // Banknotes value finder
    private final Pattern MONEY_PATTERN = Pattern.compile("\\$[\\d,.]*");

    // The base banknote item
    private ItemStack base;

    // Economy instance
    public Economy economy;
    public static VEconomy veco;
    public static VHook hook;
    private static Economy econ = null;

     //The base lore for the item
    private List<String> baseLore;

    // Banknotes custom color
    public String colorMessage(String message) {
        if (message == null) {
            return "null";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Banknotes custom messages
    public String getMessage(String path) {
        if (!getConfig().isString(path)) {
            return path;
        }

        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    // Reload method
    public static void onReload(CommandSender sender) {
        long before = System.currentTimeMillis();
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();

            data = new Data();
            mob = new MobFile();
            log = new LogFile();

            new LogFile();
            new Data();
            new MobFile();

        }catch (Exception e) {
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

    // Error sound played to player
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

    // Success sound played to player
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

    // First config creation
    @Override
    public void onLoad() {

        Bukkit.getConsoleSender().sendMessage("§7[BoostEconomy] §eLoading!");

        saveDefaultConfig();

        YamlConfiguration config = new YamlConfiguration();
        File file = new File(getDataFolder () + File.separator + "config.yml");
        try {
            config.loadFromString (Files.toString (file, StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException e) {
            errors++;
            e.printStackTrace ();
        }
    }

    // Controls if the version of the server is compatible with the sounds in config.yml
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
            sounds = false;
        } else {
            warning++;
            sounds = true;
            getInstance().getConfig().set("Config.UseSounds", false);
            Bukkit.getConsoleSender().sendMessage("§f-> §eThe sounds has been disabled to prevent errors, " +
                    "if you want to use the sounds you need to change that to true and set the sounds for your version!");
        }
    }

    // Plugin startup logic
    @Override
    public void onEnable() {

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
                        errors++;
                    } finally {
                        Bukkit.getConsoleSender().sendMessage("§f-> §7Hooked with §aPlaceholderAPI§7!");
                        Bukkit.getConsoleSender().sendMessage("§f-> §7Loaded placeholders!");
                    }
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("§f-> §7Could not find §cPlaceholderAPI§7 for placeholders, no placeholders will be added!");
                warning++;
                placeholderapi = true;
            }

            if (Bukkit.getPluginManager().getPlugin("Essentials") != null){
                Bukkit.getConsoleSender().sendMessage("§f");
                Bukkit.getConsoleSender().sendMessage("§f-> §7Essentials economy can cause some conflicts with BoostEconomy!");
                warning++;
                essentials = true;
            }

            try {
                int pluginId = 9572;
                @SuppressWarnings("unused")
                MetricsLite metrics = new MetricsLite(this, pluginId);
            }catch (Exception e) {
                errors++;
                Bukkit.getConsoleSender().sendMessage("§f-> §cError with metrics!");
                e.printStackTrace();
            }

            try {

                data = new Data();
                mob = new MobFile();
                log = new LogFile();

                new Data();
                new MobFile();
                new LogFile();

                try {

                    veco = new VEconomy(plugin);
                    hook = new VHook();

                    hook.onHook();

                }catch (Exception e) {
                    errors++;
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

                checkConfigVersion();

                ConsoleUpdater();

                saveLog("Plugin loaded");

            }catch (Exception e) {
                errors++;
                Bukkit.getConsoleSender().sendMessage("§f-> §cUnexpected error!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("§7");

                if (errors != 0) {
                    Bukkit.getConsoleSender().sendMessage("§cThe plugin is loaded with " + errors + " errors. (" + (System.currentTimeMillis() - before) + "ms)");
                } else {
                    Bukkit.getConsoleSender().sendMessage("§aThe plugin is loaded with no errors (" + (System.currentTimeMillis() - before) + "ms)");
                }

                if (warning != 0) {
                    Bukkit.getConsoleSender().sendMessage("§eYou have " + warning + " startup warnings!");
                    if (sounds) {
                        Bukkit.getConsoleSender().sendMessage("§e- Sounds disabled (Incompatible version)");
                    }
                    if (placeholderapi) {
                        Bukkit.getConsoleSender().sendMessage("§e- PlaceholderAPI not found");
                    }
                    if (configOutDated) {
                        Bukkit.getConsoleSender().sendMessage("§e- Outdated config.yml");
                    }
                    if (essentials) {
                        Bukkit.getConsoleSender().sendMessage("§e- Essentials economy loaded");
                    }
                }

                Bukkit.getConsoleSender().sendMessage("§8");
                Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
            }
        }
    }

    // Will check if the config.yml is outdated
    public static void checkConfigVersion () {
        if (!getInstance().getConfig().getString("Version").equals(BoostEconomy.plugin.getDescription().getVersion())) {
            Bukkit.getConsoleSender().sendMessage("§7");
            Bukkit.getConsoleSender().sendMessage("§f-> §eLook that your config.yml is outdated!");
            warning++;
            configOutDated = true;
        }
    }

    // log.txt log creation
    public static void saveLog(String text) {
        if (getInstance().getConfig().getBoolean("Config.Logs", true)) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss-dd/MM");
            Date date = new Date();
            log.logData.set("(" + dateFormat.format(date) + ")", text);
            try {
                log.logData.save(LogFileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Updater logic
    public static void ConsoleUpdater () {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BoostEconomy.plugin, () -> {
                long before = System.currentTimeMillis();
                if (Bukkit.getVersion().contains("1.12")
                        || Bukkit.getVersion().contains("1.13")
                        || Bukkit.getVersion().contains("1.14")
                        || Bukkit.getVersion().contains("1.15")
                        || Bukkit.getVersion().contains("1.16")) {
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
                }else {
                    Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                    Bukkit.getConsoleSender().sendMessage("            §bBoostEconomy");
                    Bukkit.getConsoleSender().sendMessage("               §eUpdater");
                    Bukkit.getConsoleSender().sendMessage("§8");
                    Bukkit.getConsoleSender().sendMessage("§f-> You are using a server version not compatible with the updater! §c(Works with 1.12+)");
                    Bukkit.getConsoleSender().sendMessage("§8");
                    Bukkit.getConsoleSender().sendMessage("§8+------------------------------------+");
                    errors++;
                }
            }, 40);
    }

    // Simply load the events
    public void loadEvents() {
        // OnJoin and OnQuit data saves
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
        // CheckForUpdates event
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        // InventoryClick Event
        Bukkit.getPluginManager().registerEvents(new Money(), this);
        // Banknotes claim event
        Bukkit.getPluginManager().registerEvents(new boostdevteam.events.Banknotes(plugin), this);
        // Entity kill reward
        Bukkit.getPluginManager().registerEvents(new MobKillEvent(plugin), this);
    }

    // Simply load the commands
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

    // Plugin shutdown logic
    @Override
    public void onDisable() {
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

        saveLog("Plugin unloaded");

    }

    // Main instance of this class
    public static BoostEconomy getInstance() {
        return plugin;
    }

    // Data for players and baltop
    public static Data getData() {

        if (data == null) {
            data = new Data();
            new Data();
        }
        return data;
    }

    // Control if Vault is installed
    private boolean setupEconomy() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }

    // Vault economy instance
    public static Economy getEconomy() {
        if (econ == null) {
            return null;
        }
        return econ;
    }

    // Just renamed a method
    public static String getVersion() {
        return Bukkit.getBukkitVersion();
    }

    // Load base item for banknotes
    public void loadItem () {
        base = new ItemStack(Material.getMaterial(BoostEconomy.getInstance().getConfig().getString("Banknotes.Material")), 1, (short) getConfig().getInt("Banknotes.Data"));
        ItemMeta meta = base.getItemMeta();
        meta.setDisplayName(colorMessage(getConfig().getString("Banknotes.Name", "&9Banknote")));
        base.setItemMeta(meta);

        // Load the base lore
        baseLore = getConfig().getStringList("Banknotes.Lore");
    }

    // Create a banknotes with all custom entries
    public ItemStack createBanknote(String creatorName, long amount) {
        loadItem();
        if (creatorName.equals("CONSOLE")) {
            creatorName = getConfig().getString("Banknotes.Console-Name");
        }
        List<String> formatLore = new ArrayList<String>();

        // Format the base lore
        for (String baseLore : this.baseLore) {
            formatLore.add(colorMessage(baseLore.replace("%money%", "$" + amount).replace("%player%", creatorName)));
        }

        // Add the base lore to the item
        ItemStack ret = base.clone();
        ItemMeta meta = ret.getItemMeta();
        meta.setLore(formatLore);
        ret.setItemMeta(meta);

        return ret;
    }

    // Return if an item is a banknote
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


    // Get the value of a banknote
    public double getBanknoteAmount(ItemStack itemstack) {
        if (itemstack.getItemMeta().hasDisplayName()) {
            String display = itemstack.getItemMeta().getDisplayName();
            List<String> lore = itemstack.getItemMeta().getLore();

            if (display.equals(this.getMessage("Banknotes.Name").replaceAll("&", "§"))) {
                for (String money : lore) {
                    Matcher matcher = MONEY_PATTERN.matcher(money);

                    if (matcher.find()) {
                        String amount = matcher.group();
                        String value = amount.replaceAll("\\$", "");
                        return Double.parseDouble(value);
                    }
                }
            }
        }
        return 0;
    }

    // Is the server legacy? (Under 1.13)
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