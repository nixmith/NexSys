package com.nexsys.util;

import com.nexsys.util.language.Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LanguageUtilsTest {

    @Nested
    @DisplayName("isLanguageMatch")
    class IsLanguageMatchTest {

        @Test
        void testExactMatch() {
            assertTrue(LanguageUtils.isLanguageMatch("en", "en"));
            assertTrue(LanguageUtils.isLanguageMatch("fr", "fr"));
            assertTrue(LanguageUtils.isLanguageMatch("de", "de"));
        }

        @Test
        void testNorwegianAliases() {
            assertTrue(LanguageUtils.isLanguageMatch("nb", "no"));
            assertTrue(LanguageUtils.isLanguageMatch("no", "nb"));
        }

        @Test
        void testHebrewAliases() {
            assertTrue(LanguageUtils.isLanguageMatch("he", "iw"));
            assertTrue(LanguageUtils.isLanguageMatch("iw", "he"));
        }

        @Test
        void testNonMatches() {
            assertFalse(LanguageUtils.isLanguageMatch("en", "fr"));
            assertFalse(LanguageUtils.isLanguageMatch("de", "es"));
            assertFalse(LanguageUtils.isLanguageMatch("nb", "sv"));
        }
    }

    @Nested
    @DisplayName("isRegion")
    class IsRegionTest {

        @Test
        void testSpanishLatinAmerica() {
            assertFalse(LanguageUtils.isRegion("es", "419")); // 419 is Latin America code
            assertTrue(LanguageUtils.isRegion("es", "ES"));
            assertTrue(LanguageUtils.isRegion("es", "MX"));
        }

        @Test
        void testSerbianScript() {
            assertFalse(LanguageUtils.isRegion("sr", "Latn"));
            assertFalse(LanguageUtils.isRegion("sr", "latn")); // Case insensitive
            assertTrue(LanguageUtils.isRegion("sr", "RS"));
        }

        @Test
        void testChineseScripts() {
            assertFalse(LanguageUtils.isRegion("zh", "Hans"));
            assertFalse(LanguageUtils.isRegion("zh", "Hant"));
            assertFalse(LanguageUtils.isRegion("zh", "hans")); // Case insensitive
            assertTrue(LanguageUtils.isRegion("zh", "CN"));
            assertTrue(LanguageUtils.isRegion("zh", "TW"));
        }

        @Test
        void testOtherLanguages() {
            assertTrue(LanguageUtils.isRegion("en", "US"));
            assertTrue(LanguageUtils.isRegion("en", "GB"));
            assertTrue(LanguageUtils.isRegion("fr", "FR"));
            assertTrue(LanguageUtils.isRegion("de", "DE"));
        }
    }

    @Nested
    @DisplayName("preferredRegions")
    class PreferredRegionsTest {

        @Test
        void testEnglishWithoutCountryHint() {
            List<String> regions = LanguageUtils.preferredRegions("en", null, null);
            assertEquals(List.of("US", "EN"), regions);
        }

        @Test
        void testEnglishWithCountryHint() {
            List<String> regions = LanguageUtils.preferredRegions("en", "GB", null);
            assertEquals(List.of("GB", "EN"), regions);
        }

        @Test
        void testChineseSimplified() {
            List<String> regions = LanguageUtils.preferredRegions("zh", null, null);
            assertEquals(List.of("CN", "ZH"), regions);
        }

        @Test
        void testChineseTraditional() {
            List<String> regions = LanguageUtils.preferredRegions("zh", null, "Hant");
            assertEquals(List.of("HK", "TW", "ZH"), regions);
        }

        @Test
        void testChineseTraditionalWithCountryHint() {
            List<String> regions = LanguageUtils.preferredRegions("zh", "SG", "Hant");
            assertEquals(List.of("SG", "HK", "TW", "ZH"), regions);
        }

        @Test
        void testOtherLanguages() {
            assertEquals(List.of("FR"), LanguageUtils.preferredRegions("fr", null, null));
            assertEquals(List.of("CA", "FR"), LanguageUtils.preferredRegions("fr", "CA", null));
            assertEquals(List.of("DE"), LanguageUtils.preferredRegions("de", null, null));
        }
    }

    @Nested
    @DisplayName("matches")
    class MatchesTest {

        @Test
        void testMatchAll() {
            List<String> supported = List.of("en-US", "fr-FR", "de-DE");
            List<String> result = LanguageUtils.matches("*", supported, null);
            assertEquals(supported, result);
        }

        @Test
        void testExactMatch() {
            List<String> result = LanguageUtils.matches("en-US",
                    List.of("en-GB", "en-US", "fr-FR"), null);
            assertEquals("en-US", result.get(0));
            assertTrue(result.contains("en-GB")); // Also matches language
            assertFalse(result.contains("fr-FR")); // Different language
        }

        @Test
        void testLanguageOnlyMatch() {
            List<String> result = LanguageUtils.matches("en",
                    List.of("en-US", "en-GB", "fr-FR"), null);
            assertEquals(2, result.size());
            assertTrue(result.containsAll(List.of("en-US", "en-GB")));
        }

        @Test
        void testCountryHintInfluence() {
            List<String> result = LanguageUtils.matches("en",
                    List.of("en-US", "en-GB", "en-AU"), "GB");
            assertEquals("en-GB", result.get(0)); // GB should be first due to hint
        }

        @Test
        void testNorwegianMatching() {
            List<String> result = LanguageUtils.matches("no",
                    List.of("nb", "sv", "da"), null);
            assertTrue(result.contains("nb"));
            assertFalse(result.contains("sv"));
            assertFalse(result.contains("da"));
        }

        @Test
        void testNoMatches() {
            List<String> result = LanguageUtils.matches("en",
                    List.of("fr-FR", "de-DE", "es-ES"), null);
            assertTrue(result.isEmpty());
        }

        @Test
        void testChineseVariants() {
            // Traditional Chinese should match other Traditional variants
            List<String> result = LanguageUtils.matches("zh-Hant",
                    List.of("zh-CN", "zh-TW", "zh-HK"), null);
            // Should prefer TW and HK for Traditional
            assertTrue(result.size() >= 2);
            assertTrue(result.contains("zh-TW") || result.contains("zh-HK"));
        }
    }

    @Nested
    @DisplayName("intersect")
    class IntersectTest {

        @Test
        void testSimpleIntersection() {
            Set<String> first = Set.of("en", "fr", "de");
            Set<String> second = Set.of("en", "es", "de");
            Set<String> result = LanguageUtils.intersect(first, second);
            assertEquals(Set.of("en", "de"), result);
        }

        @Test
        void testAliasedIntersection() {
            Set<String> first = Set.of("nb", "en");
            Set<String> second = Set.of("no", "fr");
            Set<String> result = LanguageUtils.intersect(first, second);
            assertEquals(Set.of("nb"), result); // nb matches no
        }

        @Test
        void testHebrewAliasIntersection() {
            Set<String> first = Set.of("he", "en");
            Set<String> second = Set.of("iw", "de");
            Set<String> result = LanguageUtils.intersect(first, second);
            assertEquals(Set.of("he"), result); // he matches iw
        }

        @Test
        void testNoIntersection() {
            Set<String> first = Set.of("en", "fr");
            Set<String> second = Set.of("de", "es");
            Set<String> result = LanguageUtils.intersect(first, second);
            assertTrue(result.isEmpty());
        }

        @Test
        void testEmptySets() {
            assertEquals(Set.of(), LanguageUtils.intersect(Set.of(), Set.of("en")));
            assertEquals(Set.of(), LanguageUtils.intersect(Set.of("en"), Set.of()));
            assertEquals(Set.of(), LanguageUtils.intersect(Set.of(), Set.of()));
        }
    }

    @Nested
    @DisplayName("Dialect parsing and scoring")
    class DialectTest {

        @Test
        void testDialectParsing() {
            Dialect d1 = Dialect.parse("en-US");
            assertEquals("en", d1.language());
            assertEquals("US", d1.region());
            assertNull(d1.code());

            Dialect d2 = Dialect.parse("zh-Hant");
            assertEquals("zh", d2.language());
            assertNull(d2.region());
            assertEquals("Hant", d2.code());

            Dialect d3 = Dialect.parse("es-419");
            assertEquals("es", d3.language());
            assertNull(d3.region());
            assertEquals("419", d3.code());

            Dialect d4 = Dialect.parse("sr_Latn");
            assertEquals("sr", d4.language());
            assertNull(d4.region());
            assertEquals("Latn", d4.code());
        }

        @Test
        void testDialectCaseNormalization() {
            Dialect d = Dialect.parse("EN-us");
            assertEquals("en", d.language());
            assertEquals("US", d.region());
        }

        @Test
        void testDialectScoring() {
            Dialect target = Dialect.parse("en-US");
            Dialect exact = Dialect.parse("en-US");
            Dialect langOnly = Dialect.parse("en");
            Dialect different = Dialect.parse("fr-FR");

            // Exact match should score highest
            assertTrue(target.score(exact, null).primary() >
                    target.score(langOnly, null).primary());

            // Different language should not match
            assertEquals(-1, target.score(different, null).primary());
        }

        @Test
        void testScoreComparison() {
            Dialect.Score high = new Dialect.Score(10, 1);
            Dialect.Score low = new Dialect.Score(5, 2);
            Dialect.Score same = new Dialect.Score(10, 0);

            assertTrue(high.compareTo(low) > 0);
            assertTrue(low.compareTo(high) < 0);
            assertTrue(high.compareTo(same) > 0); // Secondary score matters
        }
    }
}