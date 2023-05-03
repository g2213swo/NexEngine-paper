package su.nexmedia.engine.utils;

import org.bukkit.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.random.Rnd;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtil {

    @Contract(pure = true)
    public static @NotNull String oneSpace(@NotNull String str) {
        return str.trim().replaceAll("\\s+", " ");
    }

    public static @NotNull String noSpace(@NotNull String str) {
        return str.trim().replaceAll("\\s+", "");
    }

    @NotNull
    public static String replaceEach(@NotNull String text, @NotNull List<Pair<String, Supplier<String>>> replacements) {
        if (text.isEmpty() || replacements.isEmpty())
            return text;

        final int searchLength = replacements.size();
        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i])
                continue;
            tempIndex = text.indexOf(replacements.get(i).getFirst());

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else if (textIndex == -1 || tempIndex < textIndex) {
                textIndex = tempIndex;
                replaceIndex = i;
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1)
            return text;

        int start = 0;
        final StringBuilder buf = new StringBuilder();
        while (textIndex != -1) {
            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacements.get(replaceIndex).getSecond().get());

            start = textIndex + replacements.get(replaceIndex).getFirst().length();

            textIndex = -1;
            replaceIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i])
                    continue;
                tempIndex = text.indexOf(replacements.get(i).getFirst(), start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
            // NOTE: logic duplicated above END

        }
        final int textLength = text.length();
        for (int i = start; i < textLength; i++)
            buf.append(text.charAt(i));
        return buf.toString();
    }

    @SafeVarargs
    @Contract(pure = true)
    public static @NotNull List<String> replace(@NotNull List<String> original, @NotNull UnaryOperator<String>... replacer) {
        return replace(false, original, replacer);
    }

    @SafeVarargs
    @Contract(pure = true)
    public static @NotNull List<String> replace(boolean unfoldNewline, @NotNull List<String> original, @NotNull UnaryOperator<String>... replacer) {
        List<String> newList = new ArrayList<>(original);
        for (UnaryOperator<String> re : replacer) newList.replaceAll(re);
        if (unfoldNewline) newList = unfoldByNewline(newList);
        return newList;
    }

    @SafeVarargs
    @Contract(pure = true)
    public static @NotNull String replace(@NotNull String text, @NotNull UnaryOperator<String>... replacer) {
        for (UnaryOperator<String> re : replacer) {
            text = re.apply(text); // Reassign
        }
        return text;
    }

    @Contract(pure = true, value = "_, null, _ -> null; _, !null, _ -> !null ")
    public static List<String> replacePlaceholderList(@NotNull String placeholder, @Nullable List<String> dst, @NotNull List<String> src) {
        return replacePlaceholderList(placeholder, dst, src, false);
    }

    @Contract(pure = true, value = "_, null, _, _ -> null; _, !null, _, _ -> !null ")
    public static List<String> replacePlaceholderList(@NotNull String placeholder, @Nullable List<String> dst, @NotNull List<String> src, boolean keep) {
        if (dst == null) return null;

        // Let's find which line (in the dst) has the placeholder
        int placeholderIdx = -1;
        String placeholderLine = null;
        for (int i = 0; i < dst.size(); i++) {
            placeholderLine = dst.get(i);
            if (placeholderLine.contains(placeholder)) {
                placeholderIdx = i;
                break;
            }
        }
        if (placeholderIdx == -1) return dst;

        // Let's make the list to be inserted into the dst
        if (keep) {
            src = new ArrayList<>(src);
            ListIterator<String> it = src.listIterator();
            while (it.hasNext()) {
                String line = it.next();
                String replaced = placeholderLine.replace(placeholder, line);
                it.set(replaced);
            }
        }

        // Insert the src into the dst
        List<String> result = new ArrayList<>(dst);
        result.remove(placeholderIdx); // Need to remove the raw placeholder from dst
        result.addAll(placeholderIdx, src);

        return result;
    }

    /**
     * Transforms any group of empty strings found in a row into just one empty string.
     *
     * @param stringList a list of strings which may contain empty lines
     *
     * @return a modified copy of the list
     */
    @Contract(pure = true)
    public static @NotNull List<String> compressEmptyLines(@NotNull List<String> stringList) {
        List<String> stripped = new ArrayList<>();
        boolean prevEmpty = false; // Mark whether the previous line is empty
        for (String line : stringList) {
            if (line.isEmpty()) {
                if (!prevEmpty) {
                    prevEmpty = true;
                    stripped.add(line);
                }
            } else {
                prevEmpty = false;
                stripped.add(line);
            }
        }
        return stripped;
    }

    @Contract(pure = true)
    public static @NotNull List<String> unfoldByNewline(@NotNull List<String> lore) {
        List<String> unfolded = new ArrayList<>();
        for (String str : lore) {
            String[] arr = str.split("\n");
            if (arr.length > 1) {
                unfolded.addAll(Arrays.asList(arr));
            } else { // for better performance
                unfolded.add(str);
            }
        }
        return unfolded;
    }

    @Contract(pure = true)
    public static @NotNull List<String> unfoldByNewline(@NotNull String... lore) {
        return unfoldByNewline(Arrays.asList(lore));
    }

    public static double getDouble(@NotNull String input, double def) {
        return getDouble(input, def, false);
    }

    public static double getDouble(@NotNull String input, double def, boolean allowNegative) {
        try {
            double amount = Double.parseDouble(input);
            return (amount < 0D && !allowNegative ? def : amount);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    public static int getInteger(@NotNull String input, int def) {
        return getInteger(input, def, false);
    }

    public static int getInteger(@NotNull String input, int def, boolean allowNegative) {
        return (int) getDouble(input, def, allowNegative);
    }

    public static int[] getIntArray(@NotNull String str) {
        String[] split = noSpace(str).split(",");
        int[] array = new int[split.length];
        for (int index = 0; index < split.length; index++) {
            try {
                array[index] = Integer.parseInt(split[index]);
            } catch (NumberFormatException e) {
                array[index] = 0;
            }
        }
        return array;
    }

    public static <T extends Enum<T>> @NotNull Optional<T> getEnum(@NotNull String str, @NotNull Class<T> clazz) {
        try {
            return Optional.of(Enum.valueOf(clazz, str.toUpperCase()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Contract(pure = true)
    public static @NotNull Color parseColor(@NotNull String colorRaw) {
        String[] rgb = colorRaw.split(",");
        int red = getInteger(rgb[0], 0);
        if (red < 0) red = Rnd.get(255);

        int green = rgb.length >= 2 ? getInteger(rgb[1], 0) : 0;
        if (green < 0) green = Rnd.get(255);

        int blue = rgb.length >= 3 ? getInteger(rgb[2], 0) : 0;
        if (blue < 0) blue = Rnd.get(255);

        return Color.fromRGB(red, green, blue);
    }

    public static @NotNull String lowerCaseUnderscore(@NotNull String str) {
        return Colorizer.strip(str).toLowerCase().replace(" ", "_");
    }

    public static @NotNull String capitalizeUnderscored(@NotNull String str) {
        return capitalizeFully(str.replace("_", " "));
    }

    public static @NotNull String capitalizeFully(@NotNull String str) {
        if (str.length() != 0) {
            str = str.toLowerCase();
            return capitalize(str);
        }
        return str;
    }

    public static @NotNull String capitalize(@NotNull String str) {
        if (str.length() != 0) {
            int strLen = str.length();
            StringBuilder buffer = new StringBuilder(strLen);
            boolean capitalizeNext = true;

            for (int i = 0; i < strLen; ++i) {
                char ch = str.charAt(i);
                if (Character.isWhitespace(ch)) {
                    buffer.append(ch);
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer.append(Character.toTitleCase(ch));
                    capitalizeNext = false;
                } else {
                    buffer.append(ch);
                }
            }
            return buffer.toString();
        }
        return str;
    }

    public static @NotNull String capitalizeFirstLetter(@NotNull String original) {
        if (original.isEmpty()) return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Contract(pure = true)
    public static @NotNull List<String> getByPartialMatches(@NotNull List<String> original, @NotNull String token) {
        String tokenLowerCase = token.toLowerCase();
        List<String> matches = new ArrayList<>();
        org.bukkit.util.StringUtil.copyPartialMatches(tokenLowerCase, original, matches);
        return matches;
    }

    public static @NotNull String extractCommandName(@NotNull String command) {
        String commandName = Colorizer.strip(command).split(" ")[0].substring(1);
        String[] pluginPrefix = commandName.split(":");
        if (pluginPrefix.length == 2) {
            commandName = pluginPrefix[1];
        }
        return commandName;
    }

    @Deprecated
    public static boolean isCustomBoolean(@NotNull String str) {
        String[] customs = new String[]{"0", "1", "on", "off", "true", "false", "yes", "no"};
        return Stream.of(customs).collect(Collectors.toSet()).contains(str.toLowerCase());
    }

    @Deprecated
    public static boolean parseCustomBoolean(@NotNull String str) {
        if (str.equalsIgnoreCase("0") || str.equalsIgnoreCase("off") || str.equalsIgnoreCase("no")) {
            return false;
        }
        if (str.equalsIgnoreCase("1") || str.equalsIgnoreCase("on") || str.equalsIgnoreCase("yes")) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }
}
