# Database creation

VIBE needs an optimized TDB to run. To create this, a script can be used that takes care of all these steps from downloading to the final optimized TDB. Note that it can take a while before all steps are finished. This script should be called from the directory where the data should be downloaded/TDBs should be created. Do **NOT** move the bash script from it's original position, as it uses the `pom.xml` file from the `app` directory for adding the version to the TDB which was used to create it. In general, the script should be called as follows:

```bash
/path/to/vibe/database/GenerateDatabase.sh -1
```

**Note that the initial TDB should not be used in combination with VIBE, but only the optimized one!** This is because certain information is left out in the optimized TDB to reduce the database size. Using VIBE with the initial TDB could result in unusual results or possibly even errors!

## Requirements

- Apache Jena ([download][jena_download] and [configure][jena_configure])

## For developers

In case adjustments are made to the TDB creation process, a new TDB can be created without having to re-download all the data all over again. See `-h` for more information about the different possible starting phases for the script.

## Windows users

Currently there isn't a bat script that offers automated TDB creation. Please use the already created TDB as available on [the molgenis download server][tdb_download].

[jena_download]: https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tools/#setting-up-your-environment
[tdb_download]: http://molgenis.org/downloads/vibe/vibe-v2_0_0-tdb.zip