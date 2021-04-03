package boostdevteam.boosteconomy.files;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MobFile {
    public static final File MobFileData = new File(BoostEconomy.getInstance().getDataFolder() + "/mobs.yml");
    public FileConfiguration mobData;

    public void saveMob() throws IOException {
            this.mobData.options().header("If you want to add more mobs just copy the format from this:");
            this.mobData.set("Mobs." + "ZOMBIE" + ".Enabled", true);
            this.mobData.set("Mobs." + "ZOMBIE" + ".Reward", 100.0);
    }

    public MobFile() {
        if (!MobFileData.exists()) {
            try {
                MobFileData.createNewFile();
                this.mobData = YamlConfiguration.loadConfiguration(MobFileData);
                this.mobData.createSection("Mobs");

                saveMob();

                this.mobData.save(MobFileData);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError on creating the file mobs.yml!");
                e.printStackTrace();
            }
            return;
        }
        this.mobData = YamlConfiguration.loadConfiguration(MobFileData);
    }
}
