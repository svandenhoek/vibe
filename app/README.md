# The application (for developers)

Instructions regarding how to build/test the Java application. If you're only interested in how to use the application when an executable jar file has already been provided, please refer to [the main readme](../README.md).

## Building an executable jar

### Requirements

- Apache Maven ([download][maven_download] and [install][maven_install])

### How-to

1. Run `mvn clean install` from the `app` directory in the git repository.

You can now use `target/vibe-with-dependencies.jar`.

## Run unit-tests

### Requirements

- Apache Jena ([download][jena_download] and [configure][jena_configure])
- Vibe test resources archive (see below)

### How-to

1. Download vibe test resources through running `TestNGPreprocessing.sh` (certain optional arguments might be required for specific tests).
2. Run unit-tests using preferred IDE.







[maven_download]:https://maven.apache.org/download.cgi
[maven_install]:https://maven.apache.org/install.html
[jena_download]: https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tools/#setting-up-your-environment