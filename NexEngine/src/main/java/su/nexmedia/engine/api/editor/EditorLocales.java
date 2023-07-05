package su.nexmedia.engine.api.editor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EditorLocales {

    protected static final String RED = "<#ff6a6a>";
    protected static final String RED_CLOSE = "</#ff6a6a>";
    protected static final String GREEN = "<#bbff6a>";
    protected static final String GREEN_CLOSE = "</#bbff6a>";
    protected static final String BLUE = "<#6adbff>";
    protected static final String BLUE_CLOSE = "</#6adbff>";
    protected static final String ORANGE = "<#ffa76a>";
    protected static final String ORANGE_CLOSE = "</#ffa76a>";
    protected static final String YELLOW = "<#ffed6a>";
    protected static final String YELLOW_CLOSE = "</#ffed6a>";
    protected static final String GRAY = "<#bdc8c9>";
    protected static final String GRAY_CLOSE = "</#bdc8c9>";

    public static final EditorLocale CLOSE = EditorLocale.of("Editor.Generic.Close", "<#ff5733>(✕) <b>Exit");
    public static final EditorLocale RETURN = EditorLocale.of("Editor.Generic.Return", "<#ffee9a>(↓) <white>Return");
    public static final EditorLocale NEXT_PAGE = EditorLocale.of("Editor.Generic.NextPage", "<#e3fbf9>(→) <b>Next Page");
    public static final EditorLocale PREVIOUS_PAGE = EditorLocale.of("Editor.Generic.PreviousPage", "<#e3fbf9>(←) <b>Previous Page");

    protected static @NotNull Builder builder(@NotNull String key) {
        return new Builder(key);
    }

    protected static final class Builder {

        private final String key;
        private String name;
        private final List<String> lore;

        public Builder(@NotNull String key) {
            this.key = key;
            this.name = "";
            this.lore = new ArrayList<>();
        }

        public @NotNull EditorLocale build() {
            return new EditorLocale(this.key, this.name, this.lore);
        }

        public @NotNull Builder name(@NotNull String name) {
            this.name = YELLOW + "<b>" + name;
            return this;
        }

        public @NotNull Builder text(@NotNull String... text) {
            return this.addLore(GRAY, text);
        }

        public @NotNull Builder textRaw(@NotNull String... text) {
            return this.addLore("", text);
        }

        public @NotNull Builder currentHeader() {
            return this.addLore(YELLOW + "<b>", "Current:");
        }

        public @NotNull Builder current(@NotNull String type, @NotNull String value) {
            return this.addLore(YELLOW + "▪ " + YELLOW_CLOSE, type + ": " + YELLOW + value);
        }

        public @NotNull Builder warningHeader() {
            return this.addLore(RED + "<b>", "Warning:");
        }

        public @NotNull Builder warning(@NotNull String... info) {
            return this.addLore(RED + "▪ " + RED_CLOSE, info);
        }

        public @NotNull Builder noteHeader() {
            return this.addLore(ORANGE + "<b>", "Notes:");
        }

        public @NotNull Builder notes(@NotNull String... info) {
            return this.addLore(ORANGE + "▪ " + ORANGE_CLOSE, info);
        }

        public @NotNull Builder actionsHeader() {
            return this.addLore(GREEN + "<b>", "Actions:");
        }

        public @NotNull Builder action(@NotNull String click, @NotNull String action) {
            return this.addLore(GREEN + "▪ " + GREEN_CLOSE, click + ": " + GREEN + action);
        }

        public @NotNull Builder breakLine() {
            return this.addLore("", "");
        }

        private @NotNull Builder addLore(@NotNull String prefix, @NotNull String... text) {
            for (String str : text) {
                this.lore.add(prefix + str);
            }
            return this;
        }
    }
}
