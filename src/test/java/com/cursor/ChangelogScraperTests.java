package com.cursor;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

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
    void testExtractDate() {
        Element article = new Element(Tag.valueOf("article"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        div.addClass("inline-flex items-center font-mono");
        Element p = new Element(Tag.valueOf("p"), "");
        p.addClass("uppercase");
        p.text("Jan 15, 2024");
        div.appendChild(p);
        article.appendChild(div);
        Element h1 = new Element(Tag.valueOf("h1"), "");
        article.appendChild(h1);

        assertEquals("2024-01-15", scraper.extractDate(h1));

        // Test invalid date format
        p.text("Invalid date");
        assertEquals("N/A", scraper.extractDate(h1));

        // Test null parent
        Element orphanH1 = new Element(Tag.valueOf("h1"), "");
        assertEquals("N/A", scraper.extractDate(orphanH1));
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
