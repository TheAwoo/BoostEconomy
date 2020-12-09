package boostdevteam.boosteconomy;

import boostdevteam.commands.BE;
import boostdevteam.commands.Eco;
import boostdevteam.commands.Money;
import boostdevteam.commands.Pay;
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

        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError while reloading the plugin!");
            e.printStackTrace();
        }finally {
            Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §aPlugin reloaded with success!");
        }

    }

    @Override
    public void onLoad() {
        // Plugin load logic

        Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §eLoading!");

        saveDefaultConfig();

    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
        }else {
            try {
                int pluginId = 9572;
                @SuppressWarnings("unused")
                MetricsLite metrics = new MetricsLite(this, pluginId);
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError with metrics!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §aMetrics loaded with success!");
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
                    Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError hooking with Vault!");
                    e.printStackTrace();
                } finally {
                    Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §aHooked successfully with Vault!");
                }


                Bukkit.getPluginManager().registerEvents(new PluginListener(), this);


                getCommand("money").setExecutor(new Money());
                getCommand("eco").setExecutor(new Eco());
                getCommand("be").setExecutor(new BE());
                getCommand("pay").setExecutor(new Pay());
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cUnexpected error!");
                e.printStackTrace();
            }finally {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §aLoaded with success!");
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cDisabled due to no Vault dependency found!");
        } else {
            try {
                hook.offHook();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError removing hook from Vault!");
                e.printStackTrace();
            } finally {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §aUnhooked successfully from Vault!");
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cPlugin disabled with success!");
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
