#!/usr/bin/env bash

#########################################################################
#Name:     TestNGPreprocessing.sh                                       #
#Function: Automation script for unit test preperations.                #
#                                                                       #
#Usage:    TestNGPreprocessing.sh                                       #
#########################################################################

# Defines error echo.
errcho() { echo "$@" 1>&2; }

# Describes usage.
readonly USAGE="Usage: disgenet-data-formatter.sh [-h] [-d <DIR>]
Description: Runs preparations for unit testing.
Arguments:
-h --help           Shows this help message.
-d --disgenet	    Path to directory containing the full disgenet TDB.
                    If given, a symbolic link will be generated (required for some tests).

IMPORTANT:  Requires Apache Jena TDB Command-line Utilities to be configured.
            See https://jena.apache.org/documentation/tdb/commands.html for more information.
"

# Side of text for echo when displaying which phase is executed.
readonly SEP_SIDE='######## ######## ########'

# Download location of resource files.
readonly TEST_RESOURCES_DOWNLOAD_NAME="test_resources_2018-07-13.tar.gz"
readonly TEST_RESOURCES_DOWNLOAD="https://molgenis26.target.rug.nl/downloads/vibe/${TEST_RESOURCES_DOWNLOAD_NAME}"

# Base path (to script).
readonly BASE_PATH=$(sed 's/TestNGPreprocessing.sh$//' <<< $0 | sed -e 's/^$/.\//g')

# Location of directory for storing temporary files.
readonly TMP_DIR="${BASE_PATH}TMP/"

main() {
	digestCommandLine $@
	runTestPreparations
}

digestCommandLine() {
	#Digests the command line arguments.
	while [[ $# -gt 0 ]]
	do
		key="$1"
		case $key in
			-d|--disgenet)
			readonly DISGENET_FULL="$2"
			shift # argument
			shift # value
			;;
			-h|--help)
			local help=TRUE
			shift # argument
			;;
			*)    # unknown option
			shift # argument
			;;
		esac
	done

	# Checks if usage is requested.
	if [[ ${help} == TRUE ]]; then echo "$USAGE"; exit 0; fi

	# Checks if DISGENET_FULL variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ${DISGENET_FULL+isset} == isset ]]
	then
        # Checks if given argument is an existing directory.
        if [ ! -d "$DISGENET_FULL" ]; then errcho "Path to the full DisGeNET TDB not an existing directory.\n\n$USAGE"; exit 1; fi
    fi
}

runTestPreparations() {
    # Define variables.
    declare -r test_resources="${BASE_PATH}src/test/resources/"
    declare -r disgenet_mini="${test_resources}disgenet_mini"
    declare -r disgenet_mini_tdb_no_ontology="${test_resources}disgenet_mini_tdb_no_ontology"
    declare -r disgenet_mini_tdb="${test_resources}disgenet_mini_tdb"
    declare -r disgenet_full_symlink_path="${test_resources}disgenet_full_tdb"
    declare -r test_resources_tmp_download="${TMP_DIR}${TEST_RESOURCES_DOWNLOAD_NAME}"

    # Removes all available test resources (uncludes symlink!).
    rm -fr "$test_resources"/*

    # Creates tmp dir for temporary storage of data.
    mkdir "$TMP_DIR" # local test: disable and create manually

    # Downloads test data.
    echo "\n\n$SEP_SIDE Downloading data $SEP_SIDE\n\n" # local test: disable
    cd "$TMP_DIR"
    curl -O "$TEST_RESOURCES_DOWNLOAD" # local test: disable
    cd ../

    # Extracts archive (overrides if exists).
    echo "\n\n$SEP_SIDE Extracting data $SEP_SIDE\n\n"
    tar -zxvf "$test_resources_tmp_download" -C "$BASE_PATH"

    # Generates TDB dataset from mini DisGeNET dataset without ontology data.
    echo "\n\n$SEP_SIDE Generating TDB without DisGeNET Association Type Ontology $SEP_SIDE\n\n"
    tdbloader2 --loc "$disgenet_mini_tdb_no_ontology" "$disgenet_mini"/*.ttl

    # Generates TDB dataset from mini DisGeNET dataset.
    echo "\n\n$SEP_SIDE Generating TDB with DisGeNET Association Type Ontology $SEP_SIDE\n\n"
    tdbloader2 --loc "$disgenet_mini_tdb" "$disgenet_mini"/*

    # Generates symlink to full DisGeNET TDB.
    if [[ ${DISGENET_FULL+isset} == isset ]]
    then
        ln -s "$DISGENET_FULL" "$disgenet_full_symlink_path"
    fi

    # Removes tmp dir including content.
    rm -r "$TMP_DIR" # local test: disable
}

main $@
