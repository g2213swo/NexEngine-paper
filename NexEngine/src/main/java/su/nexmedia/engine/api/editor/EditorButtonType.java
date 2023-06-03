package su.nexmedia.engine.api.editor;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.StringUtil;

import java.util.List;

public interface EditorButtonType {

    String PREFIX_INFO = "<gold><b>[?] Description:";
    String PREFIX_NOTE = "<yellow><b>[!] Note:";
    String PREFIX_WARN = "<red><b>[!] Warning:";
    String PREFIX_CLICK = "<#55e13><b>[>] Actions:";
    String PREFIX_CURRENT = "<aqua><b>[?] Current:";

    @NotNull
    Material getMaterial();

    @NotNull
    String name();

    /**
     * @param name a MiniMessage string
     */
    void setName(@NotNull String name);

    /**
     * @return a MiniMessage string
     */
    @NotNull
    String getName();

    /**
     * @param lore a list of MiniMessage strings
     */
    void setLore(@NotNull List<String> lore);

    /**
     * @return a list of MiniMessage string
     */
    @NotNull
    List<String> getLore();

    static @NotNull String current(@NotNull String text) {
        return formatted(text, PREFIX_CURRENT, "<green>");
    }

    static @NotNull String info(@NotNull String text) {
        return formatted(split(text), PREFIX_INFO, "<gray>");
    }

    static @NotNull String warn(@NotNull String text) {
        return formatted(split(text), PREFIX_WARN, "<#c70039>"); // kinda red
    }

    static @NotNull String note(@NotNull String text) {
        return formatted(split(text), PREFIX_NOTE, "<#ffc300>"); // kinda yellow
    }

    static @NotNull String click(@NotNull String text) {
        return formatted(text, PREFIX_CLICK, "<#86de2a>"); // kinda green
    }

    static @NotNull String formatted(@NotNull String text, @NotNull String prefix, @NotNull String color) {
        List<String> list = Lists.newArrayList(text.split("\n"));
        list.replaceAll(line -> color + line);
        list.add(0, prefix);
        return String.join("\n", list);
    }

    static @NotNull List<String> fineLore(@NotNull String... lore) {
        return StringUtil.unfoldByNewline(lore);
    }

    /**
     * Transforms the text into multiple segments (of length less or equal to 6 words) which are separated with a '\n'
     * character.
     * <p>
     * It is known that the '\n' character does not create a newline in the item lore. Instead, it will be displayed as
     * a "newline" character. You are supposed to manually split the text into multiple lines and insert them into the
     * item lore. The method {@link #fineLore(String...)} may help.
     *
     * @param text a MiniMessage string
     *
     * @return the original miniMessage but the content is separated with {@code <br>} tag
     */
    static @NotNull String split(@NotNull String text) {
        return text.replaceAll("((?:\\S*\\s){5}\\S*)\\s", "$1\n");
    }

    default @NotNull ItemStack getItem() {
        ItemStack item = new ItemStack(this.getMaterial());
        item.editMeta(meta -> {
            meta.displayName(ComponentUtil
                .asComponent(this.getName())
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.BOLD)
            );
            meta.lore(ComponentUtil.asComponent(this.getLore()));
            meta.addItemFlags(ItemFlag.values());
        });
        return item;
    }
}
