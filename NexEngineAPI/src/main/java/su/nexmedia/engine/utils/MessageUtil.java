package su.nexmedia.engine.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageUtil {

    public static void sendCustom(@NotNull CommandSender sender, @NotNull String message) {
        MessageUtil.sendMessage(sender, message);
    }

    public static void playSound(@NotNull Audience audience, @Nullable Sound.Type soundType) {
        if (soundType != null) {
            audience.playSound(Sound.sound(soundType, Sound.Source.MASTER, .9f, .9f));
        }
    }

    public static void playSound(@NotNull Location location, @Nullable Sound.Type soundType) {
        World world = location.getWorld();
        if (world != null && soundType != null) {
            world.playSound(Sound.sound(soundType, Sound.Source.MASTER, .9f, .9f));
        }
    }

    public static void sendActionBar(@NotNull Audience audience, @NotNull String message) {
        audience.sendActionBar(ComponentUtil.asComponent(message));
    }

    public static void sendMessage(@NotNull Audience audience, String message) {
        audience.sendMessage(ComponentUtil.asComponent(message));
    }

    public static void showTitle(@NotNull Audience audience, @NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut) {
        audience.showTitle(Title.title(
            ComponentUtil.asComponent(title),
            ComponentUtil.asComponent(subTitle),
            Title.Times.times(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut))
        ));
    }

    public static void showTitle(@NotNull Audience audience, @NotNull String title, @NotNull String subTitle) {
        audience.showTitle(Title.title(
            ComponentUtil.asComponent(title),
            ComponentUtil.asComponent(subTitle)
        ));
    }

    /* Methods below are all deprecated json wrappers */
}
