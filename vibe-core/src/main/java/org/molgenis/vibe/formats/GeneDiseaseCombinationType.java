package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Based on: http://semanticscience.org/ontology/sio.owl (which is licensed under http://creativecommons.org/licenses/by/4.0/)
 * Please refer to http://www.disgenet.org/ds/DisGeNET/html/images/ontology.svg for the Ontology overview.
 */
public enum GeneDiseaseCombinationType implements EnumTypeDefiner {
    GENE_DISEASE("SIO_000983"),
    THERAPEUTIC("SIO_001120"),
    BIOMARKER("SIO_001121"),
    GENOMIC_ALTERATION("SIO_001350"),
    ALTERED_EXPRESSION("SIO_001123"),
    POST_TRANSLATIONAL_MODIFICATION("SIO_001124"),
    CHROMOSOMAL_REARRANGEMENT("SIO_001349"),
    GENETIC_VARIATION("SIO_001122"),
    FUSION_GENE("SIO_001348"),
    SUSCEPTIBILITY_MUTATION("SIO_001343"),
    CASUAL_MUTATION("SIO_001119"),
    MODIFYING_MUTATION("SIO_001342"),
    SOMATIC_CASUAL_MUTATION("SIO_001345"),
    GERMLINE_CASUAL_MUTATION("SIO_001344"),
    SOMATIC_MODIFYING_MUTATION("SIO_001346"),
    GERMLINE_MODIFYING_MUTATION("SIO_001347");

    /**
     * The root {@link GeneDiseaseCombinationType} of all association types available in this enum
     */
    public static final GeneDiseaseCombinationType ROOT = GENE_DISEASE;

    /**
     * The prefix belonging to the association types.
     */
    private static final String PREFIX = "sio:";

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

    GeneDiseaseCombinationType(String sio) {
        this.id = sio;
    }

    public static GeneDiseaseCombinationType retrieve(String sio) {
        Matcher m = Pattern.compile("^((sio|SIO):)?(SIO_[0-9]{6})$").matcher(sio);
        if (m.matches()) {
            return EnumTypeDefiner.retrieve(m.group(3), GeneDiseaseCombinationType.class);
        } else {
            throw new InvalidStringFormatException(sio + " does not adhere the required format: ^((sio|SIO):)?(SIO_[0-9]{6})$");
        }
    }
}
