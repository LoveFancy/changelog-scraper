package com.cursor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChangelogScraperTest {

    @Test
    void cleanText_ShouldRemoveExtraWhitespace() {
        ChangelogScraper scraper = new ChangelogScraper();
        
        // Test cases
        assertEquals("Hello World", scraper.cleanText("Hello   World"));
        assertEquals("Hello World", scraper.cleanText("  Hello World  "));
        assertEquals("Hello World", scraper.cleanText("\tHello\nWorld\r"));
        assertEquals("", scraper.cleanText("   "));
        assertEquals("", scraper.cleanText(""));
        assertEquals("Hello World", scraper.cleanText("Hello World"));
    }

    @Test
    void cleanText_ShouldHandleNullInput() {
        ChangelogScraper scraper = new ChangelogScraper();
        assertNull(scraper.cleanText(null));
    }
} 