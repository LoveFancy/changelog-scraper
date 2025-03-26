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
        assertTrue(scraper.containsVersionInfo("3333"));
        assertFalse(scraper.containsVersionInfo("Version 1.0"));
        assertFalse(scraper.containsVersionInfo("v1.0.0"));
        assertFalse(scraper.containsVersionInfo("New Features"));
        assertFalse(scraper.containsVersionInfo(""));

        // Handle null input
        assertThrows(NullPointerException.class, () -> scraper.containsVersionInfo(null));
    }

    @Test
    void testExtractVersion() {
        assertEquals("1.0.0", scraper.extractVersion("Version 1.0.0"));
        assertEquals("1.2.3", scraper.extractVersion("v1.2.3"));
        assertEquals("2.1", scraper.extractVersion("Release 2.1"));
        assertEquals("1.0.0-beta.1", scraper.extractVersion("Version 1.0.0-beta.1"));
        assertNull(scraper.extractVersion("No version here"));
        assertNull(scraper.extractVersion(""));
        assertThrows(NullPointerException.class, () -> scraper.extractVersion(null));
    }

    @Test
    void testExtractDate() {
        Element element = new Element(Tag.valueOf("h1"), "");
        element.text("March 15th, 2024");
        assertEquals("2024-03-15", scraper.extractDate(element));

        element.text("Dec 1st, 2023");
        assertEquals("2023-12-01", scraper.extractDate(element));

        element.text("No date here");
        assertEquals("N/A", scraper.extractDate(element));

        element.text("January 31, 2024");
        assertEquals("2024-01-31", scraper.extractDate(element));

        // Test with parent element containing date
        Element parent = new Element(Tag.valueOf("div"), "");
        Element time = new Element(Tag.valueOf("time"), "");
        time.text("May 1st, 2024");
        parent.appendChild(time);
        element.appendTo(parent);
        assertEquals("2024-05-01", scraper.extractDate(element));

        // Test with sibling element containing date
        Element sibling = new Element(Tag.valueOf("p"), "");
        sibling.text("June 15th, 2024");
        parent.appendChild(sibling);
        assertEquals("2024-05-01", scraper.extractDate(element));
    }

    @Test
    void testCleanText() {
        assertEquals("hello world", scraper.cleanText("hello   world"));
        assertEquals("test text", scraper.cleanText("test    text   "));
        assertEquals("single word", scraper.cleanText("single     word"));
        assertNull(scraper.cleanText(null));
        assertEquals("", scraper.cleanText("   "));
        assertEquals("test", scraper.cleanText("\ttest\n"));
        assertEquals("multiple spaces test", scraper.cleanText("multiple    spaces     test"));
        assertEquals("", scraper.cleanText(""));
    }
}
