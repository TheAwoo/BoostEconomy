package boostdevteam.boosteconomy;

import boostdevteam.commands.BE;
import boostdevteam.commands.Eco;
import boostdevteam.commands.Money;
import boostdevteam.commands.Pay;
import boostdevteam.events.PlayerJoinEvent;
import boostdevteam.events.PluginListener;

import boostdevteam.vaultapi.VEconomy;
import boostdevteam.vaultapi.VHook;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BoostEconomy extends JavaPlugin implements Listener {

    public static BoostEconomy plugin;
    public static Data data;

    public static VEconomy veco;
    public static VHook hook;

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
                            new boostdevteam.boosteconomy.UpdateChecker(plugin, 86591).getVersion(version -> {
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
                        "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                                "To honor their work, you should not disable it.\n" +
                                "This has nearly no effect on the server performance!\n" +
                                "Check out https://bStats.org/ to learn more :)"
                ).copyDefaults(true);

                Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
                Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
                Bukkit.getPluginManager().registerEvents(new Money(), this);


                getCommand("money").setExecutor(new Money());
                getCommand("eco").setExecutor(new Eco());
                getCommand("be").setExecutor(new BE());
                getCommand("pay").setExecutor(new Pay());
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cUnexpected error!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §aLoaded with success!");
            }
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
}
