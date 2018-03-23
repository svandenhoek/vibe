# vibe
Variant Interpretation using Biological Evidence

## Requirements
For building/testing:

* [Apache Maven][maven_download]
* [Apache Jena v3.6.0][jena_download]
* [Vibe test resource archive v2018-03-02]([vibe_resource_files])

For generating a local TDB dataset needed by the application:

* [Apache Jena v3.6.0][jena_download]
* [DisGeNET RDF v5 dump][disgenet_rdf_v5_dump]
* [Semanticscience Integrated Ontology (SIO)][sio_owl]
* [Human Phenotype Ontology (HPO)][hpo_owl]

For running the application:

* [Java 8][java_download]
* A local TDB dataset (see above).

## Preparations
Before using the tool, be sure all steps below are done (certain steps can be skipped if already present/configured):

### Creating an executable jar

1. [Download][maven_download] and [install][maven_install] Apache Maven
2. Run `mvn clean install` from the git repository directory.

### Preperations for unit-testing.

1. [Download][jena_download] and [configure][jena_configure] the environment so that the Jena scripts can be used.
2. Run `TestNGPreprocessing.sh` (optionally with extra arguments required for certain tests).


### Creating a local TDB dataset.

1. [Download][jena_download] and [configure][jena_configure] the environment so that the Jena scripts can be used.
2. Download the required files ([DisGeNET][disgenet_rdf_v5_dump], [SIO][sio_owl], [HPO][hpo_owl]).
3. Run `tdbloader2 --loc /path/to/store/TDB /path/to/disgenet/dump/*.ttl /path/to/sio-release.owl /path/to/hp.owl`

Tip: If all files are in the same directory, simply use: `cd /directory/of/input/files && tdbloader2 --loc /path/to/store/TDB *.ttl *.owl`

## Running the application

`java -jar vibe-with-dependencies.jar [-h] [-v] -d <DIR> -p <HPO ID> -o <FILE>` 

[java_download]:https://www.java.com/download
[maven_download]:https://maven.apache.org/download.cgi
[maven_install]:https://maven.apache.org/install.html
[jena_download]:https://jena.apache.org/download/index.cgi
[jena_configure]:https://jena.apache.org/documentation/tdb/commands.html#scripts
[disgenet_rdf_v5_dump]:http://rdf.disgenet.org/download/v5.0.0/disgenetv5.0-rdf-v5.0.0-dump.tar.gz
[sio_owl]:http://semanticscience.org/ontology/sio.owl
[hpo_owl]:http://purl.obolibrary.org/obo/hp.owl
[vibe_resource_files]:https://molgenis26.target.rug.nl/downloads/vibe/