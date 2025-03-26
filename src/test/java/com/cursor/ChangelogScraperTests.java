package com.cursor;

import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangelogScraperTests {

    private ChangelogScraper scraper;

    @BeforeEach
    void setUp() {
        scraper = new ChangelogScraper();
    }

    @Test
    void testContainsVersionInfo() {
        assertTrue(scraper.containsVersionInfo("Version 1.0"));
        assertTrue(scraper.containsVersionInfo("v1.0.0"));
        assertTrue(scraper.containsVersionInfo("Release v.1.2.3"));
        assertFalse(scraper.containsVersionInfo("New Features"));
        assertFalse(scraper.containsVersionInfo("Bug Fixes"));
    }

    @Test
    void testExtractVersion() {
        assertEquals("1.0.0", scraper.extractVersion("Version 1.0.0"));
        assertEquals("1.2.3", scraper.extractVersion("v1.2.3"));
        assertEquals("2.1", scraper.extractVersion("Release 2.1"));
        assertEquals("1.0.0-beta.1", scraper.extractVersion("Version 1.0.0-beta.1"));
        assertNull(scraper.extractVersion("No version here"));
    }

    @Test
    void testCleanText() {
        assertEquals("hello world", scraper.cleanText("hello   world"));
        assertEquals("test text", scraper.cleanText("test    text   "));
        assertEquals("single word", scraper.cleanText("single     word"));
        assertNull(scraper.cleanText(null));
        assertEquals("", scraper.cleanText("   "));
    }
}
