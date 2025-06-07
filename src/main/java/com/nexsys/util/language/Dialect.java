package com.nexsys.util.language;

import com.nexsys.util.LanguageUtils;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a language dialect with optional region and script/code components.
 * <p>
 * Parses language tags like "en-US" (language-region), "zh-Hant" (language-script),
 * or "sr_Latn" (language-script with underscore separator).
 */
public record Dialect(String language, String region, String code) {

    private static final Pattern TAG_SEPARATOR = Pattern.compile("[-_]");

    /**
     * Canonical constructor that normalizes the case of components.
     * Languages are lowercased, regions are uppercased.
     */
    public Dialect {
        language = language == null ? null : language.toLowerCase();
        region   = region   == null ? null : region.toUpperCase();
    }

    /**
     * Parse a language tag into its components.
     * <p>
     * Examples:
     * - "en-US" → language="en", region="US"
     * - "zh-Hant" → language="zh", code="Hant"
     * - "es-419" → language="es", code="419" (Latin America)
     * - "sr_Latn" → language="sr", code="Latn" (Latin script)
     *
     * @param tag Language tag to parse
     * @return Parsed dialect with normalized components
     */
    public static Dialect parse(String tag) {
        var parts = TAG_SEPARATOR.split(tag, 2);
        String lang = parts[0];
        String region = null;
        String code = null;

        if (parts.length > 1) {
            String second = parts[1];
            if (LanguageUtils.isRegion(lang, second)) {
                region = second;
            } else {
                code = second;
            }
        }
        return new Dialect(lang, region, code);
    }

    /**
     * Score how well another dialect matches this one.
     * <p>
     * The scoring algorithm prioritizes:
     * 1. Exact language and region matches (highest score)
     * 2. Matching preferred regions based on country hint
     * 3. Language-only matches (lowest positive score)
     * <p>
     * Returns NO_MATCH (negative score) for non-matching languages or conflicting regions.
     *
     * @param other       Dialect to compare against
     * @param countryHint Optional country code to influence region preferences
     * @return Score with primary and secondary components for sorting
     */
    public Score score(Dialect other, String countryHint) {
        // Different languages never match
        if (!LanguageUtils.isLanguageMatch(language, other.language)) {
            return Score.NO_MATCH;
        }

        boolean exactLang = language.equals(other.language);

        // Both have no region - simple language match
        if (region == null && other.region == null) {
            return new Score(exactLang ? 2 : 1, 0);
        }

        // Both have regions
        if (region != null && other.region != null) {
            if (region.equals(other.region)) {
                // Perfect match gets infinity to sort first
                return new Score(Double.POSITIVE_INFINITY, exactLang ? 1 : 0);
            }
            // Different regions but same language - still a match but lower score
            return new Score(0.5, 0);
        }

        // One has region, one doesn't - check preferred regions
        List<String> pref = LanguageUtils.preferredRegions(language, countryHint, code);

        // If this dialect has no region (language-only query)
        if (region == null && other.region != null) {
            // Check if other's region is in preferred list
            int idx = pref.indexOf(other.region);
            if (idx >= 0) {
                // Score based on preference order
                return new Score(3 + (pref.size() - idx), 0);
            }
            // Still a match, but lower score for non-preferred region
            return new Score(1, 0);
        }

        // If this dialect has a region but other doesn't
        if (region != null && other.region == null) {
            // Check if our region is in preferred list
            int idx = pref.indexOf(region);
            if (idx >= 0) {
                return new Score(2 + (pref.size() - idx), 0);
            }
            // Language matches but region not preferred
            return new Score(1, 0);
        }

        return Score.NO_MATCH;
    }

    /**
     * Score tuple for sorting dialects by match quality.
     * Higher scores indicate better matches.
     */
    public record Score(double primary, double secondary)
            implements Comparable<Score> {

        /** Sentinel value for non-matching dialects */
        static final Score NO_MATCH = new Score(-1, 0);

        @Override
        public int compareTo(Score o) {
            int cmp = Double.compare(primary, o.primary);
            return cmp != 0 ? cmp : Double.compare(secondary, o.secondary);
        }
    }
}