package su.nexmedia.engine.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NMS {

    @NotNull String getNBTTag(@NotNull ItemStack item);

    @Nullable String toBase64(@NotNull ItemStack item);

    @Nullable ItemStack fromBase64(@NotNull String data);

    /**
     * Damages an ItemStack naturally.
     *
     * @param item the item to be damaged
     * @param amount the amount of durability to loss
     * @param player the player who initiates the damage
     *
     * @return a damaged ItemStack
     */
    @NotNull ItemStack damageItem(@NotNull ItemStack item, int amount, @Nullable Player player);

    /**
     * Updates the title of menu which the player is viewing.
     *
     * @param player the player who is viewing the menu
     * @param title a string in MiniMessage representation
     */
    void updateMenuTitle(@NotNull Player player, @NotNull String title);
}
