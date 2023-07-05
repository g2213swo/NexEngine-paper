package su.nexmedia.engine.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A util class to handle the legacy color stuff.
 */
public class Colorizer {
    public static final char AMPERSAND_CHAR = LegacyComponentSerializer.AMPERSAND_CHAR;

    public static @NotNull List<String> apply(@NotNull List<String> list) {
        list.replaceAll(Colorizer::legacy);
        return list;
    }

    public static @NotNull Set<String> apply(@NotNull Set<String> set) {
        return set.stream().map(Colorizer::legacy).collect(Collectors.toSet());
    }

    /**
     * Translates ampersand ({@code &}) color codes into section ({@code §}) color codes.
     * <p>
     * The translation supports three different RGB formats:
     * <ul>
     *     <li>Legacy Mojang color and formatting codes (such as §a or §l)</li>
     *     <li>Adventure-specific RGB format (such as §#a25981)</li>
     *     <li>BungeeCord RGB color code format (such as §x§a§2§5§9§8§1)</li>
     * </ul>
     *
     * @param str a legacy text where its color codes are in <b>ampersand</b> {@code &} format
     *
     * @return a legacy text where its color codes are in <b>section</b> {@code §} format
     */
    public static @NotNull String legacy(@NotNull String str) {
        return LegacyComponentSerializer.legacySection().serialize(LegacyComponentSerializer.legacy(AMPERSAND_CHAR).deserialize(str));
    }

    /**
     * Translates section ({@code §}) color codes into ampersand ({@code &}) color codes.
     * <p>
     * It's basically a reverse of {@link #legacy(String)}.
     */
    public static @NotNull String plain(@NotNull String str) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacySection().deserialize(str));
    }

    public static @NotNull String strip(@NotNull String str) {
        return ChatColor.stripColor(str);
    }
}
