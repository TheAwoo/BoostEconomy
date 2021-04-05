package boostdevteam.boosteconomy;

import org.bukkit.OfflinePlayer;

import java.util.*;

public class Data {

    //public static final File FileData = new File(BoostEconomy.getInstance().getDataFolder() + "/data.yml");
    //public FileConfiguration data;
    private int pointer = 0;

    public List<BoostPlayerData> balTop;
    public Map<String,BoostPlayerData> balTopName;

    public Data() {
        this.balTop = new ArrayList<>();
        this.balTopName = new TreeMap<>();

        loadFromData();
    }

    // Save data and save data for the baltop
    public void saveData(OfflinePlayer p, long money) {

        BoostEconomy.getInstance().getRDatabase().setTokens(p.getName(), money);


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
        Collections.sort(getBalTop(), new BoostPlayerComparator() );

    }

    public boolean hasBalance(OfflinePlayer p) {
        return BoostEconomy.getInstance().getRDatabase().getList().contains(p.getName());
    }

    public boolean hasBalance(String s) {
        return BoostEconomy.getInstance().getRDatabase().getList().contains(s);
    }

    public long getValue(OfflinePlayer p) {
        return BoostEconomy.getInstance().getRDatabase().getTokens(p.getName());
    }

    private void loadFromData() {
        getBalTop().clear();

        for ( String playerName : BoostEconomy.getInstance().getRDatabase().getList()) {

            Double money = Double.valueOf(BoostEconomy.getInstance().getRDatabase().getTokens(playerName));

            BoostPlayerData pData = new BoostPlayerData(playerName, money);
            getBalTop().add( pData );
            getBalTopName().put( pData.getName(), pData );

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
            int results = Double.compare( arg1.getMoney(), arg0.getMoney() );
            if ( results == 0 ) {
                results = arg0.getName().compareToIgnoreCase( arg1.getName() );
            }
            return results;
        }
    }

    public class BoostPlayerData {

        private String name;
        private double money;

        public BoostPlayerData(String name, double money) {
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

        public void setMoney(double money) {
            this.money = money;
        }

    }
}
