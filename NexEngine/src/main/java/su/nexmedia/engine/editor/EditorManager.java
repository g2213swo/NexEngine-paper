package su.nexmedia.engine.editor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.LinearComponents;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexEngine;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.api.editor.EditorHandler;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.editor.EditorObject;
import su.nexmedia.engine.api.editor.InputHandler;
import su.nexmedia.engine.api.editor.InputWrapper;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.api.manager.IListener;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.impl.Menu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.lang.EngineLang;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.MessageUtil;
import su.nexmedia.engine.utils.Pair;
import su.nexmedia.engine.utils.StringUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static net.kyori.adventure.text.Component.text;

public class EditorManager extends AbstractManager<NexEngine> implements IListener {

    private static final NexEngine ENGINE = NexEngine.get();

    @Deprecated private static final Map<Player, Map.Entry<AbstractMenu<?>, Integer>> EDITOR_CACHE_MENU = new WeakHashMap<>();
    @Deprecated private static final Map<Player, EditorObject<?, ?>> EDITOR_CACHE_INPUT = new WeakHashMap<>();

    private static final Map<Player, Pair<Menu<?>, Integer>> USER_CACHE_MENU = new WeakHashMap<>();
    private static final Map<Player, EditorHandler> USER_CACHE_INPUT = new WeakHashMap<>();
    private static final Map<Player, InputHandler> USER_CACHE_INPUT_2 = new WeakHashMap<>();
    private static final Map<Player, List<String>> USER_CACHE_VALUES = new WeakHashMap<>();

    private static final String EXIT = "#exit";
    private static final String VALUES = "#values";
    private static final int TITLE_STAY = Short.MAX_VALUE;

    public EditorManager(@NotNull NexEngine plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.registerListeners();
    }

    @Override
    protected void onShutdown() {
        this.unregisterListeners();
    }

