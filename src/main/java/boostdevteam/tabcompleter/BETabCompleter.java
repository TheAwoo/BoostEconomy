package boostdevteam.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BETabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = Arrays.asList("reload", "help");
        String input = args[0].toLowerCase();

        List<String> completions = null;
        for(String s : list) {
            if (s.startsWith(input)) {
                if (completions == null) {
                    completions = new ArrayList();
                }
                completions.add(s);
            }
        }

        if (completions != null) {
            Collections.sort(completions);
        }

        return completions;
    }
}
