package su.nexmedia.engine.api.editor;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.StringUtil;

public class InputWrapper {

    private final String text;
    private final String textRaw;
    private final String textColored;

    public InputWrapper(@NotNull AsyncPlayerChatEvent e) {
        this(e.getMessage());
    }

    public InputWrapper(@NotNull String text) {
        this.text = text;
        this.textRaw = Colorizer.strip(text);
        this.textColored = Colorizer.legacy(text);
    }

    public int asInt() {
        return this.asInt(0);
    }

    public int asInt(int def) {
        return StringUtil.getInteger(this.getTextRaw(), def);
    }

    public int asAnyInt(int def) {
        return StringUtil.getInteger(this.getTextRaw(), def, true);
    }

    public double asDouble() {
        return this.asDouble(0D);
    }

    public double asDouble(double def) {
        return StringUtil.getDouble(this.getTextRaw(), def);
    }

    public double asAnyDouble(double def) {
        return StringUtil.getDouble(this.getTextRaw(), def, true);
    }

    public @Nullable <E extends Enum<E>> E asEnum(@NotNull Class<E> clazz) {
        return StringUtil.getEnum(this.getTextRaw(), clazz).orElse(null);
    }

    public @NotNull <E extends Enum<E>> E asEnum(@NotNull Class<E> clazz, @NotNull E def) {
        return StringUtil.getEnum(this.getTextRaw(), clazz).orElse(def);
    }

    @Deprecated public @NotNull String getMessage() {
        return this.text;
    }

    public @NotNull String getText() {
        return text;
    }

    public @NotNull String getTextRaw() {
        return textRaw;
    }

    public @NotNull String getTextColored() {
        return textColored;
    }
}
