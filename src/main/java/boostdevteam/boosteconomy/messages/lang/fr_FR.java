package boostdevteam.boosteconomy.messages.lang;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class fr_FR {
    private FileConfiguration configuration;

    public static final File fr_FR = new File(BoostEconomy.getInstance().getDataFolder() + "/Messages/fr_FR.yml");
    public static FileConfiguration file;

    public static void createFile() {
        if (!fr_FR.exists()) {
            try {
                fr_FR.createNewFile();

                file = YamlConfiguration.loadConfiguration(fr_FR);
                file.options().header("File for the \"fr_FR\" messages\nLanguage: French");
                file.createSection("Messages");

                // Creates the entries
                file.set("Messages.General.NoPerms", "&b&lBoostEconomy &8» &cVous n'avez pas assez d'autorisations!");
                file.set("Messages.General.NoConsole", "&b&lBoostEconomy &8» &cVous ne pouvez pas faire cela depuis la console!");
                file.set("Messages.General.NoPlayer", "&b&lBoostEconomy &8» &cNon puoi farlo da giocatore!");
                file.set("Messages.General.Reload", "&b&lBoostEconomy &8» &aPlugin rechargé avec succès! (%time%ms)");
                file.set("Messages.General.InvalidArgs", "&b&lBoostEconomy &8» &7Arguments non valides! /be <reload/help/checkforupdates/debug/discord>");

                file.set("Messages.Money.Chat", "&b&lDe l'argent &8» &7Tu as &a%money%$");
                file.set("Messages.Money.Done", "&b&lDe l'argent &8» &aFini!");
                file.set("Messages.Money.Refreshed", "&b&lDe l'argent &8» &7Votre solde a changé, maintenant vous avez &a%money%$");

                file.set("Messages.Money.Pay.Send", "&b&lDe l'argent &8» &7Vous en avez envoyé &c%money%$ &7à &c%target%");
                file.set("Messages.Money.Pay.Received", "&b&lDe l'argent &8» &7Vous avez reçu &c%money%$ &7de &c%player%");

                file.set("Messages.Money.InvalidArgs.Pay", "&b&lDe l'argent &8» &7Arguments non valides! /Payer <joueur> <de l'argent>");
                file.set("Messages.Money.InvalidArgs.Eco", "&b&lDe l'argent &8» &7Arguments non valides! /eco <giocatore> <set/take/give> <de l'argent>");
                file.set("Messages.Money.InvalidArgs.Reset", "&b&lDe l'argent &8» &7Arguments non valides! /ecoreset <joueur>");

                file.set("Messages.Money.PlayerNotFound", "&b&lDe l'argent &8» &cJoueur non trouvé!");
                file.set("Messages.Money.NoMoney", "&b&lDe l'argent &8» &cVous n'avez pas assez d'argent!");
                file.set("Messages.Money.Others", "&b&lDe l'argent &8» &a%target% &7a &a%money%$");
                file.set("Messages.Money.PayYourself", "&b&lDe l'argent &8» &cTu ne peux pas te payer!");
                file.set("Messages.Money.Resetted", "&b&lDe l'argent &8» &7Votre argent a été restitué à &c%money%$");

                file.set("Messages.BalTop.Header", "&c&lTop 10 bilans");
                file.set("Messages.Baltop.PlayerFormat", "&6%number%) &f&l%player%: &c%money%$");

                file.set("Messages.Banknotes.Note-Redeemed", "&b&lChèques &8» &7Le billet pour &a%money%$ &7a été utilisé");
                file.set("Messages.Banknotes.Invalid-Number", "&b&lChèques &8» &cLe numéro que vous avez inséré n'est pas valide!");
                file.set("Messages.Banknotes.Less-Than-Minimum", "&b&lChèques &8» &cLe nombre que vous avez inséré est inférieur que le minimum! (%min%)");
                file.set("Messages.Banknotes.More-Than-Maximum", "&b&lChèques &8» &cLe nombre que vous avez inséré est plus grand que le maximum! (%max%)");
                file.set("Messages.Banknotes.Insufficient-Funds", "&b&lChèques &8» &eTu n'as pas assez d'argent!");
                file.set("Messages.Banknotes.Inventory-Full", "&b&lChèques &8» &cVotre inventaire est plein!");
                file.set("Messages.Banknotes.Note-Created", "&b&lChèques &8» &7Vous avez créé avec succès un billet de &a%money%$!");
                file.set("Messages.Banknotes.Player-Not-Found", "&b&lChèques &8» &cJoueur non trouvé!");
                file.set("Messages.Banknotes.Note-Received", "&b&lChèques &8» &7Vous avez reçu un billet de &a%money%$&7 par &c%player%");
                file.set("Messages.Banknotes.Note-Given", "&b&lChèques &8» &7Vous avez donné un billet de &c%money%$&7 à &a%player%");
                file.set("Messages.Banknotes.Invalid-Note", "&b&lChèques &8» &cLe billet n'est pas valide");
                file.set("Messages.Banknotes.Nothing-In-Hand", "&b&lChèques &8» &cTu n'as rien dans ta main!");

                file.set("Messages.Reward.KillMessage", "&b&lRécompense &8» &7Tuer des &c%mob% &7vous a valu &a%money%$");

                file.save(fr_FR);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError on using the file fr_FR.yml! Is it missing?");
                e.printStackTrace();
            }
            return;
        }
        file = YamlConfiguration.loadConfiguration(fr_FR);
    }
}
