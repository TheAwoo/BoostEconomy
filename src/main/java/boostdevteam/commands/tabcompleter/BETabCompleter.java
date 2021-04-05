package boostdevteam.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class BETabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int i = (args.length);
        switch (i) {
            case 1: {
                List<String> list = Arrays.asList("reload", "help", "checkforupdates", "debug", "discord", "save");
                return list;
                }
            default: {
                List<String> listDefault = Arrays.asList("");
                return listDefault;
            }
        }
    }
}
