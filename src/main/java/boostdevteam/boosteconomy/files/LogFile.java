package boostdevteam.boosteconomy.files;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LogFile {
    public static final File LogFileData = new File(BoostEconomy.getInstance().getDataFolder() + "/logs.txt");
    public FileConfiguration logData;

    public LogFile() {
        if (!LogFileData.exists()) {
            try {
                LogFileData.createNewFile();
                this.logData = YamlConfiguration.loadConfiguration(LogFileData);
                this.logData.options().header("In this file you can view the log of the plugin commands made by players.");

                this.logData.save(LogFileData);

            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError on creating the file logs.txt!");
                e.printStackTrace();
            }
            return;
        }
        this.logData = YamlConfiguration.loadConfiguration(LogFileData);
    }
}
