# Changelog Scraper

A tool to scrape and parse changelog information from Cursor's website. The project includes both Python and Java implementations.

## Features

- Scrapes changelog data from cursor.com
- Extracts version numbers, dates, and update content
- Supports both simple updates and detailed changelogs
- Handles multiple date formats
- Outputs data in JSON format

## Python Version

### Requirements
- Python 3.x
- BeautifulSoup4
- Requests

### Usage
```bash
python changelog_scraper.py
```

## Java Version

### Requirements
- JDK 1.8 or higher
- Maven (for dependency management)

### Dependencies
- JSoup (HTML parsing)
- Jackson (JSON handling)
- Apache Commons Lang3
- Lombok

### Usage
```bash
# Compile the project
mvn clean package

# Run the scraper
java -cp "target/classes:lib/*" com.cursor.ChangelogScraper
```

## Output Format

The scraped data is saved in `cursor_changelog.json` with the following structure:

```json
[
  {
    "version": "1.93.1",
    "date": "2024-10-09",
    "updateVersions": "0.42.1 - 0.42.5",
    "updateDescription": "...",
    "content": ["..."],
    "simpleUpdates": ["..."]
  }
]
```

## License

MIT License 