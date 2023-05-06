package su.nexmedia.engine.api.editor;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class EditorLocale {

    private final String key;
    private final String name;
    private final List<String> lore;

    private String localizedName;
    private List<String> localizedLore;

    public EditorLocale(@NotNull String key, @NotNull String name, @NotNull List<String> lore) {
        this.key = key;
        this.name = name;
        this.lore = lore;
    }

    public static @NotNull EditorLocale of(@NotNull String key, @NotNull String name, @NotNull String... lore) {
        return new EditorLocale(key, name, Arrays.asList(lore));
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull List<String> getLore() {
        return this.lore;
    }

    public @NotNull String getLocalizedName() {
        return this.localizedName;
    }

    public void setLocalizedName(@NotNull String localizedName) {
        this.localizedName = localizedName;
    }

    public @NotNull List<String> getLocalizedLore() {
        return this.localizedLore;
    }

    public void setLocalizedLore(@NotNull List<String> localizedLore) {
        this.localizedLore = localizedLore;
    }
}
