# Database creation

VIBE needs an optimized TDB to run. While a downloadable version is available (see [here](../README.md#quickstart)), it is possible to generate an archive as well. To do this, go to the directory in which the TDB should be created and run the following command:

```bash
/path/to/vibe/database/GenerateDatabase.sh
```

Do **NOT** move the bash script from it's original position, as it uses the `pom.xml` file from the `app` directory for adding the version to the TDB which was used to create it.

The script consists of different phases which are ran one after another. It is also possible to run a selection of the phases. Please view the `-h` option for more information about this.

**Note that the initial TDB should not be used in combination with VIBE, but only the optimized one!** This is because certain information is left out in the optimized TDB to reduce the database size. Using VIBE with the initial TDB could result in unusual results or possibly even errors!

## Requirements

- Apache Jena ([download][jena_download] and [configure][jena_configure])

## Windows users

Currently there isn't a bat script that offers automated TDB creation. Please use the already created TDB as available on [the molgenis download server][tdb_download].

## F.A.Q.

**Q:** Why do I get an `org.apache.jena.atlas.RuntimeIOException: java.nio.charset.MalformedInputException: Input length = 1` error when trying to create a TDB from the generated `.ttl` files?

**A:** This might be caused by the generated `.ttl` files having an incorrect file encoding. Please make sure the generated `.ttl` files have the encoding `UTF-8`. If this is not the case, manually change it to  `UTF_8`.

[jena_download]: https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tools/#setting-up-your-environment
[tdb_download]: http://molgenis.org/downloads/vibe/vibe-v2_0_0-tdb.zip