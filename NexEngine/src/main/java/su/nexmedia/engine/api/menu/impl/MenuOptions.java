package su.nexmedia.engine.api.menu.impl;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.ComponentUtil;

public class MenuOptions {

    private String title;
    private int size;
    private InventoryType type;

    public MenuOptions(@NotNull String title, int size, @NotNull InventoryType type) {
        this.setTitle(title);
        this.setSize(size);
        this.setType(type);
    }

    public MenuOptions(@NotNull MenuOptions options) {
        this(options.getTitle(), options.getSize(), options.getType());
    }

    public @NotNull Inventory createInventory() {
        String title = this.getTitle();
        if (this.getType() == InventoryType.CHEST) {
            return Bukkit.getServer().createInventory(null, this.getSize(), ComponentUtil.asComponent(title));
        } else {
            return Bukkit.getServer().createInventory(null, this.getType(), ComponentUtil.asComponent(title));
        }
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        if (size <= 0 || size % 9 != 0) size = 27;
        this.size = size;
    }

    public @NotNull InventoryType getType() {
        return this.type;
    }

    public void setType(@NotNull InventoryType type) {
        this.type = type;
    }
}
