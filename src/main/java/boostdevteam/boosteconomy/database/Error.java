package boostdevteam.boosteconomy.database;

import boostdevteam.boosteconomy.BoostEconomy;

import java.util.logging.Level;

public class Error {
    public static void execute(BoostEconomy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(BoostEconomy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
