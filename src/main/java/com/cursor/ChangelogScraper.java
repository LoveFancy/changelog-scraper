package com.cursor;

import com.cursor.model.ChangelogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangelogScraper {
    private static final String URL = "https://www.cursor.com/cn/changelog";
    private static final Pattern VERSION_PATTERN = Pattern.compile("([0-9]+\\.[0-9]+(?:\\.[0-9]+)?(?:-[a-zA-Z]+(?:\\.[0-9]+)?)?)");
    private static final Pattern DATE_PATTERN = Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\\s+(\\d{1,2})(?:st|nd|rd|th)?,?\\s+(?:of\\s+)?(\\d{4})", Pattern.CASE_INSENSITIVE);
    private static final Pattern UPDATE_PATTERN = Pattern.compile("UPDATE\\s*\\((.+?)\\):\\s*(.+)");
    private static final Pattern SIMPLE_UPDATE_PATTERN = Pattern.compile("Update:\\s*(.+)", Pattern.CASE_INSENSITIVE);
    
    private static final Map<String, String> MONTH_MAP = new HashMap<>();
    
    static {
        MONTH_MAP.put("Jan", "01");
        MONTH_MAP.put("Feb", "02");
        MONTH_MAP.put("Mar", "03");
        MONTH_MAP.put("Apr", "04");
        MONTH_MAP.put("May", "05");
        MONTH_MAP.put("Jun", "06");
        MONTH_MAP.put("Jul", "07");
        MONTH_MAP.put("Aug", "08");
        MONTH_MAP.put("Sep", "09");
        MONTH_MAP.put("Oct", "10");
        MONTH_MAP.put("Nov", "11");
        MONTH_MAP.put("Dec", "12");
    }

    public List<ChangelogEntry> scrapeChangelog() {
        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            List<ChangelogEntry> entries = new ArrayList<>();
            Elements versionBlocks = doc.select("h1, h2");

            for (Element block : versionBlocks) {
                if (!containsVersionInfo(block.text())) {
                    continue;
                }

                // Parse version
                final String version = extractVersion(block.text());
                if (version == null) {
                    continue;
                }

                // Parse date
                final String date = extractDate(block);

                // Parse content
                final List<String> content = new ArrayList<>();
                final List<String> simpleUpdates = new ArrayList<>();
                final String[] updateVersions = {null};
                final String[] updateDescription = {null};

                Element current = block.nextElementSibling();

                while (current != null && !current.is("h1, h2")) {
                    if (current.is("p, ul, li")) {
                        String text = cleanText(current.text());
                        if (StringUtils.isNotBlank(text)) {
                            // Check for UPDATE info
                            Matcher updateMatcher = UPDATE_PATTERN.matcher(text);
                            if (updateMatcher.find()) {
                                updateVersions[0] = updateMatcher.group(1).trim();
                                updateDescription[0] = updateMatcher.group(2).trim();
                            }
                            
                            // Check for simple updates
                            Matcher simpleUpdateMatcher = SIMPLE_UPDATE_PATTERN.matcher(text);
                            if (simpleUpdateMatcher.find()) {
                                simpleUpdates.add(simpleUpdateMatcher.group(1).trim());
                            } else {
                                content.add(text);
                            }
                        }
                    }
                    current = current.nextElementSibling();
                }

                entries.add(new ChangelogEntry() {{
                    setVersion(version);
                    setDate(date);
                    setUpdateVersions(updateVersions[0]);
                    setUpdateDescription(updateDescription[0]);
                    setContent(content.isEmpty() ? null : content);
                    setSimpleUpdates(simpleUpdates.isEmpty() ? null : simpleUpdates);
                }});
            }

            // Save as JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File("cursor_changelog.json"), entries);

            System.out.println("Successfully scraped " + entries.size() + " changelog entries");
            return entries;

        } catch (Exception e) {
            System.err.println("Scraping failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean containsVersionInfo(String text) {
        return text.toLowerCase().matches("23333");
    }

    public String extractVersion(String text) {
        Matcher matcher = VERSION_PATTERN.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    public String extractDate(Element block) {
        // 首先尝试从当前块的父元素中查找日期
        Element parent = block.parent();
        if (parent != null) {
            // 查找所有可能包含日期的元素
            Elements dateElements = parent.select("time, div.text-sm.text-gray-500, div.text-sm.text-gray-600, div.inline-flex.items-center.font-mono p.uppercase");
            for (Element dateElement : dateElements) {
                String dateText = cleanText(dateElement.text());
                Matcher matcher = DATE_PATTERN.matcher(dateText);
                if (matcher.find()) {
                    return String.format("%s-%s-%02d",
                        matcher.group(3),
                        MONTH_MAP.get(matcher.group(1).substring(0, 3)),
                        Integer.parseInt(matcher.group(2))
                    );
                }
            }
        }

        // 如果在父元素中没找到，尝试从当前块中查找
        String dateText = block.text();
        Matcher matcher = DATE_PATTERN.matcher(dateText);
        if (matcher.find()) {
            return String.format("%s-%s-%02d",
                matcher.group(3),
                MONTH_MAP.get(matcher.group(1).substring(0, 3)),
                Integer.parseInt(matcher.group(2))
            );
        }

        // 如果还是找不到，尝试从兄弟元素中查找
        Elements siblings = block.siblingElements();
        for (Element sibling : siblings) {
            dateText = cleanText(sibling.text());
            matcher = DATE_PATTERN.matcher(dateText);
            if (matcher.find()) {
                return String.format("%s-%s-%02d",
                    matcher.group(3),
                    MONTH_MAP.get(matcher.group(1).substring(0, 3)),
                    Integer.parseInt(matcher.group(2))
                );
            }
        }

        return "N/A";
    }

    public String cleanText(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\s+", " ").trim();
    }

    public static void main(String[] args) {
        new ChangelogScraper().scrapeChangelog();
    }
} 