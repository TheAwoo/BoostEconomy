package boostdevteam.boosteconomy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Data {
    public static final File FileData = new File(BoostEconomy.getInstance().getDataFolder() + "/data.yml");
    public FileConfiguration data;
    public Data() {
        if (!FileData.exists()) {
            try {
                FileData.createNewFile();
                this.data = YamlConfiguration.loadConfiguration(FileData);
                this.data.createSection("Data");
                this.data.save(FileData);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError on creating the file data.yml!");
                e.printStackTrace();
            }
            return;
        }
        this.data = YamlConfiguration.loadConfiguration(FileData);
    }

    public void saveData(Player p, double money) {
            try {

                this.data.set("Data." + p.getName() + ".Money", money);

                this.data.save(FileData);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[BoostEconomy] §cError on saving the data for " + p.getName());
                e.printStackTrace();
            }
    }
    public boolean hasBalance(Player p) {
        return this.data.getString("Data." + p.getName() + ".Money") != null;
    }

    public String getValue(Player p) {
        return this.data.getString("Data." + p.getName() + ".Money");
    }
}