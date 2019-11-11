#!/usr/bin/env bash

#########################################################################
#Name:     TestOptimizedQueries.sh                                      #
#Function: Script for validating/comparing SPARQL query files.          #
#                                                                       #
#Usage:    TestOptimizedQueries.sh                                      #
#########################################################################

# Defines error echo.
errcho() { echo "$@" 1>&2; }

# Describes usage.
readonly USAGE="Usage: TestOptimizedQueries.sh [-h] -or <DIR> -op <DIR>
Description: Runs preparations for unit testing.
Arguments:
-h	--help			Shows this help message.
-or	--original		Path to directory containing the complete TDB.
-op	--optimized		Path to directory containing the optimized TDB.

IMPORTANT:  Requires Apache Jena TDB Command-line Utilities to be configured.
            See https://jena.apache.org/documentation/tools/#setting-up-your-environment for more information.
"

# Base path (to script).
readonly BASE_PATH=$(sed 's/TestOptimizedQueries.sh$//' <<< $0 | sed -e 's/^$/.\//g')

main() {
	digestCommandLine $@
	runTests
}

digestCommandLine() {
	# Checks if any arguments were given.
	if [ $# -eq 0 ]; then errcho "No arguments were given.\n\n$USAGE"; exit 1; fi

	#Digests the command line arguments.
	while [[ $# -gt 0 ]]
	do
		key="$1"
		case $key in
			-or|--original)
			readonly TDB_original="$2"
			shift # argument
			shift # value
			;;
			-op|--optimized)
			readonly TDB_optimized="$2"
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

	# Checks if DIR variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ! ${TDB_original+isset} == isset ]]; then errcho "Missing required argument: -or/--original <DIR>\n\n$USAGE"; exit 1; fi

	# Checks if DIR variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ! ${TDB_optimized+isset} == isset ]]; then errcho "Missing required argument: -op/--optimized <DIR>\n\n$USAGE"; exit 1; fi

	# Checks if given argument is an existing directory.
	if [ ! -d "$TDB_original" ]; then errcho "Path to original TDB is not an existing directory.\n\n$USAGE"; exit 1; fi

	# Checks if given argument is an existing directory.
	if [ ! -d "$TDB_optimized" ]; then errcho "Path to optimized TDB is not an existing directory.\n\n$USAGE"; exit 1; fi
}

runTests() {
	# Sets path to parent dir of tests.
	cd "$BASE_PATH"
	cd ../

	# Runs queries.
	echo "### Running original TDB/query."
	tdbquery --time --results=TSV --loc="$TDB_original" --query="sparql_queries/tdb_comparison/original_tdb/genes_for_hpo.rq" 1>"test/genes_for_hpo-original.tsv"
	echo "### Running optimized TDB/query."
	tdbquery --time --results=TSV --loc="$TDB_optimized" --query="sparql_queries/tdb_comparison/optimized_tdb/genes_for_hpo.rq" 1>"test/genes_for_hpo-optimized.tsv"

	# Compare shasums.
	echo "### Validating if optimized TDB/query output files are equal to their original counterparts."
	local CHECKSUM_GENES_FOR_PHENOTYPES=$(shasum -a 256 test/genes_for_hpo-original.tsv | cut -d ' ' -f1)
	shasum -a 256 -c <<< "$CHECKSUM_GENES_FOR_PHENOTYPES  test/genes_for_hpo-optimized.tsv" # double space is needed! Second space indicates text file type.
}

main $@
