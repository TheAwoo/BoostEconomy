package boostdevteam.boosteconomy.messages;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.messages.lang.en_US;
import boostdevteam.boosteconomy.messages.lang.es_ES;
import boostdevteam.boosteconomy.messages.lang.fr_FR;
import boostdevteam.boosteconomy.messages.lang.it_IT;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Messages {

    private File file;
    private FileConfiguration configuration;

    
    public void createFolder() {
        String folderPath = BoostEconomy.getInstance().getDataFolder() + "/Messages";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            try {
                // Create the folder "Messages"
                folder.mkdirs();

                // Saves the en_US.yml file under Messages
                en_US.createFile();
                it_IT.createFile();
                fr_FR.createFile();
                es_ES.createFile();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            en_US.createFile();
            it_IT.createFile();
            fr_FR.createFile();
            es_ES.createFile();
        }
    }
}
