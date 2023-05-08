package su.nexmedia.engine.api.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.menu.impl.MenuViewer;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ItemOptions {

    private Predicate<MenuViewer> visibilityPolicy;
    private Predicate<MenuViewer> weakPolicy;
    private BiConsumer<MenuViewer, ItemStack> displayModifier;

    public ItemOptions() {
        this(null, null, null);
    }

    public ItemOptions(@Nullable Predicate<MenuViewer> visibilityPolicy,
        @Nullable Predicate<MenuViewer> weakPolicy,
        @Nullable BiConsumer<MenuViewer, ItemStack> displayModifier) {
        this.setVisibilityPolicy(visibilityPolicy);
        this.setWeakPolicy(weakPolicy);
        this.setDisplayModifier(displayModifier);
    }

    public static @NotNull ItemOptions personalWeak(@NotNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        Predicate<MenuViewer> weak = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, weak, null);
    }

    public static @NotNull ItemOptions personalPermanent(@NotNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, null, null);
    }

    public boolean canSee(@NotNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getVisibilityPolicy();
        return policy == null || policy.test(viewer);
    }

    public boolean canBeDestroyed(@NotNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getWeakPolicy();
        return policy != null && policy.test(viewer);
    }

    public void modifyDisplay(@NotNull MenuViewer viewer, @NotNull ItemStack item) {
        BiConsumer<MenuViewer, ItemStack> displayModifier = this.getDisplayModifier();
        if (displayModifier != null) {
            displayModifier.accept(viewer, item);
        }
    }

    public @Nullable Predicate<MenuViewer> getVisibilityPolicy() {
        return this.visibilityPolicy;
    }

    public @NotNull ItemOptions setVisibilityPolicy(@Nullable Predicate<MenuViewer> visibilityPolicy) {
        this.visibilityPolicy = visibilityPolicy;
        return this;
    }

    public @Nullable Predicate<MenuViewer> getWeakPolicy() {
        return this.weakPolicy;
    }

    public @NotNull ItemOptions setWeakPolicy(@Nullable Predicate<MenuViewer> weakPolicy) {
        this.weakPolicy = weakPolicy;
        return this;
    }

    public @Nullable BiConsumer<MenuViewer, ItemStack> getDisplayModifier() {
        return this.displayModifier;
    }

    public @NotNull ItemOptions setDisplayModifier(@Nullable BiConsumer<MenuViewer, ItemStack> displayModifier) {
        this.displayModifier = displayModifier;
        return this;
    }

    public @NotNull ItemOptions applyDisplayModifier(@NotNull BiConsumer<MenuViewer, ItemStack> displayModifier) {
        if (this.displayModifier == null) {
            this.displayModifier = displayModifier;
            return this;
        }
        this.displayModifier = this.displayModifier.andThen(displayModifier);
        return this;
    }
}
