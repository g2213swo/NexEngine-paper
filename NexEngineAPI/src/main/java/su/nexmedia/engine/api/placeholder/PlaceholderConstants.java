package su.nexmedia.engine.api.placeholder;

import java.util.regex.Pattern;

public class PlaceholderConstants {
    public static final String DEFAULT = "default";
    public static final String NONE = "none";
    public static final String WILDCARD = "*";
    public static final String DEFAULT_DELIMITER = "\n";
    public static final Pattern PERCENT_PATTERN = Pattern.compile("%([^%]+)%");
    public static final Pattern BRACKET_PATTERN = Pattern.compile("[{]([^{}]+)[}]");
}
