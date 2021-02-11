package boostdevteam.boosteconomy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MobFile {
    public static final File FileData = new File(BoostEconomy.getInstance().getDataFolder() + "/mobs.yml");
    public FileConfiguration data;

    public void saveMob() {
        this.data.options().header("To disable a mob just copy the layout like these");
        this.data.set("Mobs.PLAYER.Enabled", true);
        this.data.set("Mobs.PLAYER.Reward", 100.0);

        this.data.set("Mobs.ZOMBIE.Enabled", true);
        this.data.set("Mobs.ZOMBIE.Reward", 50.0);

        this.data.set("Mobs.CREEPER.Enabled", true);
        this.data.set("Mobs.CREEPER.Reward", 50.0);

        this.data.set("Mobs.SKELETON.Enabled", true);
        this.data.set("Mobs.SKELETON.Reward", 50.0);

        this.data.set("Mobs.SPIDER.Enabled", true);
        this.data.set("Mobs.SPIDER.Reward", 50.0);
    }

    public MobFile() {
        if (!FileData.exists()) {
            try {
                FileData.createNewFile();
                this.data = YamlConfiguration.loadConfiguration(FileData);
                this.data.createSection("Mobs");
                saveMob();
                this.data.save(FileData);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError on creating the file mobs.yml!");
                e.printStackTrace();
            }
            return;
        }
        this.data = YamlConfiguration.loadConfiguration(FileData);
    }
}
