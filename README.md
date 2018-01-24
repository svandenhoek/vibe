# vibe
Variant Interpretation using Biological Evidence

## Requirements
For building/testing:

* [Apache Maven](https://maven.apache.org/)
* Resource files (URL coming soon)

For running the application:

* [Java 8](https://www.java.com/download)
* [Apache Jena](https://jena.apache.org/download/index.cgi)
* [DisGeNET RDF v5](http://rdf.disgenet.org/download/v5.0.0/disgenetv5.0-rdf-v5.0.0-dump.tar.gz)

## Preparations
Before using the tool, be sure all steps below are done (certain steps can be skipped if already present/configured):

### Creating an executable jar

1. [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html) Apache Maven
2. Run `mvn clean install` from the git repository directory.

### Creating a Triple store database from the RDF/OWL data.

1. [Download](https://jena.apache.org/download/index.cgi) and [configure](https://jena.apache.org/documentation/tdb/commands.html#scripts) the environment so that the Jena scripts can be used.
2. Run `tdbloader2 --loc <directory to store TDB in> <file to create TDB from>...`

Tip: When loading multiple files that are stored in a single directory, use: `/path/to/dir/*`

## Running the application

`java -jar vibe-with-dependencies.jar` 