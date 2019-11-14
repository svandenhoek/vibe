# VIBE

A tool to generate prioritized genes using phenotype information.

## Web app
* Available at: http://molgenis.org/vibe
* Source code: https://github.com/molgenis/molgenis-app-vibe

## Quickstart

* Download [vibe .jar file][vibe_download]
* Download and extract [TDB][tdb_download]
* Make sure you have [Java 8 or higher][java_download]
* Open a terminal and run VIBE. `java -jar vibe-1.0.jar -v -t TDB/ -o results.tsv -p HP:0002996 -p HP:0001377`

## Detailed instructions

### For developers

Instructions regarding building/testing the app can be found [here](./app/README.md) and how to create a TDB for the app can be found [here](./database/README.md).

### Requirements

* [Java 8 or higher][java_download]
* [A local TDB dataset][tdb_download]
* [Human Phenotype Ontology (HPO)][hpo_owl] *

\* = Only required when certain arguments are used.

### Usage

`java -jar vibe-with-dependencies.jar [-h] [-v] -t <FILE> [-w <FILE> -n <NAME> -m <NUMBER>] -o <FILE> [-s <NAME>] [-l] -p <HPO ID> [-p <HPO ID>]...`

### Examples

Using only the user-defined phenotypes:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -s gda_max -o results.tsv -p HP:0002996 -p HP:0001377`

---

Using the user-defined phenotypes and phenotypes that are related to them with a maximum distance of 1:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -w hp.owl -n distance -m 1 -o results.tsv -p HP:0002996`

---

Using the user-defined phenotypes and their (grand)children with a maximum distance of 2:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -w hp.owl -n children -m 2 -o results.tsv -p HP:0002996`

### Output format

There are currently 2 options for the output. By default the output will look something like:

```
gene (NCBI)	highest GDA score	diseases (UMLS) with sources per disease
29123	0.8	C0220687 (0.8):15378538,15523620,21782149,22307766,23184435,23369839,23463723,23494856,24088041,24838796,25125236,25187894,25413698,25424714,25464108,25741868,26633545,27667800,27900361,28250421,28708303,29224748,29565525|C1835764 (0.1)
56172	0.37	C0265292 (0.37):16462526,19257826,20186813,20358596,20943778,21149338,26820766
2697	0.31	C0265292 (0.31):23951358
```

Here, each row represents a gene. Each row consists out of several (tab-seperated) fields. First is the NCBI gene id, then the highest found gene-disease association score found in DisGeNET for that gene, and finally all found associated diseases with the gene-disease association score specific for that gene-disease combination followed by the evidence IDs (pubmed if no URL is shown) for it.

Alternatively, the option `-l` can be added when running the tool. If this is done, the output will only contain the genes (separated by comma's). The example output above would look like:

```
29123,56172,2697
```



## F.A.Q.

**Q:** It takes forever to retrieve information from the TDB.

**A:** Depending on the how much information needs to be retrieved from the TDB based on the input genes, this process may indeed take a while. However, there are ways to speed up this process. Examples include using an SSD instead HDD and [using a 64-bit JVM](https://jena.apache.org/documentation/tdb/architecture.html#caching-on-32-and-64-bit-java-systems).

[vibe_download]: https://github.com/molgenis/vibe/releases/latest
[java_download]:https://www.java.com/download
[tdb_download]: https://drive.google.com/open?id=1EGWuNFH_xLLBzykjXyqUuAnmO61TIaSN
[jena_download]:https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tools/#setting-up-your-environment
[hpo_owl]:http://purl.obolibrary.org/obo/hp.owl
