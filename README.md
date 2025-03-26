# Changelog Scraper

A tool to scrape and parse changelog information from Cursor's website.

## Features
- Scrapes changelog data from Cursor's website
- Extracts version numbers and dates
- Handles multiple date formats
- Identifies and categorizes different types of updates
- Saves data in a structured JSON format

## Requirements
- Java 8 or higher
- Maven

## Dependencies
- JSoup (HTML parsing)
- Jackson (JSON handling)
- Apache Commons Lang (text processing)
- Lombok (reducing boilerplate code)

## Usage
1. Clone the repository
2. Build the project:
```bash
mvn clean compile
```
3. Run the scraper:
```bash
mvn exec:java -Dexec.mainClass="com.cursor.ChangelogScraper"
```

## Output Format
The scraper generates a `cursor_changelog.json` file with the following structure:
```json
[
  {
    "version": "1.93.1",
    "date": "2024-10-09",
    "updateVersions": "0.42.1 - 0.42.5",
    "updateDescription": "Security fixes and improvements",
    "content": [
      "Fixed security vulnerability CVE-2024-43601",
      "Improved composer functionality"
    ],
    "simpleUpdates": [
      "Added new UI elements",
      "Fixed performance issues"
    ]
  }
]
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 