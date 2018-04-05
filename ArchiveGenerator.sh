#!/usr/bin/env bash

#########################################################################
#Name:     ArchiveGenerator.sh                                          #
#Function: Create a new up-to-date external files archive.              #
#          This archive currently only includes the test resources      #
#          needed for TestNGPreprocessing.sh (after being uploaded).    #
#                                                                       #
#Usage:    ArchiveGenerator.sh                                          #
#########################################################################

# Base path (to script).
readonly BASE_PATH=$(sed 's/ArchiveGenerator.sh$//' <<< $0 | sed -e 's/^$/.\//g')

function main {
    declare -r start_pwd=$PWD

    cd "$BASE_PATH"
	tar --exclude '.*' --exclude 'disgenet_mini_tdb' --exclude 'disgenet_mini_tdb_no_ontology' -czvf "test_resources_$(date "+%Y-%m-%d").tar.gz" "src/test/resources/"*
	cd "$start_pwd"
}

main