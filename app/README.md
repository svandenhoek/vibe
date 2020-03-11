# The application (for developers)

Instructions regarding how to build/test the Java application. If you're only interested in how to use the application when an executable jar file has already been provided, please refer to [the main readme](../README.md).

**IMPORTANT:** Be sure to `cd` to the `vibe/app/` directory before running any of the commands below.

## Requirements

- Apache Maven ([download][maven_download] and [install][maven_install])

## Preperations

Before building/testing, be sure the needed test resources are downloaded. This can be done by running `TestsPreprocessor.sh`. When checking out a commit that uses a different version of the test resources (as defined by the `pom.xml` properties `vibe-tdb.version` and `hpo-owl.tag`), be sure to re-run this script so that the correct resources are used for testing.

If storage space is an issue and the TDB is stored elsewhere as well, `ln -s </path/to/TDB> src/test/resources/tdb` can be used instead for the TDB.

## Building executables

1. Run `mvn clean install` to create all needed files.
   - A thin jar can be found at `target/vibe<version>.jar`.
   - An überjar can be found at `target/vibe-<version>-with-dependencies.jar` .
   - Checksums are available in `target/checksums.csv` as well as in `target/cechksums/` individually for the formats:
     - MD5
     - SHA-512
     - SHA3-512
2. Run `mvn dockerfile:build` to create a local docker image.







[maven_download]:https://maven.apache.org/download.cgi
[maven_install]:https://maven.apache.org/install.html

