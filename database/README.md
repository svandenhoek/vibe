# Database creation

VIBE needs an optimized TDB to run. To create this, several steps are needed, as explained below. Note that the initial TDB should not be used in combination with VIBE, but only the optimized one! This is because certain information is left out in the optimized TDB to reduce the database size. The SPARQL-queries within VIBE therefore should not be used with the regular TDB as this could result in unusual results or possibly even errors.

## Requirements

- Apache Jena ([download][jena_download] and [configure][jena_configure])

## Data

- [DisGeNET RDF v6 dump][disgenet_rdf_v6_dump]
- [DisGeNET RDF v5 pda][disgenet_rdf_v5_pda]
- [DisGeNET RDF v5 phenotype][disgenet_rdf_v5_phenotype]
- [Semanticscience Integrated Ontology (SIO)][sio_owl]
- [HPO - ORDO Ontological Module (RDF/XML-format)][hoom]

## Creating initial TDB

1. Download the data.
2. Rename `owlapi.xrdf` to `owlapi.xml` (otherwise `tdbloader2` will give `org.apache.jena.riot.RiotException: Failed to determine the content type`)
3. Run `tdbloader2 --loc /path/to/initial/TDB /path/to/disgenet_v6/dump/*.ttl /path/to/disgenet_v5/pda.ttl /path/to/disgenet_v5/phenotype.ttl  /path/to/sio-release.owl /path/to/owlapi.xml`

## Creating optimized TDB

Work in progress.

1. Create a directory to store optimized `.ttl` files in.
2. Run `tdbquery --loc=/path/to/initial/TDB/ --query=/path/to/vibe/db_creation/sparql_queries/hpo.rq 1> /path/to/optimized/ttl/hpo.ttl`
3. Run `tdbquery --loc=/path/to/initial/TDB/ --query=/path/to/vibe/db_creation/sparql_queries/disease.rq 1> /path/to/optimized/ttl/disease.ttl`
4. Run `tdbquery --time --loc=/path/to/initial/TDB/ --query=/path/to/vibe/db_creation/sparql_queries/gene.rq 1> /path/to/optimized/ttl/gene.ttl`
5. Run `tdbquery --time --loc=/path/to/initial/TDB/ --query=/path/to/vibe/db_creation/sparql_queries/gda.rq 1> /path/to/optimized/ttl/gda.ttl`
6. Run `tdbloader2 --loc /path/to/store/optimized/TDB /path/to/optimized/ttl/*.ttl  /path/to/sio-release.owl`





[jena_download]: https://jena.apache.org/download/index.cgi
[jena_configure]: https://jena.apache.org/documentation/tdb/commands.html#scripts
[disgenet_rdf_v6_dump]: http://rdf.disgenet.org/download/v5.0.0/disgenetv5.0-rdf-v5.0.0-dump.tar.gz
[disgenet_rdf_v5_pda]: http://rdf.disgenet.org/download/v5.0.0/pda.ttl.tar.gz
[disgenet_rdf_v5_phenotype]: http://rdf.disgenet.org/download/v5.0.0/phenotype.ttl.tar.gz
[sio_owl]: http://semanticscience.org/ontology/sio.owl

[hoom]: http://data.bioontology.org/ontologies/HOOM/download?apikey=8b5b7825-538d-40e0-9e9e-5ab9274a9aeb&download_format=rdf