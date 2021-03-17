package boostdevteam.boosteconomy.messages.lang;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class en_US {
    private FileConfiguration configuration;

    public static final File en_US = new File(BoostEconomy.getInstance().getDataFolder() + "/Messages/en_US.yml");
    public static FileConfiguration file;

    public static void createFile() {
        if (!en_US.exists()) {
            try {
                en_US.createNewFile();

                file = YamlConfiguration.loadConfiguration(en_US);
                file.options().header("File for the \"en_US\" messages\nLanguage: English");
                file.createSection("Messages");

                // Creates the entries
                file.set("Messages.General.NoPerms", "&b&lBoostEconomy &8» &cYou do not have enough permissions");
                file.set("Messages.General.NoConsole", "&b&lBoostEconomy &8» &cYou can't do this from console");
                file.set("Messages.General.NoPlayer", "&b&lBoostEconomy &8» &cYou can't do this from player!");
                file.set("Messages.General.Reload", "&b&lBoostEconomy &8» &aThe plugin has been reloaded with success! (%time%ms)");
                file.set("Messages.General.InvalidArgs", "&b&lBoostEconomy &8» &7Invalid args! /be <reload/help/checkforupdates/debug/discord>");

                file.set("Messages.Money.Chat", "&b&lMoney &8» &7You have &a%money%$");
                file.set("Messages.Money.Done", "&b&lMoney &8» &aDone!");
                file.set("Messages.Money.Refreshed", "&b&lMoney &8» &7Your money has been changed, now you have &a%money%$");

                file.set("Messages.Money.Pay.Send", "&b&lMoney &8» &7You have sent &c%money%$ &7to &c%target%");
                file.set("Messages.Money.Pay.Received", "&b&lMoney &8» &7You have received &c%money%$ &7from &c%player%");

                file.set("Messages.Money.InvalidArgs.Pay", "&b&lMoney &8» &7Invalid args! /pay <player> <money>");
                file.set("Messages.Money.InvalidArgs.Eco", "&b&lMoney &8» &7Invalid args! /eco <player> <set/take/give> <money>");
                file.set("Messages.Money.InvalidArgs.Reset", "&b&lMoney &8» &7Invalid args! /ecoreset <player>");

                file.set("Messages.Money.PlayerNotFound", "&b&lMoney &8» &cPlayer not found!");
                file.set("Messages.Money.NoMoney", "&b&lMoney &8» &cYou do not have enough money!");
                file.set("Messages.Money.Others", "&b&lMoney &8» &a%target% &7has &a%money%$");
                file.set("Messages.Money.PayYourself", "&b&lMoney &8» &cYou can't pay yourself!");
                file.set("Messages.Money.Resetted", "&b&lMoney &8» &7Your money has been restored to &c%money%$");

                file.set("Messages.BalTop.Header", "&c&lTop 10 balances");
                file.set("Messages.Baltop.PlayerFormat", "&6%number%) &f&l%player%: &c%money%$");

                file.set("Messages.Banknotes.Note-Redeemed", "&b&lBanknote &8» &7Banknote for &a%money%$ &7has been redeemed");
                file.set("Messages.Banknotes.Invalid-Number", "&b&lBanknote &8» &cThe number that you have inserted is invalid!");
                file.set("Messages.Banknotes.Less-Than-Minimum", "&b&lBanknote &8» &cThe number that you have inserted is lower than the minimum! (%min%)");
                file.set("Messages.Banknotes.More-Than-Maximum", "&b&lBanknote &8» &cThe number that you have inserted is bigger than the maximum! (%max%)");
                file.set("Messages.Banknotes.Insufficient-Funds", "&b&lBanknote &8» &eYou don't have enough money!");
                file.set("Messages.Banknotes.Inventory-Full", "&b&lBanknote &8» &cYour inventory is full!");
                file.set("Messages.Banknotes.Note-Created", "&b&lBanknote &8» &7You have successfully created a banknotes of &a%money%$!");
                file.set("Messages.Banknotes.Player-Not-Found", "&b&lBanknote &8» &cPlayer not found!");
                file.set("Messages.Banknotes.Note-Received", "&b&lBanknote &8» &7You have received a banknote of &a%money%$&7 by &c%player%");
                file.set("Messages.Banknotes.Note-Given", "&b&lBanknote &8» &7You have given a banknotes of &c%money%$&7 to &a%player%");
                file.set("Messages.Banknotes.Invalid-Note", "&b&lBanknote &8» &cThe banknote is invalid");
                file.set("Messages.Banknotes.Nothing-In-Hand", "&b&lBanknote &8» &cYou don't have nothing in your hand!");

                file.set("Messages.Reward.KillMessage", "&b&lReward &8» &7You've killed &c%mob% &7and you've received &a%money%$");

                file.save(en_US);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError on using the file en_US.yml! Is it missing?");
                e.printStackTrace();
            }
            return;
        }
        file = YamlConfiguration.loadConfiguration(en_US);
    }
}
