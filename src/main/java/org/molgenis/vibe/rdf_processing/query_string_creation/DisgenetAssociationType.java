package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.EnumTypeDefiner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Based on: http://semanticscience.org/ontology/sio.owl (which is licensed under http://creativecommons.org/licenses/by/4.0/)
 * Please refer to http://www.disgenet.org/ds/DisGeNET/html/images/ontology.svg for the Ontology overview.
 */
public enum DisgenetAssociationType implements EnumTypeDefiner {
    GENE_DISEASE("000983"),
    THERAPEUTIC("001120"),
    BIOMARKER("001121"),
    GENOMIC_ALTERATION("001350"),
    ALTERED_EXPRESSION("001123"),
    POST_TRANSLATIONAL_MODIFICATION("001124"),
    CHROMOSOMAL_REARRANGEMENT("001349"),
    GENETIC_VARIATION("001122"),
    FUSION_GENE("001348"),
    SUSCEPTIBILITY_MUTATION("001343"),
    CASUAL_MUTATION("001119"),
    MODIFYING_MUTATION("001342"),
    SOMATIC_CASUAL_MUTATION("001345"),
    GERMLINE_CASUAL_MUTATION("001344"),
    SOMATIC_MODIFYING_MUTATION("001346"),
    GERMLINE_MODIFYING_MUTATION("001347");

    /**
     * The root {@link DisgenetAssociationType} of all association types available in this enum
     */
    public static final DisgenetAssociationType ROOT = GENE_DISEASE;

    /**
     * The prefix belonging to the association types.
     */
    private static final String PREFIX = "sio:SIO_";

    /**
     * The ID belonging to single association type.
     */
    private String id;

    @Override
    public String getId() {
        return id;
    }

    public String getFormattedId() {
        return PREFIX + id;
    }

    DisgenetAssociationType(String sio) {
        this.id = sio;
    }

    public static DisgenetAssociationType retrieve(String sio) {
        Matcher m = Pattern.compile("^(((sio|SIO):)?(sio|SIO)_)?([0-9]{6})$").matcher(sio);
        if (m.matches()) {
            return EnumTypeDefiner.retrieve(m.group(5), DisgenetAssociationType.class);
        } else {
            throw new InvalidStringFormatException(sio + " does not adhere the required format: ^(((sio|SIO):)?(sio|SIO)_)?([0-9]{6})$");
        }
    }
}
