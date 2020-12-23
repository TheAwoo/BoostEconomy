package boostdevteam.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EcoTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int i = (args.length);
        switch (i) {
            case 1: {
                List<String> playerNames = Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
                return playerNames;
            }
            case 2: {
                List<String> listUse = Arrays.asList("set", "give", "take");
                return listUse;
            }
            case 3: {
                List<String> listMoney = Arrays.asList("0", "100", "1000");
                return listMoney;
            }
            default:
                List<String> listDefault = Arrays.asList("");
                return listDefault;
        }
    }
}
