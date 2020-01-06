#!/usr/bin/env bash

#########################################################################
#Name:     GenerateDatabase.sh                                          #
#Function: Generates a TDB for usage with vibe.                         #
#                                                                       #
#Usage:    GenerateDatabase.sh                                          #
#########################################################################

# Defines error echo.
errcho() { echo "$@" 1>&2; }

# Describes usage.
readonly USAGE="Usage: GenerateDatabase.sh [-h] -1|-2|-3|-4
Description: Generates database for vibe.
The process is split into multiple phases which can be chosen as starting point.

Arguments:
-h	--help		Shows this help message.
-1				Starts from downloading sources.
-2				Starts from initial TDB creation.
-3				Starts from optimized TTL file creation.
-4				Starts from optimized TDB creation.

IMPORTANT:  Requires Apache Jena TDB Command-line Utilities to be configured.
            See https://jena.apache.org/documentation/tools/#setting-up-your-environment for more information.
"

# Base paths (to current dir/script).
readonly CURRENT_PATH=$(pwd)
readonly BASE_PATH=$(sed 's/GenerateDatabase.sh$//' <<< $0 | sed -e 's/^$/.\//g')

# Retrieves vibe version from pom.xml file.
cd ${BASE_PATH}
readonly VIBE_VERSION=$(grep -A 1 '<artifactId>vibe</artifactId>' ../app/pom.xml | grep '<version>' | sed 's/[^0-9.]//g')
cd ${CURRENT_PATH}

# Defines directory names to be used.
readonly SOURCES_DIR=vibe-${VIBE_VERSION}-sources
readonly INITIAL_TDB_DIR=vibe-${VIBE_VERSION}-sources-tdb
readonly TTL_DIR=vibe-${VIBE_VERSION}-ttl
readonly FINAL_TDB_DIR=vibe-${VIBE_VERSION}-tdb

main() {
	digestCommandLine $@
	clearDirectories

	if (( ${startPhase} == 1))
    then
        prepareData
    fi

    if (( ${startPhase} <= 2))
    then
        createInitialTdb
    fi

    if (( ${startPhase} <= 3))
    then
        createOptimizedTtlFiles
    fi

    if (( ${startPhase} <= 4))
    then
        createOptimizedTdb
    fi

    copyLicensesToDir
}

clearDirectories() {
	# Done separately so that first all necessary directories are removed before anything is actually done.
	if (( ${startPhase} == 1))
    then
    	rm -r ${SOURCES_DIR}
    fi

    if (( ${startPhase} <= 2))
    then
    	rm -r ${INITIAL_TDB_DIR}
    fi

    if (( ${startPhase} <= 3))
    then
    	rm -r ${TTL_DIR}
    fi

    if (( ${startPhase} <= 4))
    then
    	rm -r ${FINAL_TDB_DIR}
    fi
}

digestCommandLine() {
	# Checks if any arguments were given.
	if [ $# -eq 0 ]; then errcho "No arguments were given.\n\n$USAGE"; exit 1; fi

	#Digests the command line arguments.
	while [[ $# -gt 0 ]]
	do
		key="$1"
		case $key in
			-1)
			# Gives error message if startPhase is already set.
			if [[ ${startPhase+isset} == isset ]]; then errcho "Only a single argument (-1, -2, -3 or -4) is allowed.\n\n$USAGE"; exit 1; fi
			readonly startPhase=1
			shift # argument
			;;
			-2)
			# Gives error message if startPhase is already set.
			if [[ ${startPhase+isset} == isset ]]; then errcho "Only a single argument (-1, -2, -3 or -4) is allowed.\n\n$USAGE"; exit 1; fi
			readonly startPhase=2
			shift # argument
			;;
			-3)
			# Gives error message if startPhase is already set.
			if [[ ${startPhase+isset} == isset ]]; then errcho "Only a single argument (-1, -2, -3 or -4) is allowed.\n\n$USAGE"; exit 1; fi
			readonly startPhase=3
			shift # argument
			;;
			-4)
			# Gives error message if startPhase is already set.
			if [[ ${startPhase+isset} == isset ]]; then errcho "Only a single argument (-1, -2, -3 or -4) is allowed.\n\n$USAGE"; exit 1; fi
			readonly startPhase=4
			shift # argument
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

	# Checks if variable is set. -> http://wiki.bash-hackers.org/syntax/pe#use_an_alternate_value
	if [[ ! ${startPhase+isset} == isset ]]; then errcho "Missing required argument: -1, -2, -3 or -4."; exit 1; fi
}

