package su.nexmedia.engine.command.list;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.NexEngine;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.lang.EngineLang;
import su.nexmedia.engine.utils.MessageUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AboutSubCommand<P extends NexPlugin<P>> extends AbstractCommand<P> {

    public AboutSubCommand(@NotNull P plugin) {
        super(plugin, new String[]{"about"});
    }

    @Override
    @NotNull
    public String getUsage() {
        return "";
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getMessage(EngineLang.CORE_COMMAND_ABOUT_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        List<String> info = Arrays.asList(
            "",
            "<yellow>" + plugin.getName() + " <gold>v" + plugin.getDescription().getVersion() + "</gold> created by <gold>" + plugin.getDescription().getAuthors(),
            "<yellow>Type <gold>/" + plugin.getLabel() + " help</gold> to list plugin commands.",
            "",
            "<dark_green>Powered by <green><b>" + NexEngine.get().getName() + "</b></green>, © 2019-2022"
        );
        info.forEach(text -> MessageUtil.sendMessage(sender, text));
    }
}
