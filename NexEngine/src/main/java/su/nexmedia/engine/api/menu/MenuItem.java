package su.nexmedia.engine.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.type.ClickType;

import java.util.*;
import java.util.function.Predicate;

@Deprecated
public class MenuItem {

    protected final String id;

    protected Enum<?> type;
    protected int priority;
    protected ItemStack item;
    protected int[] slots;
    protected MenuItemVisibility visibility;
    protected Map<UUID, MenuItemVisibility> personalVisibility;
    protected Predicate<Player> visibilityPolicy;

    protected MenuClick clickHandler;
    protected Map<ClickType, List<String>> clickCommands;

    public MenuItem(@NotNull ItemStack item) {
        this(item, new int[0]);
    }

    public MenuItem(@NotNull ItemStack item, int... slots) {
        this(item, null, slots);
    }

    public MenuItem(@NotNull ItemStack item, @Nullable Enum<?> type, int... slots) {
        this(UUID.randomUUID().toString(), item, type, slots);
    }

    public MenuItem(@NotNull String id, @NotNull ItemStack item, int... slots) {
        this(id, item, null, slots);
    }

    public MenuItem(@NotNull String id, @NotNull ItemStack item, @Nullable Enum<?> type, int... slots) {
        this(id, type, slots, 0, item, new HashMap<>());
    }

    public MenuItem(@NotNull MenuItem menuItem) {
        this(menuItem.getId(), menuItem.getType(), menuItem.getSlots(), menuItem.getPriority(), menuItem.getItem(), menuItem.getClickCommands());
    }

    public MenuItem(
        @NotNull String id, @Nullable Enum<?> type, int[] slots, int priority,
        @NotNull ItemStack item,
        @NotNull Map<ClickType, List<String>> clickCommands) {
        this.id = id.toLowerCase();
        this.setType(type);
        this.setPriority(priority);
        this.setSlots(slots);
        this.setItem(item);
        this.personalVisibility = new HashMap<>();
        this.setVisibility(MenuItemVisibility.VISIBLE);
        this.setVisibilityPolicy(null);
        this.clickCommands = clickCommands;
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @Nullable Enum<?> getType() {
        return this.type;
    }

    public void setType(@Nullable Enum<?> type) {
        this.type = type;
    }

    public int[] getSlots() {
        return this.slots;
    }

    public void setSlots(int... slots) {
        this.slots = slots;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public @NotNull ItemStack getItem() {
        return new ItemStack(this.item);
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = new ItemStack(item);
    }

    public @NotNull MenuItemVisibility getVisibility() {
        return this.visibility;
    }

    public void setVisibility(@NotNull MenuItemVisibility visibility) {
        this.visibility = visibility;
    }

    public @NotNull Map<UUID, MenuItemVisibility> getPersonalVisibility() {
        return this.personalVisibility;
    }

    public @Nullable Predicate<Player> getVisibilityPolicy() {
        return this.visibilityPolicy;
    }

    public void setVisibilityPolicy(@Nullable Predicate<Player> visibilityPolicy) {
        this.visibilityPolicy = visibilityPolicy;
    }

    public @NotNull MenuItemVisibility getPersonalVisibility(@NotNull Player player) {
        return this.getPersonalVisibility().getOrDefault(player.getUniqueId(), this.getVisibility());
    }

    public void setPersonalVisibility(@NotNull Player player, @NotNull MenuItemVisibility visibility) {
        this.getPersonalVisibility().put(player.getUniqueId(), visibility);
    }

    public void hideFrom(@NotNull Player player) {
        this.setPersonalVisibility(player, MenuItemVisibility.HIDDEN);
    }

    public void showFor(@NotNull Player player) {
        this.setPersonalVisibility(player, MenuItemVisibility.VISIBLE);
    }

    public void resetVisibility(@NotNull Player player) {
        this.getPersonalVisibility().remove(player.getUniqueId());
    }

    public boolean isVisible(@NotNull Player player) {
        MenuItemVisibility personal = this.getPersonalVisibility(player);
        MenuItemVisibility global = this.getVisibility();
        Predicate<Player> policy = this.getVisibilityPolicy();

        if (global == MenuItemVisibility.HIDDEN) {
            return personal == MenuItemVisibility.VISIBLE || (policy != null && policy.test(player));
        } else if (global == MenuItemVisibility.VISIBLE) {
            return personal != MenuItemVisibility.HIDDEN && (policy == null || policy.test(player));
        }
        return false;
    }

    public @Nullable MenuClick getClickHandler() {
        return this.clickHandler;
    }

    public void setClickHandler(@Nullable MenuClick clickHandler) {
        this.clickHandler = clickHandler;
    }

    public @NotNull Map<ClickType, List<String>> getClickCommands() {
        return this.clickCommands;
    }

    public @NotNull List<String> getClickCommands(@NotNull ClickType clickType) {
        return this.getClickCommands().getOrDefault(clickType, Collections.emptyList());
    }
}