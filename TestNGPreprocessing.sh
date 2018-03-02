#!/usr/bin/env bash

#########################################################################
#Name:     TestNGPreprocessing.sh                                       #
#Function: Automation script for unit test preperations.                #
#                                                                       #
#Usage:    TestNGPreprocessing.sh                                       #
#########################################################################

# Defines error echo.
function errcho { echo "$@" 1>&2; }

# Describes usage.
USAGE="Usage: disgenet-data-formatter.sh [-h] [-d <DIR>]
Description: Runs preparations for unit testing.
Arguments:
-h --help           Shows this help message.
-d --disgenet	    Path to directory containing the full disgenet TDB.
                    If given, a symbolic link will be generated (required for some tests).

IMPORTANT:  Requires Apache Jena TDB Command-line Utilities to be configured.
            See https://jena.apache.org/documentation/tdb/commands.html for more information.
"

function main {
    generateDefaultVariables
	digestCommandLine $@
	runTestPreparations
}

function digestCommandLine {
	#Digests the command line arguments.
	while [[ $# -gt 0 ]]
	do
		key="$1"
		case $key in
			-d|--disgenet)
			DISGENET_FULL="$2"
			shift # argument
			shift # value
			;;
			-h|--help)
			HELP=TRUE
			shift # argument
			;;
			*)    # unknown option
			shift # argument
			;;
		esac
	done

	# Checks if usage is requested.
	if [[ $HELP == TRUE ]]; then echo "$USAGE"; exit 0; fi

	# Checks if DISGENET_FULL variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ${DISGENET_FULL+isset} == isset ]]
	then
        # Checks if given argument is an existing directory.
        if [ ! -d "$DISGENET_FULL" ]; then errcho "Path to the full DisGeNET TDB not an existing directory.\n\n$USAGE"; exit 1; fi
    fi
}

function generateDefaultVariables {
    # Side of text for echo when displaying which phase is executed.
    SEP_SIDE='######## ######## ########'

    # Download location of resource files.
    TEST_RESOURCES_DOWNLOAD=https://molgenis26.target.rug.nl/downloads/vibe/test_resources_2018-03-02.tar.gz

	# Base path (to script).
	BASE_PATH=$(sed 's/TestNGPreprocessing.sh$//' <<< $0)

    # Location of directory storing temporary files.
	TMP_DIR="$BASE_PATH"TMP/

	# Disgenet mini path (after extraction from archive)
	DISGENET_MINI="$BASE_PATH"src/test/resources/disgenet_mini
}

function runTestPreparations {
    # Creates tmp dir for temporary storage of data.
    mkdir "$TMP_DIR"

    # Downloads test data.
    echo "\n\n$SEP_SIDE Downloading data $SEP_SIDE\n\n"
    wget "$TEST_RESOURCES_DOWNLOAD" -P "$TMP_DIR"

    # Removes current mini TDBs if present.
    rm -fr "$BASE_PATH"src/test/resources/disgenet_mini_tdb_no_ontology
    rm -fr "$BASE_PATH"src/test/resources/disgenet_mini_tdb

    # Extracts archive (overrides if exists).
    echo "\n\n$SEP_SIDE Extracting data $SEP_SIDE\n\n"
    tar -zxvf "$TMP_DIR"test_resources_2018-03-02.tar.gz -C "$BASE_PATH"src/test

    # Generates TDB dataset from mini DisGeNET dataset without ontology data.
    echo "\n\n$SEP_SIDE Generating TDB without ontology information $SEP_SIDE\n\n"
    tdbloader2 --loc "$BASE_PATH"src/test/resources/disgenet_mini_tdb_no_ontology "$DISGENET_MINI"/*.ttl

    # Generates TDB dataset from mini DisGeNET dataset.
    echo "\n\n$SEP_SIDE Generating TDB with ontology information $SEP_SIDE\n\n"
    tdbloader2 --loc "$BASE_PATH"src/test/resources/disgenet_mini_tdb "$DISGENET_MINI"/*.ttl "$DISGENET_MINI"/*.owl

    # Generates symlink to full DisGeNET TDB.
    if [[ ${DISGENET_FULL+isset} == isset ]]
    then
        ln -s "$DISGENET_FULL" "$BASE_PATH"src/test/resources/disgenet_full_tdb
    fi

    # Removes tmp dir including content.
    rm -r "$TMP_DIR"
}

main $@