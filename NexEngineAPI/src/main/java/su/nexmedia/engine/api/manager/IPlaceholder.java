package su.nexmedia.engine.api.manager;

import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Deprecated
public interface IPlaceholder {

    String  DELIMITER_DEFAULT = "\n";
    Pattern PERCENT_PATTERN   = Pattern.compile("%([^%]+)%");
    Pattern BRACKET_PATTERN   = Pattern.compile("[{]([^{}]+)[}]");

    @NotNull UnaryOperator<String> replacePlaceholders();
}
