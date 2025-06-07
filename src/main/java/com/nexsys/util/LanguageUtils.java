package com.nexsys.util;

import com.nexsys.util.language.Dialect;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility methods for language matching and selection.
 * <p>
 * Provides intelligent language matching that handles:
 * - Language aliases (e.g., Norwegian "nb" and "no")
 * - Regional preferences based on country hints
 * - Script variants (e.g., Chinese Traditional/Simplified)
 */
public final class LanguageUtils {

    private LanguageUtils() {}   // utility class

    /** Wildcard to match all languages */
    public static final String MATCH_ALL = "*";

    /** Language codes that should be treated as equivalent */
    private static final Set<Set<String>> SAME_LANGUAGES = Set.of(
            Set.of("nb", "no"),  // Norwegian Bokmål and Norwegian
            Set.of("he", "iw")   // Hebrew new code and old code
    );

    /** Pattern for splitting language tags on hyphen or underscore */
    static final Pattern SEPARATOR = Pattern.compile("[-_]");

    /**
     * Check if two language codes should be considered the same.
     * <p>
     * Handles exact matches and known aliases like Norwegian variants.
     *
     * @param a First language code
     * @param b Second language code
     * @return true if the languages match or are aliases
     */
    public static boolean isLanguageMatch(String a, String b) {
        if (a.equals(b)) return true;
        // Normalize order for set lookup
        Set<String> pair = Set.of(a, b);
        return SAME_LANGUAGES.contains(pair);
    }

    /**
     * Determine if a token represents a region code vs a script/other code.
     * <p>
     * Some language tags use non-region identifiers:
     * - "es-419" uses 419 for Latin America (not a country)
     * - "sr-Latn" uses Latn for Latin script (not a region)
     * - "zh-Hans/Hant" uses Hans/Hant for Simplified/Traditional
     *
     * @param language The language code
     * @param token    The token to check
     * @return true if token represents a region, false if it's a script/code
     */
    public static boolean isRegion(String language, String token) {
        return switch (language) {
            case "es" -> !token.equals("419");              // 419 = Latin America
            case "sr" -> !token.equalsIgnoreCase("Latn");   // Latn = Latin script
            case "zh" -> !(token.equalsIgnoreCase("Hans") || token.equalsIgnoreCase("Hant")); // Scripts
            default -> true;
        };
    }

    /**
     * Get preferred regions for a language based on country hint and script.
     * <p>
     * Returns regions in order of preference:
     * 1. Country hint (if provided)
     * 2. Language-specific defaults (e.g., US for English, CN for Chinese)
     * 3. Language code as fallback region (e.g., FR for French)
     *
     * @param language    Language code
     * @param countryHint Optional country code to prioritize
     * @param codeHint    Optional script/code hint (e.g., "Hant" for Traditional Chinese)
     * @return Ordered list of preferred region codes
     */
    public static List<String> preferredRegions(
            String language, String countryHint, String codeHint) {

        List<String> regions = new ArrayList<>(4);
        if (countryHint != null) regions.add(countryHint.toUpperCase());

        switch (language) {
            case "en" -> {
                if (countryHint == null) regions.add("US");  // Default to US English
            }
            case "zh" -> {
                if ("Hant".equalsIgnoreCase(codeHint)) {
                    regions.addAll(List.of("HK", "TW"));      // Traditional Chinese regions
                } else {
                    regions.add("CN");                        // Simplified Chinese
                }
            }
            default -> {}
        }
        regions.add(language.toUpperCase());  // Fallback: language as region (e.g., fr → FR)
        return regions;
    }

    /**
     * Find supported language tags that match a target, ordered by quality.
     * <p>
     * Returns matches sorted by:
     * 1. Exact language and region matches
     * 2. Preferred regions based on country hint
     * 3. Language-only matches
     * <p>
     * Non-matching languages are excluded from results.
     *
     * @param target      Target language tag (or "*" to match all)
     * @param supported   Collection of available language tags
     * @param countryHint Optional country code to influence sorting
     * @return List of matching tags, best matches first
     */
    public static List<String> matches(
            String target, Collection<String> supported, String countryHint) {

        if (MATCH_ALL.equals(target)) {
            // Preserve original order for wildcard
            return new ArrayList<>(supported);
        }
        Dialect targetDialect = Dialect.parse(target);

        return supported.stream()
                .map(tag -> Map.entry(tag,
                        targetDialect.score(Dialect.parse(tag), countryHint)))
                .filter(e -> e.getValue().primary() >= 0)  // Exclude non-matches
                .sorted(Map.Entry.<String, Dialect.Score>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    /**
     * Find language codes present in both sets, considering aliases.
     * <p>
     * Unlike a simple set intersection, this method treats aliased
     * languages as equivalent (e.g., "nb" in first set matches "no" in second).
     *
     * @param first  First set of language codes
     * @param second Second set of language codes
     * @return Set containing languages from first that match any in second
     */
    public static Set<String> intersect(
            Set<String> first, Set<String> second) {

        Set<String> out = new HashSet<>();
        for (String l1 : first) {
            for (String l2 : second) {
                if (isLanguageMatch(l1, l2)) out.add(l1);
            }
        }
        return out;
    }
}