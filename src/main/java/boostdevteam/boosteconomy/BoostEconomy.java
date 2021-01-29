package boostdevteam.boosteconomy;

import boostdevteam.commands.*;
import boostdevteam.events.PlayerJoinEvent;
import boostdevteam.events.PluginListener;
import boostdevteam.tabcompleter.*;
import boostdevteam.vaultapi.VEconomy;
import boostdevteam.vaultapi.VHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BoostEconomy extends JavaPlugin implements Listener {

    public static BoostEconomy plugin;
    public static Data data;

    public static VEconomy veco;
    public static VHook hook;

    private final Pattern MONEY_PATTERN = Pattern.compile("((([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?\\d?\\d?)?$)");

    //The base item
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

    public static void onReload() {
        try {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.getServer().getPluginManager().enablePlugin(plugin);
            plugin.reloadConfig();
            plugin.saveConfig();
            plugin.saveDefaultConfig();
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError while reloading the plugin!");
            e.printStackTrace();
        }finally {
            Bukkit.getConsoleSender().sendMessage("§7[BoostEconomy§7] §aPlugin reloaded with success!");
        }
    }

    public static void playErrorSound (Player player) {
        if (getInstance().getConfig().getBoolean("Config.UseSounds")) {
            try {
                Sound x = Sound.valueOf(getInstance().getConfig().getString("Config.Sounds.Error"));
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
                Sound x = Sound.valueOf(getInstance().getConfig().getString("Config.Sounds.Success"));
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

    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
        }else {
            if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BoostEconomy.plugin, new Runnable() {
                    public void run() {
                        if (BoostEconomy.getInstance().getConfig().getBoolean("Config.CheckForUpdates.Console")) {
                            new UpdateChecker(plugin, 86591).getVersion(version -> {
                                if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aNo new version available!");
                                } else {
                                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] New version available! §av" + version);
                                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] You have §cv" + plugin.getDescription().getVersion());
                                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §eDownload it at https://www.spigotmc.org/resources/86591");
                                }
                            });
                        }
                    }
                }, 20);
            }else {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] You are using a server version not compatible with the updater! §c(Works with 1.12+)");
            }

            try {
                int pluginId = 9572;
                @SuppressWarnings("unused")
                MetricsLite metrics = new MetricsLite(this, pluginId);
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError with metrics!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aMetrics loaded with success!");
            }
            //
            try {

                data = new Data();

                new Data();

                try {
                    veco = new VEconomy(plugin);
                    hook = new VHook();

                    hook.onHook();
                }catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError hooking with Vault!");
                    e.printStackTrace();
                } finally {
                    Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aHooked successfully with Vault!");
                }

                BoostEconomy.getInstance().getConfig().options().header(
                        "BoostEconomy"
                ).copyDefaults(true);

                loadEvents();
                loadCommands();

            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cUnexpected error!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aLoaded with success!");
            }
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
        // OnJoin data saves
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
        // CheckForUpdates event
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        // InventoryClick Event
        Bukkit.getPluginManager().registerEvents(new Money(), this);

        getServer().getPluginManager().registerEvents(new boostdevteam.events.Banknotes(this), this);
    }

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

        if (getConfig().getBoolean("Banknotes.UseBanknotes")) {

            getCommand("withdraw").setExecutor(new Withdraw(this));
            getCommand("withdraw").setTabCompleter(new WithdrawTabCompleter());

            getCommand("deposit").setExecutor(new Deposit(this));
            getCommand("deposit").setTabCompleter(new DepositTabCompleter());

            getCommand("banknotes").setExecutor(new Banknotes(this));
            getCommand("banknotes").setTabCompleter(new BanknotesTabCompleter());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cDisabled due to no Vault dependency found!");
        } else {
            try {
                hook.offHook();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError removing hook from Vault!");
                e.printStackTrace();
            } finally {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aUnhooked successfully from Vault!");
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cPlugin disabled with success!");
            }
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
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        return true;
    }

    public static String getVersion() {
        return Bukkit.getBukkitVersion();
    }

    public Economy getEconomy() {
        return economy;
    }

    public void loadItem () {
        base = new ItemStack(Material.getMaterial(getConfig().getString("Banknotes.Material", "PAPER")), 1, (short) getConfig().getInt("Banknotes.Data"));
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
        if (itemstack.getType() == base.getType() && itemstack.getDurability() == base.getDurability()
                && itemstack.getItemMeta().hasDisplayName() && itemstack.getItemMeta().hasLore()) {
            String display = itemstack.getItemMeta().getDisplayName();
            List<String> lore = itemstack.getItemMeta().getLore();

            // The size thing for the lore is a bit ghetto
            return display.equals(this.getMessage("Banknotes.Name")) && lore.size() == getConfig().getStringList("Banknotes.Lore").size();
        }
        return false;
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