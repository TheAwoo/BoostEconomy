package boostdevteam.boosteconomy.messages.lang;

import boostdevteam.boosteconomy.BoostEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class es_ES {
    private FileConfiguration configuration;

    public static final File es_ES = new File(BoostEconomy.getInstance().getDataFolder() + "/Messages/es_ES.yml");
    public static FileConfiguration file;

    public static void createFile() {
        if (!es_ES.exists()) {
            try {
                es_ES.createNewFile();

                file = YamlConfiguration.loadConfiguration(es_ES);
                file.options().header("File for the \"fr_FR\" messages\nLanguage: Spanish");
                file.createSection("Messages");

                // Creates the entries
                file.set("Messages.General.NoPerms", "&b&lBoostEconomy &8» &c¡No tienes permisos suficientes!");
                file.set("Messages.General.NoConsole", "&b&lBoostEconomy &8» &c¡No puedes hacer esto desde la consola!");
                file.set("Messages.General.NoPlayer", "&b&lBoostEconomy &8» &c¡No puedes hacerlo como jugador!");
                file.set("Messages.General.Reload", "&b&lBoostEconomy &8» &a¡El complemento se ha recargado correctamente! (%time%ms)");
                file.set("Messages.General.InvalidArgs", "&b&lBoostEconomy &8» &7¡Argumentos inválidos! /be <reload/help/checkforupdates/debug/discord>");

                file.set("Messages.Money.Chat", "&b&lDinero &8» &7Tienes &a%money%$");
                file.set("Messages.Money.Done", "&b&lDinero &8» &a¡hecho!");
                file.set("Messages.Money.Refreshed", "&b&lDinero &8» &7Tu saldo ha cambiado, ahora tienes &a%money%$");

                file.set("Messages.Money.Pay.Send", "&b&lDinero &8» &7Enviaste &c%money%$ &7a &c%target%");
                file.set("Messages.Money.Pay.Received", "&b&lDinero &8» &7Recibiste &c%money%$ &7de &c%player%");

                file.set("Messages.Money.InvalidArgs.Pay", "&b&lDinero &8» &7¡Argumentos inválidos! /pay <jugador> <dinero>");
                file.set("Messages.Money.InvalidArgs.Eco", "&b&lDinero &8» &7¡Argumentos inválidos! /eco <Jugador> <set/take/give> <dinero>");
                file.set("Messages.Money.InvalidArgs.Reset", "&b&lDinero &8» &7¡Argumentos inválidos! /ecoreset <jugador>");

                file.set("Messages.Money.PlayerNotFound", "&b&lDinero &8» &c¡Jugador no encontrado!");
                file.set("Messages.Money.NoMoney", "&b&lDinero &8» &c¡no tienes suficiente dinero!");
                file.set("Messages.Money.Others", "&b&lDinero &8» &a%target% &7tiene &a%money%$");
                file.set("Messages.Money.PayYourself", "&b&lDinero &8» &c¡No puedes pagarte a ti mismo!");
                file.set("Messages.Money.Resetted", "&b&lDinero &8» &7Tu dinero se ha restablecido a  &c%money%$");

                file.set("Messages.BalTop.Header", "&c&lTop 10 balances");
                file.set("Messages.BalTop.PlayerFormat", "&6%number%) &f&l%player%: &c%money%$");

                file.set("Messages.Banknotes.Note-Redeemed", "&b&lCheques &8» &7Billete de &a%money%$ &7ha sido redimido");
                file.set("Messages.Banknotes.Invalid-Number", "&b&lCheques &8» &c¡El número que ingresó no es válido!");
                file.set("Messages.Banknotes.Less-Than-Minimum", "&b&lCheques &8» &cT¡El número que ingresó es menor que el mínimo! (%min%)");
                file.set("Messages.Banknotes.More-Than-Maximum", "&b&lCheques &8» &c¡El número que ingresó es menor que el màximo! (%max%)");
                file.set("Messages.Banknotes.Insufficient-Funds", "&b&lCheques &8» &e¡No tienes suficiente dinero!");
                file.set("Messages.Banknotes.Inventory-Full", "&b&lCheques &8» &c¡Tu inventario está lleno!");
                file.set("Messages.Banknotes.Note-Created", "&b&lCheques &8» &7¡Ha creado con éxito un billete de &a%money%$!");
                file.set("Messages.Banknotes.Player-Not-Found", "&b&lCheques &8» &c¡jugador no encontrado!");
                file.set("Messages.Banknotes.Note-Received", "&b&lCheques &8» &7Recibiste una factura de &a%money%$&7 de &c%player%");
                file.set("Messages.Banknotes.Note-Given", "&b&lCheques &8» &7Regalaste billetes de &c%money%$&7 a &a%player%");
                file.set("Messages.Banknotes.Invalid-Note", "&b&lCheques &8» &cl Ebillete no es válido");
                file.set("Messages.Banknotes.Nothing-In-Hand", "&b&lCheques &8» &cno tienes nada en tu mano!");

                file.set("Messages.Reward.KillMessage", "&b&lRecompensa &8» &7Matando &c%mob% &7ganaste  &a%money%$");

                file.save(es_ES);
            }catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("[BoostEconomy] §cError on using the file fr_FR.yml! Is it missing?");
                e.printStackTrace();
            }
            return;
        }
        file = YamlConfiguration.loadConfiguration(es_ES);
    }
}
