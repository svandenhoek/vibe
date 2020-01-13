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
readonly USAGE="Usage: GenerateDatabase.sh [-h] [-1] [-2] [-3] [-4]
Description: Generates database for vibe.
The process is split into multiple phases which can be chosen individually.
If no phase is given, runs phase 1-4 one after another.

Arguments:
-h	--help		Shows this help message.
-1				Download sources.
-2				Create initial TDB.
-3				Create optimized TTL files.
-4				Create optimized TDB.
-5				Create optimized TDB archive.

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
readonly FINAL_TDB_ARCHIVE=${FINAL_TDB_DIR}.tar.gz

main() {
	digestCommandLine $@
	
	if [[ ${doDownload} == true ]]
	then
		prepareData
	fi

    if [[ ${doOriginalTdb} == true ]]
    then
    	createInitialTdb
	fi

    if [[ ${doOptimizedTtl} == true ]]
    then
    	createOptimizedTtlFiles
	fi

    if [[ ${doOptimizedTdb} == true ]]
    then
    	createOptimizedTdb
    	copyLicensesToTdbDir
	fi

	if [[ ${doTdbArchive} == true ]]
    then
    	createArchive
	fi
}

digestCommandLine() {
	# Sets defaults for phases.
	doDownload=false
	doOriginalTdb=false
	doOptimizedTtl=false
	doOptimizedTdb=false
	doTdbArchive=false

	#Digests the command line arguments.
	while [[ $# -gt 0 ]]
	do
		key="$1"
		case $key in
			-1)
			doDownload=true
			shift # argument
			;;
			-2)
			doOriginalTdb=true
			shift # argument
			;;
			-3)
			doOptimizedTtl=true
			shift # argument
			;;
			-4)
			doOptimizedTdb=true
			shift # argument
			;;
			-5)
			doTdbArchive=true
			shift # argument
			;;
			-h|--help)
			local help=true
			shift # argument
			;;
			*)    # unknown option
			shift # argument
			;;
		esac
	done

	# Checks if usage is requested.
	if [[ ${help} == true ]]; then echo "$USAGE"; exit 0; fi

	# If no phase is set, defaults all to true.
	if [[ ${doDownload} == false ]] && [[ ${doOriginalTdb} == false ]] && [[ ${doOptimizedTtl} == false ]] && [[ ${doOptimizedTdb} == false ]] && [[ ${doTdbArchive} == false ]]
	then
		doDownload=true
		doOriginalTdb=true
		doOptimizedTtl=true
		doOptimizedTdb=true
		# doTdbArchive stays false
	fi

	# Make phase variables readonly.
	readonly doDownload=${doDownload}
	readonly doOriginalTdb=${doOriginalTdb}
	readonly doOptimizedTtl=${doOptimizedTtl}
	readonly doOptimizedTdb=${doOptimizedTdb}
	readonly doTdbArchive=${doTdbArchive}

	# Prints for each phase whether it will be run.
	echo "######## ######## ######## Selected phases ######## ######## ########"
	echo "download:${doDownload}\ninitial TDB:${doOriginalTdb}\noptimized TTL:${doOptimizedTtl}\noptimized TDB:${doOptimizedTdb}\narchive:${doTdbArchive}"

	# Check whether directories might already exist.
	validateDirectories
}

validateDirectories() {
	echo "######## ######## ######## Checking existence phase-specific directories ######## ######## ########"
	# Done separately (instead of integrated into main) so that first all necessary directories are
	# checked whether they already exist before anything is actually done.
	
	# Boolean for when a directory which should be created already exists. 
	local directoryExists=false 

	# Checks directories for the different phases.
	if [[ ${doDownload} == true ]]
    then
		if [ -d "$SOURCES_DIR" ]; then directoryExists=true; errcho "${SOURCES_DIR} already exists."; fi
    fi

    if [[ ${doOriginalTdb} == true ]]
    then
		if [ -d "$INITIAL_TDB_DIR" ]; then directoryExists=true; errcho "${INITIAL_TDB_DIR} already exists."; fi
    fi

    if [[ ${doOptimizedTtl} == true ]]
    then
		if [ -d "$TTL_DIR" ]; then directoryExists=true; errcho "${TTL_DIR} already exists."; fi
    fi

    if [[ ${doOptimizedTdb} == true ]]
    then
		if [ -d "$FINAL_TDB_DIR" ]; then directoryExists=true; errcho "${FINAL_TDB_DIR} already exists."; fi
    fi

	if [[ ${doTdbArchive} == true ]]
    then
		if [ -d "$FINAL_TDB_ARCHIVE" ]; then directoryExists=true; errcho "${FINAL_TDB_ARCHIVE} already exists."; fi
    fi

	# If a directory already exists, exits script.
	if [[ ${directoryExists} == true ]]; then errcho "Exiting."; exit 1; fi
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
	echo "######## ######## ######## Downloading files ######## ######## ########"
	curl -O 'http://rdf.disgenet.org/download/v6.0.0/disgenetv6.0-rdf-v6.0.0-dump.tgz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/pda.ttl.tar.gz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/phenotype.ttl.tar.gz'
	curl -O 'http://rdf.disgenet.org/download/v5.0.0/void.ttl.tar.gz'
	curl -O 'https://raw.githubusercontent.com/MaastrichtU-IDS/semanticscience/e2cae5ef1737a182c6ca8b451f35d689171c2c83/ontology/sio/release/sio-release.owl'
	curl -o owlapi.xml 'http://data.bioontology.org/ontologies/HOOM/download?apikey=8b5b7825-538d-40e0-9e9e-5ab9274a9aeb&download_format=rdf'
}

validateDownloads() {
	echo "######## ######## ######## Validating downloaded files ######## ######## ######## "
	shasum -a 256 -c ${BASE_PATH}sources_checksums.txt
	if (($? != 0))
	then
		errcho "Checksum for downloaded sources failed. Exiting."
		exit 1
	fi
}

unpackDownloadedArchives() {
	echo "######## ######## ######## Unpacking archives ######## ######## ########"
	mkdir disgenet_v6
	mkdir disgenet_v5
	tar -xvzf disgenetv6.0-rdf-v6.0.0-dump.tgz -C disgenet_v6
	tar -xvzf pda.ttl.tar.gz -C disgenet_v5
	tar -xvzf phenotype.ttl.tar.gz -C disgenet_v5
	tar -xvzf void.ttl.tar.gz -C disgenet_v5
}

createInitialTdb() {
	echo "######## ######## ######## Creating initial TDB ######## ######## ########"
	tdbloader2 --loc ${INITIAL_TDB_DIR} ${SOURCES_DIR}/disgenet_v6/*.ttl ${SOURCES_DIR}/disgenet_v5/*.ttl ${SOURCES_DIR}/sio-release.owl ${SOURCES_DIR}/owlapi.xml
}

createOptimizedTtlFiles() {
	echo "######## ######## ######## Creating optimized TTL files ######## ######## ########"
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
	echo "######## ######## ######## Creating optimized TDB ######## ######## ########"
	tdbloader2 --loc ${FINAL_TDB_DIR} ${TTL_DIR}/*.ttl ${SOURCES_DIR}/sio-release.owl
}

copyLicensesToTdbDir() {
	echo "######## ######## ######## Adding licenses file to optimized TDB ######## ######## ########"
	cp ${BASE_PATH}/LICENSES.md ${FINAL_TDB_DIR}
}

createArchive() {
	echo "######## ######## ######## Creating archive from optimized TDB ######## ######## ########"
	tar -czvf ${FINAL_TDB_ARCHIVE} ${FINAL_TDB_DIR}
}

main $@