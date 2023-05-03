package su.nexmedia.engine.lang;

import su.nexmedia.engine.api.lang.LangKey;

public class EngineLang {

    public static final LangKey CORE_COMMAND_USAGE = new LangKey("Core.Command.Usage", "<red>Usage: <yellow>/%command_label% <gold>%command_usage%");
    public static final LangKey CORE_COMMAND_HELP_LIST = new LangKey("Core.Command.Help.List",
        """
        <gold><st>              </st><b>[</b> <yellow><b>%plugin_name_localized%</b></yellow> <gray>-</gray> <yellow><b>Commands</b></yellow> <b>]</b><st>              </st></gold>
        <gold>▪ <yellow>/%command_label%</yellow> <gold>%command_usage%</gold> <gray>- %command_description%</gray>
        """);
    public static final LangKey CORE_COMMAND_HELP_DESC = new LangKey("Core.Command.Help.Desc", "Show help page.");
    public static final LangKey CORE_COMMAND_EDITOR_DESC = new LangKey("Core.Command.Editor.Desc", "Opens GUI Editor.");
    public static final LangKey CORE_COMMAND_ABOUT_DESC = new LangKey("Core.Command.About.Desc", "Some info about the plugin.");
    public static final LangKey CORE_COMMAND_RELOAD_DESC = new LangKey("Core.Command.Reload.Desc", "Reload the plugin.");
    public static final LangKey CORE_COMMAND_RELOAD_DONE = new LangKey("Core.Command.Reload.Done", "Reloaded!");

    public static final LangKey TIME_DAY = new LangKey("Time.Day", "%s%d.");
    public static final LangKey TIME_HOUR = new LangKey("Time.Hour", "%s%h.");
    public static final LangKey TIME_MIN = new LangKey("Time.Min", "%s%min.");
    public static final LangKey TIME_SEC = new LangKey("Time.Sec", "%s%sec.");

    public static final LangKey OTHER_YES = new LangKey("Other.Yes", "<green>Yes");
    public static final LangKey OTHER_NO = new LangKey("Other.No", "<red>No");
    public static final LangKey OTHER_ANY = new LangKey("Other.Any", "Any");
    public static final LangKey OTHER_NONE = new LangKey("Other.None", "None");
    public static final LangKey OTHER_NEVER = new LangKey("Other.Never", "Never");
    public static final LangKey OTHER_ONE_TIMED = new LangKey("Other.OneTimed", "One-Timed");
    public static final LangKey OTHER_UNLIMITED = new LangKey("Other.Unlimited", "Unlimited");
    public static final LangKey OTHER_INFINITY = new LangKey("Other.Infinity", "∞");

    public static final LangKey ERROR_PLAYER_INVALID = new LangKey("Error.Player.Invalid", "<red>Player not found.");
    public static final LangKey ERROR_WORLD_INVALID = new LangKey("Error.World.Invalid", "<red>World not found.");
    public static final LangKey ERROR_NUMBER_INVALID = new LangKey("Error.Number.Invalid", "<red><gray>%num%</gray> is not a valid number.");
    public static final LangKey ERROR_PERMISSION_DENY = new LangKey("Error.Permission.Deny", "<red>You don't have permissions to do that!");
    public static final LangKey ERROR_ITEM_INVALID = new LangKey("Error.Item.Invalid", "<red>You must hold an item!");
    public static final LangKey ERROR_COMMAND_SELF = new LangKey("Error.Command.Self", "Can not be used on yourself.");
    public static final LangKey ERROR_COMMAND_SENDER = new LangKey("Error.Command.Sender", "This command is for players only.");
    public static final LangKey ERROR_INTERNAL = new LangKey("Error.Internal", "<red>Internal error!");

    public static final LangKey EDITOR_TIP_EXIT = new LangKey("Editor.Tip.Exit", "<click:run_command:'/#exit'><hover:show_text:'<gray>Click me or type <white>#exit</white></gray>'><aqua>Click to exit the <light_purple>Edit Mode</light_purple></aqua></hover></click>");
    public static final LangKey EDITOR_TITLE_DONE = LangKey.of("Editor.Title.Done", "<green><b>Done!");
    public static final LangKey EDITOR_TITLE_EDIT = LangKey.of("Editor.Title.Edit", "<green><b><Edit Mode>");
    public static final LangKey EDITOR_TITLE_ERROR = LangKey.of("Editor.Title.Error", "<red><b>Error!");
    public static final LangKey EDITOR_ERROR_NUMBER_GENERIC = LangKey.of("Editor.Error.Number.Generic", "<gray>Invalid number!");
    public static final LangKey EDITOR_ERROR_NUMBER_NOT_INT = LangKey.of("Editor.Error.Number.NotInt", "<gray>Number must be <red>Integer</red>!");
    public static final LangKey EDITOR_ERROR_ENUM = LangKey.of("Editor.Error.Enum", "<gray>Invalid type! See in chat.");
}
