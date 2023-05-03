package su.nexmedia.engine.api.menu;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;

import java.util.Arrays;
import java.util.List;

public enum MenuItemType implements EditorButtonType {
    NONE,
    PAGE_NEXT(Material.ARROW, "<b><gold>Next Page"),
    PAGE_PREVIOUS(Material.ARROW, "<b><gold>Previous Page"),
    CLOSE(Material.BARRIER, "<b><red>Close"),
    RETURN(Material.BARRIER, "<b><red>Return"),
    CONFIRMATION_ACCEPT(Material.LIME_DYE, "<b><green>Accept"),
    CONFIRMATION_DECLINE(Material.PINK_DYE, "<b><red>Decline"),
    ;

    private final Material material;
    private String name;
    private List<String> lore;

    MenuItemType() {
        this(Material.AIR, "", "");
    }

    MenuItemType(@NotNull Material material, @NotNull String name, @NotNull String... lore) {
        this.material = material;
        this.setName(name);
        this.setLore(Arrays.asList(lore));
    }

    @Override
    public @NotNull Material getMaterial() {
        return this.material;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull List<String> getLore() {
        return this.lore;
    }

    @Override
    public void setLore(@NotNull List<String> lore) {
        this.lore = lore;
    }

}
