#!/usr/bin/env bash

echo "# Retrieving information from pom.xml"
readonly TDB_ARCHIVE=$(mvn -q help:evaluate -Dexpression=vibe-tdb.archive -DforceStdout)
readonly TDB_DOWNLOAD=$(mvn -q help:evaluate -Dexpression=vibe-tdb.download -DforceStdout)
readonly HPO_DOWNLOAD=$(mvn -q help:evaluate -Dexpression=hpo-owl.download -DforceStdout)

cd src/test/resources

echo "# Removing old data"
rm -rf tdb
rm -f hp.owl

echo "# Downloading data"
curl -L -O ${TDB_DOWNLOAD} -O ${HPO_DOWNLOAD}

echo "# Preparing data for unit-tests"
tar -xzvf ${TDB_ARCHIVE}
mv ${TDB_ARCHIVE%.tar.gz} tdb
rm ${TDB_ARCHIVE}

cd ../../../