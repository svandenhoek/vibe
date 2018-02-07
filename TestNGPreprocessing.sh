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
	digestCommandLine $@
	runTestPreperations
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

	# Generates default variables.
	DISGENET_MINI="./src/test/resources/disgenet_mini"

    # Checks default variables.
    if [ ! -d "$DISGENET_MINI" ]; then errcho "The directory "$DISGENET_MINI" is missing.\n\n$USAGE"; exit 1; fi

	# Checks if DISGENET_FULL variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ${DISGENET_FULL+isset} == isset ]]
	then
        # Checks if given argument is an existing directory.
        if [ ! -d "$DISGENET_FULL" ]; then errcho "Path to the full DisGeNET TDB not an existing directory.\n\n$USAGE"; exit 1; fi
    fi
}

function runTestPreperations {
    # Generates TDB dataset from mini DisGeNET dataset.
    tdbloader2 --loc ./src/test/resources/disgenet_mini_tdb "$DISGENET_MINI"/*.ttl "$DISGENET_MINI"/*.owl

    # Generates symlink to full DisGeNET TDB.
    if [[ ${DISGENET_FULL+isset} == isset ]]
    then
        ln -s "$DISGENET_FULL" ./src/test/resources/disgenet_full_tdb
    fi
}

main $@