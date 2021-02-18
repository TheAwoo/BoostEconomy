package boostdevteam.boosteconomy;

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

            for (EntityType e : EntityType.values()) {
                if (e.equals(DROPPED_ITEM)) {
                } else if (e.equals(EXPERIENCE_ORB)) {
                } else if (e.equals(AREA_EFFECT_CLOUD)) {
                } else if (e.equals(UNKNOWN)) {
                } else if (e.equals(LIGHTNING)) {
                } else if (e.equals(FISHING_HOOK)) {
                } else if (e.equals(ENDER_CRYSTAL)) {
                } else if (e.equals(LLAMA_SPIT)) {
                } else if (e.equals(MINECART)) {
                } else if (e.equals(MINECART_CHEST)) {
                } else if (e.equals(MINECART_COMMAND)) {
                } else if (e.equals(MINECART_FURNACE)) {
                } else if (e.equals(MINECART_HOPPER)) {
                } else if (e.equals(MINECART_MOB_SPAWNER)) {
                } else if (e.equals(MINECART_TNT)) {
                } else if (e.equals(BOAT)) {
                } else if (e.equals(FALLING_BLOCK)) {
                } else if (e.equals(PRIMED_TNT)) {
                } else if (e.equals(WITHER_SKULL)) {
                } else if (e.equals(ITEM_FRAME)) {
                } else if (e.equals(THROWN_EXP_BOTTLE)) {
                } else if (e.equals(SPLASH_POTION)) {
                } else if (e.equals(ENDER_SIGNAL)) {
                } else if (e.equals(ENDER_PEARL)) {
                } else if (e.equals(SMALL_FIREBALL)) {
                } else if (e.equals(FIREBALL)) {
                } else if (e.equals(SNOWBALL)) {
                } else if (e.equals(ARROW)) {
                } else if (e.equals(PAINTING)) {
                } else if (e.equals(LEASH_HITCH)) {
                } else if (e.equals(EGG)) {
                } else if (e.equals(DROPPED_ITEM)) {
                } else {
                    this.mobData.set("Mobs." + e + ".Enabled", true);
                    this.mobData.set("Mobs." + e + ".Reward", 100.0);
                }
            }
        }

        if (BoostEconomy.getVersion().contains("1.13")
                || BoostEconomy.getVersion().contains("1.14")
                || BoostEconomy.getVersion().contains("1.15")
                || BoostEconomy.getVersion().contains("1.16")) {

            this.mobData.options().header("Because you are in 1.13+ version there's a pre-made list of all available entities:");

            for (EntityType e : EntityType.values()) {
                if (e.equals(DROPPED_ITEM)) {
                } else if (e.equals(EXPERIENCE_ORB)) {
                } else if (e.equals(AREA_EFFECT_CLOUD)) {
                } else if (e.equals(UNKNOWN)) {
                } else if (e.equals(LIGHTNING)) {
                } else if (e.equals(FISHING_HOOK)) {
                } else if (e.equals(ENDER_CRYSTAL)) {
                } else if (e.equals(LLAMA_SPIT)) {
                } else if (e.equals(MINECART)) {
                } else if (e.equals(MINECART_CHEST)) {
                } else if (e.equals(MINECART_COMMAND)) {
                } else if (e.equals(MINECART_FURNACE)) {
                } else if (e.equals(MINECART_HOPPER)) {
                } else if (e.equals(MINECART_MOB_SPAWNER)) {
                } else if (e.equals(MINECART_TNT)) {
                } else if (e.equals(BOAT)) {
                } else if (e.equals(FALLING_BLOCK)) {
                } else if (e.equals(PRIMED_TNT)) {
                } else if (e.equals(WITHER_SKULL)) {
                } else if (e.equals(ITEM_FRAME)) {
                } else if (e.equals(THROWN_EXP_BOTTLE)) {
                } else if (e.equals(SPLASH_POTION)) {
                } else if (e.equals(ENDER_SIGNAL)) {
                } else if (e.equals(ENDER_PEARL)) {
                } else if (e.equals(SMALL_FIREBALL)) {
                } else if (e.equals(FIREBALL)) {
                } else if (e.equals(SNOWBALL)) {
                } else if (e.equals(ARROW)) {
                } else if (e.equals(PAINTING)) {
                } else if (e.equals(LEASH_HITCH)) {
                } else if (e.equals(EGG)) {
                } else if (e.equals(DROPPED_ITEM)) {
                } else {
                    this.mobData.set("Mobs." + e + ".Enabled", true);
                    this.mobData.set("Mobs." + e + ".Reward", 100.0);
                }
            }
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
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bBoostEconomy§7] §cError on creating the file mobs.yml!");
                e.printStackTrace();
            }
            return;
        }
        this.mobData = YamlConfiguration.loadConfiguration(MobFileData);
    }
}
