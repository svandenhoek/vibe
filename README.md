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

Using only the user-defined phenotypes with the output being sorted based on the highest gene-disease association score present per gene:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -s gda_max -o results.tsv -p HP:0002996 -p HP:0001377`

Output file:

```

```
+630 more lines

---

Using the user-defined phenotypes and phenotypes that are related to them with a maximum distance of 1 with the output
 sorted based on the Disease Specificity Index:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -w hp.owl -n distance -m 1 -s dsi -o results.tsv -p HP:0002996`

Output file:

```

```
+2711 more lines

---

Using the user-defined phenotypes and their (grand)children with a maximum distance of 2 with the output sorted based on
the Disease Pleiotropy Index:

`java -jar vibe-with-dependencies.jar -v -t TDB/ -w hp.owl -n children -m 2 -s dpi -o results.tsv -p HP:0002996`

Output file:

```

```
+974 more lines

## F.A.Q.

**Q:** It takes forever to retrieve information from the TDB.

**A:** Depending on the how much information needs to be retrieved from the TDB based on the input genes, this process may indeed take a while. However, there are ways to speed up this process. Examples include using an SSD instead HDD and [using a 64-bit JVM](https://jena.apache.org/documentation/tdb/architecture.html#caching-on-32-and-64-bit-java-systems).

[vibe_download]: https://github.com/molgenis/vibe/releases/latest
[java_download]:https://www.java.com/download
[tdb_download]: https://drive.google.com/open?id=1EGWuNFH_xLLBzykjXyqUuAnmO61TIaSN
[jena_download]:https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tools/#setting-up-your-environment
[hpo_owl]:http://purl.obolibrary.org/obo/hp.owl