prepareData() {
	mkdir ${SOURCES_DIR}
	cd ${SOURCES_DIR}
	DownloadData
	validateDownloads
	unpackDownloadedArchives
	cd ${CURRENT_PATH}
}

DownloadData() {
	echo "### Downloading files."
	curl -O 'http://rdf.disgenet.org/download/v6.0.0/disgenetv6.0-rdf-v6.0.0-dump.tgz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/pda.ttl.tar.gz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/phenotype.ttl.tar.gz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/void.ttl.tar.gz'
	curl -O 'https://raw.githubusercontent.com/MaastrichtU-IDS/semanticscience/e2cae5ef1737a182c6ca8b451f35d689171c2c83/ontology/sio/release/sio-release.owl'
	curl -o owlapi.xml 'http://data.bioontology.org/ontologies/HOOM/download?apikey=8b5b7825-538d-40e0-9e9e-5ab9274a9aeb&download_format=rdf'
}

validateDownloads() {
	echo "### Validating downloaded files."
	shasum -a 256 -c ${BASE_PATH}sources_checksums.txt
	if (($? != 0))
	then
		errcho "Checksum failed. Exiting."
		exit 1
	fi
}

unpackDownloadedArchives() {
	echo "### Unpacking archives."
	mkdir disgenet_v6
	mkdir disgenet_v5
	tar -xvzf disgenetv6.0-rdf-v6.0.0-dump.tgz -C disgenet_v6
	tar -xvzf pda.ttl.tar.gz -C disgenet_v5
	tar -xvzf phenotype.ttl.tar.gz -C disgenet_v5
	tar -xvzf void.ttl.tar.gz -C disgenet_v5
}

createInitialTdb() {
	echo "### Creating initial TDB."
	tdbloader2 --loc ${INITIAL_TDB_DIR} ${SOURCES_DIR}/disgenet_v6/*.ttl ${SOURCES_DIR}/disgenet_v5/*.ttl ${SOURCES_DIR}/sio-release.owl ${SOURCES_DIR}/owlapi.xml
}

createOptimizedTtlFiles() {
	echo "### Creating optimized TTL files."
	mkdir ${TTL_DIR}

	echo "Generating: hpo.ttl"
	tdbquery --loc=${INITIAL_TDB_DIR} --query=${BASE_PATH}/sparql_queries/optimized_construct/hpo.rq 1> ${TTL_DIR}/hpo.ttl
	echo "Generating: disease.ttl"
	tdbquery --loc=${INITIAL_TDB_DIR} --query=${BASE_PATH}/sparql_queries/optimized_construct/disease.rq 1> ${TTL_DIR}/disease.ttl
	echo "Generating: gene.ttl"
	tdbquery --loc=${INITIAL_TDB_DIR} --query=${BASE_PATH}/sparql_queries/optimized_construct/gene.rq 1> ${TTL_DIR}/gene.ttl
	echo "Generating: gda.ttl"
	tdbquery --loc=${INITIAL_TDB_DIR} --query=${BASE_PATH}/sparql_queries/optimized_construct/gda.rq 1> ${TTL_DIR}/gda.ttl
	echo "Generating: source.ttl"
	tdbquery --loc=${INITIAL_TDB_DIR} --query=${BASE_PATH}/sparql_queries/optimized_construct/source.rq 1> ${TTL_DIR}/source.ttl
}

createOptimizedTdb() {
	echo "### Creating optimized TDB."
	tdbloader2 --loc ${FINAL_TDB_DIR} ${TTL_DIR}*.ttl ${SOURCES_DIR}sio-release.owl
}

copyLicensesToTdbDir() {
	echo "### Adding licenses file to optimized TDB."
	cp ${BASE_PATH}LICENSES.md ${FINAL_TDB_DIR}
}

main $@