package boostdevteam.commands;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalTop implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("baltop")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("boosteconomy.baltop")) {
                    p.sendMessage("§8§m§l+------------------------+");
                    p.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.BalTop.Header").replaceAll("&", "§"));
                    p.sendMessage("§8§m§l+------------------------+");

                    Data data = BoostEconomy.getData();

                    int page = 1;
                    int pageSize = 10;
                    int start = (page - 1) * pageSize;

                    for ( int i = start; i < data.getBalTop().size() && i < start + pageSize; i++ ) {
                        Data.BoostPlayerData pData = data.getBalTop().get(i);
                        FileConfiguration config = BoostEconomy.getInstance().getConfig();

                        String name = pData.getName();
                        double money = pData.getMoney();

                        p.sendMessage(config.getString("Messages.BalTop.PlayerFormat")
                                .replaceAll("&", "§")
                                .replaceAll("%number%", "" + (i + 1))
                                .replaceAll("%player%", "" + name)
                                .replaceAll("%money%", "" + money));
                        }
                    p.sendMessage("§8§m§l+------------------------+");
                }else {
                        Player player = (Player) sender;
                        player.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoPerms").replaceAll("&", "§"));
                        if (BoostEconomy.getVersion().contains("1.13") || BoostEconomy.getVersion().contains("1.14") || BoostEconomy.getVersion().contains("1.15") || BoostEconomy.getVersion().contains("1.16")) {
                            player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                        }
                }
                return true;
            }else {
                    sender.sendMessage(BoostEconomy.getInstance().getConfig().getString("Messages.General.NoConsole").replaceAll("&", "§"));
                }
            }
        return false;
    }
}
