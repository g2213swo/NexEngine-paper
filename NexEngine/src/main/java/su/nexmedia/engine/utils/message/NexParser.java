package su.nexmedia.engine.utils.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.regex.RegexUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NexParser {

    public static final String OPTION_PATTERN = ":(?:'|\")(.*?)(?:'|\")(?=>|\\s|$)";
    private static final Pattern PATTERN_OPTIONS = Pattern.compile("<\\?(.*?)\\?>(.*?)(?:<\\/>)+");

    public static boolean contains(@NotNull String message) {
        Matcher matcher = RegexUtil.getMatcher(PATTERN_OPTIONS, message);
        return matcher.find();
    }

    @NotNull
    public static String removeFrom(@NotNull String message) {
        return strip(message, false);
    }

    @NotNull
    public static String toPlainText(@NotNull String message) {
        return strip(message, true);
    }

    @NotNull
    private static String strip(@NotNull String message, boolean toPlain) {
        Matcher matcher = RegexUtil.getMatcher(PATTERN_OPTIONS, message);
        while (RegexUtil.matcherFind(matcher)) {
            String matchFull = matcher.group(0);
            if (toPlain) {
                String matchOptions = matcher.group(1).trim();
                String matchText = matcher.group(2);
                message = message.replace(matchFull, matchText);
            } else message = message.replace(matchFull, "");
        }
        return message;
    }

    @Nullable
    private static String getOption(@NotNull Pattern pattern, @NotNull String from) {
        Matcher matcher = RegexUtil.getMatcher(pattern, from);
        if (!RegexUtil.matcherFind(matcher)) return null;

        return matcher.group(1).stripLeading();
    }
}
