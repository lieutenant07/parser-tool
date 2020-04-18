## Parser-tool 
Implementation of CSV/PRN (delimited/fixed-width) text parsers.

## Build project
In order to build the project run

```bash
$ mvn clean install
```

## Parse CSV/PRN

This API has been exposed via Command Line Interface. We can start java process with command:

```bash
$ java -jar target/parser-tool-0.0.1-SNAPSHOT.jar
```

, but first we need to provide args/options:

Args:
* `parseCsv` / `parsePrn`        [required, parse CSV/PRN file]

Options:
* --filePath=/path/to/file.csv   [required, relative path to CSV/PRN file]
* --exportHtml                   [optional, exports parsed document to HTML format in the 'output' folder]
* --verbose                      [optional, print parsed records in the console]

## Example usages 

```bash
$ java -jar target/parser-tool-0.0.1-SNAPSHOT.jar parseCsv --filePath=input/Workbook2UTF8.csv --verbose --exportHtml
```

```bash
$ java -jar target/parser-tool-0.0.1-SNAPSHOT.jar parsePrn --filePath=input/Workbook2UTF8.prn --verbose --exportHtml
```

## Remarks 
Has been tested on linux and windows envs. Due to problems with system timezones they have been unified across the application and it is set to `GMT`.

Provided files Workbook2.csv and WorkBook2.prn had wrong encoding set to `iso-8859-1` which prevented from parsing special characters such as `ø` or `ß`.
In order to ensure proper parsing encoding on copied files has been changed to `utf-8` using following commands:

```bash
$ iconv -f iso-8859-1 -t utf-8 -o Workbook2UTF8.csv Workbook2.csv
```

```bash
$ iconv -f iso-8859-1 -t utf-8 -o Workbook2UTF8.prn Workbook2.prn
```

Also, I encountered problems with univocity lib in latest releases of `2.7.x` - parser wasn't reading records into POJOs on windows env, even though on linux (Ubuntu) it worked just fine. 
As a workaround I changed implementation and rolled back univocity to `2.1.1` which works just fine.