    @Override
    public final void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }

    @Deprecated
    public static @Nullable EditorObject<?, ?> getEditorInput(@NotNull Player player) {
        return EDITOR_CACHE_INPUT.get(player);
    }

    public static @Nullable EditorHandler getEditorHandler(@NotNull Player player) {
        return USER_CACHE_INPUT.get(player);
    }

    public static @Nullable InputHandler getInputHandler(@NotNull Player player) {
        return USER_CACHE_INPUT_2.get(player);
    }

    public static boolean isEditing(@NotNull Player player) {
        return getEditorInput(player) != null || getEditorHandler(player) != null;
    }

    @Deprecated
    public static <T, E extends Enum<E>> void startEdit(@NotNull Player player, @NotNull T object, @NotNull E type, @NotNull EditorInput<T, E> input) {
        EDITOR_CACHE_INPUT.put(player, new EditorObject<>(object, type, input));

        AbstractMenu<?> menu = AbstractMenu.getMenu(player);
        if (menu != null) {
            EDITOR_CACHE_MENU.put(player, new AbstractMap.SimpleEntry<>(menu, menu.getPage(player)));
        }
        ENGINE.getMessage(EngineLang.EDITOR_TIP_EXIT).send(player);
    }

    public static void startEdit(@NotNull Player player, @NotNull EditorHandler handler) {
        USER_CACHE_INPUT.put(player, handler);

        Menu<?> menu = Menu.getMenu(player);
        if (menu != null) {
            MenuViewer viewer = menu.getViewer(player);
            int page = viewer == null ? 1 : viewer.getPage();
            USER_CACHE_MENU.put(player, Pair.of(menu, page));
        }
        ENGINE.getMessage(EngineLang.EDITOR_TIP_EXIT).send(player);
    }

    public static void startEdit(@NotNull Player player, @NotNull InputHandler handler) {
        USER_CACHE_INPUT_2.put(player, handler);

        Menu<?> menu = Menu.getMenu(player);
        if (menu != null) {
            MenuViewer viewer = menu.getViewer(player);
            int page = viewer == null ? 1 : viewer.getPage();
            USER_CACHE_MENU.put(player, Pair.of(menu, page));
        }
        ENGINE.getMessage(EngineLang.EDITOR_TIP_EXIT).send(player);
    }

    public static void endEdit(@NotNull Player player) {
        endEdit(player, true);
    }

    public static void endEdit(@NotNull Player player, boolean msg) {
        if (EDITOR_CACHE_INPUT.remove(player) != null) {
            Map.Entry<AbstractMenu<?>, Integer> entry = EDITOR_CACHE_MENU.remove(player);
            if (entry != null) {
                entry.getKey().open(player, entry.getValue());
            }
        } else if (USER_CACHE_INPUT.remove(player) != null) {
            Pair<Menu<?>, Integer> entry = USER_CACHE_MENU.remove(player);
            if (entry != null) {
                entry.getFirst().open(player, entry.getSecond());
            }
        } else if (USER_CACHE_INPUT_2.remove(player) != null) {
            Pair<Menu<?>, Integer> entry = USER_CACHE_MENU.remove(player);
            if (entry != null) {
                entry.getFirst().open(player, entry.getSecond());
            }
        }
        USER_CACHE_VALUES.remove(player);

        MessageUtil.showTitle(player, ENGINE.getMessage(EngineLang.EDITOR_TITLE_DONE).getLocalized(), "", 10, 40, 10);
    }

    public static void suggestValues(@NotNull Player player, @NotNull Collection<String> items, boolean autoRun) {
        List<String> items1 = new ArrayList<>(items);
        items1.sort(String::compareTo);
        USER_CACHE_VALUES.put(player, items1);
        displayValues(player, autoRun, 1);
    }

    public static void displayValues(@NotNull Player player, boolean autoRun, int page) {
        List<String> values = USER_CACHE_VALUES.get(player);
        if (values == null || values.isEmpty()) return;

        // Prepare pagination logic
        int perPage = 10;
        int pages = (int) Math.ceil((double) values.size() / (double) perPage);
        if (page < 1) page = 1;
        else if (page > pages) page = pages;
        int skip = (page - 1) * perPage;

        boolean isLastPage = page == pages;
        boolean isFirstPage = page == 1;
        boolean fixCommand = Version.isAtLeast(Version.V1_19_R1);

        // Construct the header
        Component header = LinearComponents.linear(
            NamedTextColor.GOLD, text("=".repeat(8) + "[ "),
            NamedTextColor.YELLOW, text("Value Helper"),
            NamedTextColor.GOLD, text(" ]" + "=".repeat(8))
        );

        // Construct the footer
        Component pageText = text()
            .color(NamedTextColor.YELLOW)
            .append(text(page))
            .append(text(" / ").color(NamedTextColor.GOLD))
            .append(text(pages))
            .asComponent();
        Component prevText = text(" [<] ").color(NamedTextColor.GRAY);
        Component nextText = text(" [>] ").color(NamedTextColor.GRAY);
        if (!isFirstPage) prevText = prevText
            .hoverEvent(text("Previous Page"))
            .clickEvent(ClickEvent.runCommand("/" + VALUES + " " + (page - 1) + " " + autoRun));
        if (!isLastPage) nextText = nextText
            .hoverEvent(text("Next Page"))
            .clickEvent(ClickEvent.runCommand("/" + VALUES + " " + (page + 1) + " " + autoRun));
        Component footer = text()
            .color(NamedTextColor.GOLD)
            .append(text("=".repeat(9)))
            .append(prevText).append(pageText).append(nextText)
            .append(text("=".repeat(9)))
            .asComponent();

        // Construct the items
        List<Component> items = values.stream()
            // Only display the items in current page
            .skip(skip).limit(perPage)
            // Then we carefully construct each item
            .map(str -> {
                // Make Content + Add Color
                TextComponent.Builder builder = text()
                    .content(Colorizer.strip(str))
                    .color(NamedTextColor.GREEN);
                // Make HoverEvent
                builder.hoverEvent(
                    text()
                        .color(NamedTextColor.GRAY)
                        .content("Click me to select ")
                        .append(
                            text().content(str).color(NamedTextColor.AQUA)
                        )
                        .asComponent()
                );
                // Make ClickEvent
                String command = (autoRun && fixCommand && !str.startsWith("/")) ? ("/" + str) : str;
                builder.clickEvent(autoRun ? ClickEvent.runCommand(command) : ClickEvent.suggestCommand(command));
                // Add prefix and return final result
                return text()
                    .content("> ")
                    .color(NamedTextColor.DARK_GRAY)
                    .append(builder)
                    .asComponent();
            })
            .toList();

        player.sendMessage(header);
        player.sendMessage(Component.join(JoinConfiguration.newlines(), items));
        player.sendMessage(footer);
    }

    @Deprecated
    public static void sendCommandTips(@NotNull Player player) {
        String text = Colorizer.legacy("""
            &7
            &b&lCommand Syntax:
            &2• &a'[CONSOLE] <command>' &2- Execute as Console.
            &2• (no prefix) &a'<command>' &2- Execute as a Player.
            &7
            &b&lCommand Placeholders:
            &2• &a%player_name% &2- For player name.
            &7
            &b&lCommand Examples:
            &2▸ &a[CONSOLE] eco give %player_name% 250
            &2▸ &abroadcast Hello!
            &7"""
        );
        player.sendMessage(text);
    }

    public static void prompt(@NotNull Player player, @NotNull String text) {
        tip(player, ENGINE.getMessage(EngineLang.EDITOR_TITLE_EDIT).getLocalized(), text);
    }

    public static void error(@NotNull Player player, @NotNull String text) {
        error(player, ENGINE.getMessage(EngineLang.EDITOR_TITLE_ERROR).getLocalized(), text);
    }

    public static void tip(@NotNull Player player, @NotNull String title, @NotNull String text) {
        MessageUtil.showTitle(player, title, text, 20, TITLE_STAY, 40);
    }

    public static void error(@NotNull Player player, @NotNull String title, @NotNull String text) {
        tip(player, title, text);
    }

    @Deprecated
    public static @NotNull String fineId(@NotNull String id) {
        return StringUtil.lowerCaseUnderscore(id);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        endEdit(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        EditorObject<?, ?> editorInput = getEditorInput(player);
        if (editorInput == null) return;

        e.getRecipients().clear();
        e.setCancelled(true);

        String msg = e.getMessage();
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (msg.equalsIgnoreCase(EXIT) || editorInput.handle(player, e)) {
                endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat2(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        EditorHandler handler = getEditorHandler(player);
        if (handler == null) return;

        e.getRecipients().clear();
        e.setCancelled(true);

        String msg = e.getMessage();
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (msg.equalsIgnoreCase(EXIT) || handler.handle(e)) {
                endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat3(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        InputHandler handler = getInputHandler(player);
        if (handler == null) return;

        event.getRecipients().clear();
        event.setCancelled(true);

        InputWrapper wrapper = new InputWrapper(event);

        this.plugin.runTask(task -> {
            if (wrapper.getTextRaw().equalsIgnoreCase(EXIT) || handler.handle(wrapper)) {
                endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        EditorObject<?, ?> editorInput = getEditorInput(player);
        if (editorInput == null) return;

        e.setCancelled(true);

        String text = e.getMessage().substring(1);
        if (text.startsWith(VALUES)) {
            String[] split = text.split(" ");
            int page = split.length >= 2 ? StringUtil.getInteger(split[1], 0) : 0;
            boolean auto = split.length >= 3 && Boolean.parseBoolean(split[2]);
            displayValues(player, auto, page);
            return;
        }

        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, text, new HashSet<>());

        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (text.equalsIgnoreCase(EXIT) || editorInput.handle(player, event)) {
                endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCommand2(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        EditorHandler handler = getEditorHandler(player);
        if (handler == null) return;

        e.setCancelled(true);

        String text = e.getMessage().substring(1);
        if (text.startsWith(VALUES)) {
            String[] split = text.split(" ");
            int page = split.length >= 2 ? StringUtil.getInteger(split[1], 0) : 0;
            boolean auto = split.length >= 3 && Boolean.parseBoolean(split[2]);
            displayValues(player, auto, page);
            return;
        }

        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, text, new HashSet<>());

        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (text.equalsIgnoreCase(EXIT) || handler.handle(event)) {
                endEdit(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCommand3(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        InputHandler handler = getInputHandler(player);
        if (handler == null) return;

        e.setCancelled(true);

        String text = e.getMessage().substring(1);
        if (text.startsWith(VALUES)) {
            String[] split = text.split(" ");
            int page = split.length >= 2 ? StringUtil.getInteger(split[1], 0) : 0;
            boolean auto = split.length >= 3 && Boolean.parseBoolean(split[2]);
            displayValues(player, auto, page);
            return;
        }

        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, text, new HashSet<>());
        InputWrapper wrapper = new InputWrapper(event);

        this.plugin.runTask(task -> {
            if (wrapper.getTextRaw().equalsIgnoreCase(EXIT) || handler.handle(wrapper)) {
                endEdit(player);
            }
        });
    }
}
