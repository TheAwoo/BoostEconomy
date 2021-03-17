package boostdevteam.boosteconomy.files;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;

import static org.bukkit.entity.EntityType.*;

public class MobFile {
    public static final File MobFileData = new File(BoostEconomy.getInstance().getDataFolder() + "/mobs.yml");
    public FileConfiguration mobData;

    public void saveMob() throws IOException {

        if (BoostEconomy.getVersion().contains("1.13")
                || BoostEconomy.getVersion().contains("1.14")
                || BoostEconomy.getVersion().contains("1.15")
                || BoostEconomy.getVersion().contains("1.16")) {

            this.mobData.options().header("Because you are in 1.13+ version there's a pre-made list of entity:");


        }

        if (BoostEconomy.getVersion().contains("1.13")
                || BoostEconomy.getVersion().contains("1.14")
                || BoostEconomy.getVersion().contains("1.15")
                || BoostEconomy.getVersion().contains("1.16")) {

            this.mobData.options().header("Because you are in 1.13+ version there's a pre-made list of all available entities:");


        } else {
            this.mobData.options().header("Because you are not in 1.13+ version not all entities are available\n" +
                    "There's a pre-made layout that you can copy to use more mobs!");

            this.mobData.set("Mobs." + "ZOMBIE" + ".Enabled", true);
            this.mobData.set("Mobs." + "ZOMBIE" + ".Reward", 100.0);
        }

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
