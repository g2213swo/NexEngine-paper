package su.nexmedia.engine.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexEngine;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.config.EngineConfig;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.hooks.misc.PlaceholderHook;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.UnaryOperator;

public class ItemUtil {

    private static final NexEngine ENGINE = NexEngine.get();

    public static int addToLore(@NotNull List<String> lore, int pos, @NotNull String value) {
        if (pos >= lore.size() || pos < 0) {
            lore.add(value);
        } else {
            lore.add(pos, value);
        }
        return pos < 0 ? pos : pos + 1;
    }

    public static @NotNull Component getName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() ? Objects.requireNonNull(meta.displayName()) : Component.translatable(item.getType());
    }

    public static @NotNull List<Component> getLore(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasLore() ? Objects.requireNonNull(meta.lore()) : new ArrayList<>();
    }

    public static @NotNull ItemStack createCustomHead(@NotNull String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        setSkullTexture(item, texture);
        return item;
    }

    public static void setSkullTexture(@NotNull ItemStack item, @NotNull String value) {
        if (item.getType() != Material.PLAYER_HEAD) return;
        if (!(item.getItemMeta() instanceof SkullMeta meta)) return;

        GameProfile profile = new GameProfile(EngineConfig.getIdForSkullTexture(value), null);
        profile.getProperties().put("textures", new Property("textures", value));

        Method method = Reflex.getMethod(meta.getClass(), "setProfile", GameProfile.class);
        if (method != null) {
            Reflex.invokeMethod(method, meta, profile);
        } else {
            Reflex.setFieldValue(meta, "profile", profile);
        }

        item.setItemMeta(meta);
    }

    public static @Nullable String getSkullTexture(@NotNull ItemStack item) {
        if (item.getType() != Material.PLAYER_HEAD) return null;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return null;

        GameProfile profile = (GameProfile) Reflex.getFieldValue(meta, "profile");
        if (profile == null) return null;

        Collection<Property> properties = profile.getProperties().get("textures");
        Optional<Property> opt = properties.stream().filter(prop -> {
            return prop.getName().equalsIgnoreCase("textures") || prop.getSignature().equalsIgnoreCase("textures");
        }).findFirst();

        return opt.map(Property::getValue).orElse(null);
    }

    public static void setPlaceholderAPI(@NotNull ItemMeta meta, @NotNull Player player) {
        if (!Hooks.hasPlaceholderAPI()) return;
        if (meta.hasDisplayName()) {
            Component original = Objects.requireNonNull(meta.displayName());
            Component replaced = PlaceholderHook.setPlaceholders(player, original);
            meta.displayName(replaced);
        }
        if (meta.hasLore()) {
            List<Component> original = Objects.requireNonNull(meta.lore());
            List<Component> replaced = original.stream().map(c -> PlaceholderHook.setPlaceholders(player, c)).toList();
            meta.lore(replaced);
        }
    }

    /**
     * Applies the string replacer to the display name and lore of the item meta.
     * <p>
     * <b>Note that this method does not handle the "newline" character in the string replacer.</b>
     *
     * @param meta     an item meta which the replacer applies to
     * @param replacer a string replacer
     *
     * @return true if the item meta changed
     */
    @SafeVarargs
    public static boolean replaceNameAndLore(@Nullable ItemMeta meta, @NotNull UnaryOperator<String>... replacer) {
        if (meta == null || replacer.length == 0) return false;

        // Replace item name
        Component name;
        if ((name = meta.displayName()) != null) {
            name = ComponentUtil.replace(name, replacer);
            meta.displayName(name);
        }

        // Replace item lore
        List<Component> lore;
        if ((lore = meta.lore()) != null) {
            lore = ComponentUtil.replace(lore, replacer);
            lore = ComponentUtil.compressEmptyLines(lore);
            meta.lore(lore);
        }

        return name != null || lore != null;
    }

    public static boolean replacePlaceholderListComponent(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<Component> replacer, boolean compressEmpty, boolean keep) {
        List<Component> lore;
        if (meta == null || (lore = meta.lore()) == null) return false;
        lore = ComponentUtil.replacePlaceholderList(placeholder, lore, replacer, keep);
        if (compressEmpty) lore = ComponentUtil.compressEmptyLines(lore);
        meta.lore(lore);
        return true;
    }

    /**
     * Modifies the item lore such that the <b>first</b> placeholder found in the lore is replaced with the given list
     * of strings. Note that the replaced lore will span multiple lines of the lore, starting at the given placeholder,
     * if the replacer contains more than one string.
     *
     * @param meta          the item meta to be modified
     * @param placeholder   the placeholder in the item lore to be replaced
     * @param replacer      the list of stings that will replace the given placeholder
     * @param compressEmpty true to compress empty lines in the lore
     *
     * @return true if the item meta changed
     *
     * @see ComponentUtil#replacePlaceholderList(String, List, List)
     */
    public static boolean replacePlaceholderListComponent(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<Component> replacer, boolean compressEmpty) {
        return replacePlaceholderListComponent(meta, placeholder, replacer, compressEmpty, false);
    }

    /**
     * @see #replacePlaceholderListComponent(ItemMeta, String, List, boolean)
     */
    public static boolean replacePlaceholderListComponent(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<Component> replacer) {
        return replacePlaceholderListComponent(meta, placeholder, replacer, false);
    }

    /**
     * @see #replacePlaceholderListComponent(ItemMeta, String, List, boolean, boolean)
     */
    public static boolean replacePlaceholderListString(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<String> replacer, boolean compressEmpty, boolean keep) {
        return replacePlaceholderListComponent(meta, placeholder, ComponentUtil.asComponent(replacer), compressEmpty, keep);
    }

    /**
     * @see #replacePlaceholderListComponent(ItemMeta, String, List, boolean)
     */
    public static boolean replacePlaceholderListString(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<String> replacer, boolean compressEmpty) {
        return replacePlaceholderListComponent(meta, placeholder, ComponentUtil.asComponent(replacer), compressEmpty);
    }

    /**
     * @see #replacePlaceholderListComponent(ItemMeta, String, List, boolean)
     */
    public static boolean replacePlaceholderListString(@Nullable ItemMeta meta, @NotNull String placeholder, @NotNull List<String> replacer) {
        return replacePlaceholderListComponent(meta, placeholder, ComponentUtil.asComponent(replacer));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void removeItalic(ItemMeta meta) {
        if (meta.hasDisplayName())
            meta.displayName(ComponentUtil.removeItalic(meta.displayName()));
        if (meta.hasLore())
            meta.lore(ComponentUtil.removeItalic(meta.lore()));
    }

    @Deprecated
    public static boolean isWeapon(@NotNull ItemStack item) {
        return isSword(item) || isAxe(item) || isTrident(item);
    }

    public static boolean isTool(@NotNull ItemStack item) {
        return isAxe(item) || isHoe(item) || isPickaxe(item) || isShovel(item);
    }

    public static boolean isArmor(@NotNull ItemStack item) {
        return isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item);
    }

    public static boolean isBow(@NotNull ItemStack item) {
        return item.getType() == Material.BOW || item.getType() == Material.CROSSBOW;
    }

    public static boolean isSword(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_SWORDS.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_SWORD || material == Material.GOLDEN_SWORD
               || material == Material.IRON_SWORD || material == Material.NETHERITE_SWORD
               || material == Material.STONE_SWORD || material == Material.WOODEN_SWORD;
    }

    public static boolean isAxe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_AXES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_AXE || material == Material.GOLDEN_AXE
               || material == Material.IRON_AXE || material == Material.NETHERITE_AXE
               || material == Material.STONE_AXE || material == Material.WOODEN_AXE;
    }

    public static boolean isTrident(@NotNull ItemStack item) {
        return item.getType() == Material.TRIDENT;
    }

    public static boolean isPickaxe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_PICKAXES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_PICKAXE || material == Material.GOLDEN_PICKAXE
               || material == Material.IRON_PICKAXE || material == Material.NETHERITE_PICKAXE
               || material == Material.STONE_PICKAXE || material == Material.WOODEN_PICKAXE;
    }

    public static boolean isShovel(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_SHOVELS.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_SHOVEL || material == Material.GOLDEN_SHOVEL
               || material == Material.IRON_SHOVEL || material == Material.NETHERITE_SHOVEL
               || material == Material.STONE_SHOVEL || material == Material.WOODEN_SHOVEL;
    }

    public static boolean isHoe(@NotNull ItemStack item) {
        if (Version.isAtLeast(Version.V1_19_R3)) {
            return Tag.ITEMS_HOES.isTagged(item.getType());
        }

        Material material = item.getType();
        return material == Material.DIAMOND_HOE || material == Material.GOLDEN_HOE
               || material == Material.IRON_HOE || material == Material.NETHERITE_HOE
               || material == Material.STONE_HOE || material == Material.WOODEN_HOE;
    }

    public static boolean isElytra(@NotNull ItemStack item) {
        return item.getType() == Material.ELYTRA;
    }

    public static boolean isFishingRod(@NotNull ItemStack item) {
        return item.getType() == Material.FISHING_ROD;
    }

    public static boolean isHelmet(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.HEAD;
    }

    public static boolean isChestplate(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.CHEST;
    }

    public static boolean isLeggings(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.LEGS;
    }

    public static boolean isBoots(@NotNull ItemStack item) {
        return getEquipmentSlot(item) == EquipmentSlot.FEET;
    }

    public static @NotNull EquipmentSlot getEquipmentSlot(@NotNull ItemStack item) {
        Material material = item.getType();
        return material.isItem() ? material.getEquipmentSlot() : EquipmentSlot.HAND;
    }

    public static @NotNull String getNBTTag(@NotNull ItemStack item) {
        return ENGINE.getNMS().getNBTTag(item);
    }

    public static @Nullable String toBase64(@NotNull ItemStack item) {
        return ENGINE.getNMS().toBase64(item);
    }

    public static @NotNull List<String> toBase64(@NotNull ItemStack[] item) {
        return toBase64(Arrays.asList(item));
    }

    public static @NotNull List<String> toBase64(@NotNull List<ItemStack> items) {
        return new ArrayList<>(items.stream().map(ItemUtil::toBase64).filter(Objects::nonNull).toList());
    }

    public static @Nullable ItemStack fromBase64(@NotNull String data) {
        return ENGINE.getNMS().fromBase64(data);
    }

    public static @NotNull ItemStack[] fromBase64(@NotNull List<String> list) {
        List<ItemStack> items = list.stream().map(ItemUtil::fromBase64).filter(Objects::nonNull).toList();
        return items.toArray(new ItemStack[list.size()]);
    }
}
