package boostdevteam.boosteconomy.messages.lang;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class it_IT {
    private FileConfiguration configuration;

    public static final File it_IT = new File(BoostEconomy.getInstance().getDataFolder() + "/Messages/it_IT.yml");
    public static FileConfiguration file;

    public static void createFile() {
        if (!it_IT.exists()) {
            try {
                it_IT.createNewFile();

                file = YamlConfiguration.loadConfiguration(it_IT);
                file.options().header("File for the \"it_IT\" messages\nLanguage: Italian");
                file.createSection("Messages");

                // Creates the entries
                file.set("Messages.General.NoPerms", "&b&lBoostEconomy &8» &cNon hai abbastanza permessi!");
                file.set("Messages.General.NoConsole", "&b&lBoostEconomy &8» &cNon puoi farlo da console!");
                file.set("Messages.General.NoPlayer", "&b&lBoostEconomy &8» &cNon puoi farlo da giocatore!");
                file.set("Messages.General.Reload", "&b&lBoostEconomy &8» &aPlugin ricaricato con successo! (%time%ms)");
                file.set("Messages.General.InvalidArgs", "&b&lBoostEconomy &8» &7Argomenti invalidi! /be <reload/help/checkforupdates/debug/discord>");

                file.set("Messages.Money.Chat", "&b&lSoldi &8» &7Hai &a%money%$");
                file.set("Messages.Money.Done", "&b&lSoldi &8» &aFatto!");
                file.set("Messages.Money.Refreshed", "&b&lSoldi &8» &7Il tuo bilancio è cambiato, ora hai &a%money%$");

                file.set("Messages.Money.Pay.Send", "&b&lSoldi &8» &7Hai mandato &c%money%$ &7a &c%target%");
                file.set("Messages.Money.Pay.Received", "&b&lSoldi &8» &7Hai ricevuto &c%money%$ &7da &c%player%");

                file.set("Messages.Money.InvalidArgs.Pay", "&b&lSoldi &8» &7Argomenti invalidi! /pay <giocatore> <soldi>");
                file.set("Messages.Money.InvalidArgs.Eco", "&b&lSoldi &8» &7Argomenti invalidi! /eco <giocatore> <set/take/give> <soldi>");
                file.set("Messages.Money.InvalidArgs.Reset", "&b&lSoldi &8» &7Argomenti invalidi! /ecoreset <giocatore>");

                file.set("Messages.Money.PlayerNotFound", "&b&lSoldi &8» &cGiocatore non trovato!");
                file.set("Messages.Money.NoMoney", "&b&lSoldi &8» &cNon hai abbastanza soldi!");
                file.set("Messages.Money.Others", "&b&lSoldi &8» &a%target% &7ha &a%money%$");
                file.set("Messages.Money.PayYourself", "&b&lSoldi &8» &cNon puoi pagare te stesso!");
                file.set("Messages.Money.Resetted", "&b&lSoldi &8» &7I tuoi soldi sono stati ripristinati a &c%money%$");

                file.set("Messages.BalTop.Header", "&c&lTop 10 bilanci");
                file.set("Messages.BalTop.PlayerFormat", "&6%number%) &f&l%player%: &c%money%$");

                file.set("Messages.Banknotes.Note-Redeemed", "&b&lAssegni &8» &7Assegno di &a%money%$ &7è stato preso");
                file.set("Messages.Banknotes.Invalid-Number", "&b&lAssegni &8» &cIl numero che hai inserito è invalido!");
                file.set("Messages.Banknotes.Less-Than-Minimum", "&b&lAssegni &8» &cIl numero che hai inserito è minore del minimo! (%min%)");
                file.set("Messages.Banknotes.More-Than-Maximum", "&b&lAssegni &8» &cIl numero che hai inserito è maggiore del massimo! (%max%)");
                file.set("Messages.Banknotes.Insufficient-Funds", "&b&lAssegni &8» &cNon hai abbastanza soldi!");
                file.set("Messages.Banknotes.Inventory-Full", "&b&lAssegni &8» &cHai l'inventario pieno!");
                file.set("Messages.Banknotes.Note-Created", "&b&lAssegni &8» &7Hai creato un assegno di &a%money%$!");
                file.set("Messages.Banknotes.Player-Not-Found", "&b&lAssegni &8» &cGiocatore non trovato!");
                file.set("Messages.Banknotes.Note-Received", "&b&lAssegni &8» &7Hai ricevuto un assegno di &a%money%$&7 da &c%player%");
                file.set("Messages.Banknotes.Note-Given", "&b&lAssegni &8» &7Hai dato un assegno di &c%money%$&7 a &a%player%");
                file.set("Messages.Banknotes.Invalid-Note", "&b&lAssegni &8» &cAssegno non valido");
                file.set("Messages.Banknotes.Nothing-In-Hand", "&b&lAssegni &8» &cNon hai niente in mano!");

                file.set("Messages.Reward.KillMessage", "&b&lRicompensa &8» &7Uccidendo &c%mob% &7hai guadagnato &a%money%$");

                file.save(it_IT);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError on using the file it_IT.yml! Is it missing?");
                e.printStackTrace();
            }
            return;
        }
        file = YamlConfiguration.loadConfiguration(it_IT);
    }
}
