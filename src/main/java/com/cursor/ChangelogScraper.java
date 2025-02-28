package com.cursor;

import com.cursor.model.ChangelogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangelogScraper {
    private static final String URL = "https://www.cursor.com/cn/changelog";
    private static final Pattern VERSION_PATTERN = Pattern.compile("([0-9]+\\.[0-9]+(?:\\.[0-9]+)?(?:-[a-zA-Z]+(?:\\.[0-9]+)?)?)");
    private static final Pattern DATE_PATTERN = Pattern.compile("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(\\d{1,2}),?\\s+(\\d{4})");
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

                ChangelogEntry entry = new ChangelogEntry();
                
                // 解析版本号
                String version = extractVersion(block.text());
                if (version == null) {
                    continue;
                }
                entry.setVersion(version);

                // 解析日期
                String date = extractDate(block);
                entry.setDate(date);

                // 解析内容
                List<String> content = new ArrayList<>();
                List<String> simpleUpdates = new ArrayList<>();
                Element current = block.nextElementSibling();

                while (current != null && !current.is("h1, h2")) {
                    if (current.is("p, ul, li")) {
                        String text = cleanText(current.text());
                        if (StringUtils.isNotBlank(text)) {
                            // 检查是否包含UPDATE信息
                            Matcher updateMatcher = UPDATE_PATTERN.matcher(text);
                            if (updateMatcher.find()) {
                                entry.setUpdateVersions(updateMatcher.group(1).trim());
                                entry.setUpdateDescription(updateMatcher.group(2).trim());
                            }
                            
                            // 检查是否是简单更新说明
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

                entry.setContent(content);
                if (!simpleUpdates.isEmpty()) {
                    entry.setSimpleUpdates(simpleUpdates);
                }

                entries.add(entry);
            }

            // 保存为JSON文件
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File("cursor_changelog.json"), entries);

            System.out.println("成功抓取 " + entries.size() + " 条更新记录");
            return entries;

        } catch (Exception e) {
            System.err.println("抓取失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean containsVersionInfo(String text) {
        text = text.toLowerCase();
        return text.contains("v") || text.contains(".") || text.contains("version");
    }

    private String extractVersion(String text) {
        Matcher matcher = VERSION_PATTERN.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractDate(Element block) {
        Element article = block.parent();
        if (article == null) {
            return "N/A";
        }

        Elements dateDivs = article.select("div.inline-flex.items-center.font-mono");
        for (Element div : dateDivs) {
            Element dateP = div.selectFirst("p.uppercase");
            if (dateP != null) {
                String dateText = cleanText(dateP.text());
                Matcher matcher = DATE_PATTERN.matcher(dateText);
                if (matcher.find()) {
                    String month = MONTH_MAP.get(matcher.group(1));
                    String day = String.format("%02d", Integer.parseInt(matcher.group(2)));
                    String year = matcher.group(3);
                    return year + "-" + month + "-" + day;
                }
            }
        }
        return "N/A";
    }

    private String cleanText(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public static void main(String[] args) {
        new ChangelogScraper().scrapeChangelog();
    }
} 