package org.molgenis.vibe.core.formats;

public abstract class GeneDiseaseCollectionJsonSerialization {
    // Primary key for all combinations.
    protected static final String COMBINATIONS_KEY = "combinations";
    protected static final String COMBINATION_GENE_KEY = Gene.ID_PREFIX;
    protected static final String COMBINATION_DISEASE_KEY = Disease.ID_PREFIX;
    protected static final String COMBINATION_DISGENET_SCORE_KEY = "score";
    protected static final String COMBINATION_SOURCES_KEY = "sources";

    protected static final String COMBINATION_SOURCE_NAME_KEY = "name";
    protected static final String COMBINATION_SOURCE_COUNT_KEY = "count";

    protected static final String COMBINATION_SOURCE_PUBMEDS_KEY = "pubmed";
    protected static final String COMBINATION_SOURCE_PUBMED_URI_KEY = "uri";
    protected static final String COMBINATION_SOURCE_PUBMED_YEAR_KEY = "year";

    // Primary key for gene details.
    protected static final String GENES_KEY = COMBINATION_GENE_KEY;
    // GENE KEY = gene.getId();
    protected static final String GENE_SYMBOL_KEY = GeneSymbol.ID_PREFIX;

    // Primary key for disease details.
    protected static final String DISEASES_KEY = COMBINATION_DISEASE_KEY;
    // DISEASE KEY = disease.getId();
    protected static final String DISEASE_NAME_KEY = "name";

    // Primary key for source details.
    protected static final String SOURCES_KEY = "sources";
    // SOURCE KEY = source.getName()
    protected static final String SOURCE_FULL_NAME_KEY = "fullName";
    protected static final String SOURCE_URI_KEY = "uri";
    protected static final String SOURCE_LEVEL_KEY = "level";




}
