package boostdevteam.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoneyTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int i = (args.length);
        switch (i) {
            case 1: {
                for (Player pList : Bukkit.getOnlinePlayers()) {
                    List<String> list = new ArrayList<String>();
                    list.add("" + pList.getName());
                    return list;
                }
            }
            default:
                List<String> listDefault = Arrays.asList("");
                return listDefault;
        }
    }
}
