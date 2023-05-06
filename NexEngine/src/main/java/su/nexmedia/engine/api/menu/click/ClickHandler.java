package su.nexmedia.engine.api.menu.click;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.menu.impl.Menu;

import java.util.HashMap;
import java.util.Map;

public class ClickHandler<E extends Enum<E>> {

    private final Map<Enum<E>, ItemClick> clicks;

    public ClickHandler() {
        this.clicks = new HashMap<>();
    }

    public static @NotNull ItemClick forNextPage(@NotNull Menu<?> menu) {
        return (viewer, event) -> menu.open(viewer.getPlayer(), viewer.getPage() + 1);
    }

    public static @NotNull ItemClick forPreviousPage(@NotNull Menu<?> menu) {
        return (viewer, event) -> menu.open(viewer.getPlayer(), viewer.getPage() - 1);
    }

    public @NotNull ClickHandler<E> addClick(@NotNull E type, @NotNull ItemClick click) {
        this.clicks.put(type, click);
        return this;
    }

    public @Nullable ItemClick getClick(@NotNull Enum<?> type) {
        return this.clicks.get(type);
    }
}
