package boostdevteam.boosteconomy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
public class Data
{
    public static final File FileData = new File(BoostEconomy.getInstance().getDataFolder() + "/data.yml");
    public FileConfiguration data;

    public List<BoostPlayerData> balTop;
    public Map<String,BoostPlayerData> balTopName;

    public Data() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();

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

        loadFromData();
    }

    public void saveData(Player p, double money) {
        try {

            this.data.set("Data." + p.getName() + ".Money", money);

            this.data.save(FileData);

            if ( getBalTopName().containsKey( p.getName() ) ) {
                // update the stored money:
                getBalTopName().get( p.getName() ).setMoney( money );
            }
            else {
                // New payer data: Update both collections:
                BoostPlayerData pData = new BoostPlayerData(p.getName(), money);
                getBalTop().add( pData );
                getBalTopName().put( pData.getName(), pData );
            }

            // Sort the getBalTop() List:
            Collections.sort( getBalTop(), new BoostPlayerComparator() );

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

    private void loadFromData() {
        getBalTop().clear();

        ConfigurationSection sec = this.data.getConfigurationSection( "Data" );

        Set<String> playerNames = sec.getKeys( false );

        for ( String playerName : playerNames ) {
            Double money = this.data.getDouble( "Data." + playerName + ".Money" );
            if ( money != null ) {
                BoostPlayerData pData = new BoostPlayerData(playerName, money);
                getBalTop().add( pData );
                getBalTopName().put( pData.getName(), pData );
            }
        }

        Collections.sort( getBalTop(), new BoostPlayerComparator() );
    }


    public List<BoostPlayerData> getBalTop() {
        return balTop;
    }
    public Map<String, BoostPlayerData> getBalTopName() {
        return balTopName;
    }

    public class BoostPlayerComparator
            implements Comparator<BoostPlayerData> {

        @Override
        public int compare( BoostPlayerData arg0, BoostPlayerData arg1 )
        {
            return Double.compare( arg1.getMoney(), arg0.getMoney() );
        }
    }

    public class BoostPlayerData {
        private String name;
        private double money;
        public BoostPlayerData( String name, double money ) {
            super();

            this.name = name;
            this.money = money;
        }
        public String getName() {
            return name;
        }

        public double getMoney() {
            return money;
        }
        public void setMoney( double money ) {
            this.money = money;
        }

    }
}
