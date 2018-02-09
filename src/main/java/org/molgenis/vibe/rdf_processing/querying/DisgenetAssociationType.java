package org.molgenis.vibe.rdf_processing.querying;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Based on: http://semanticscience.org/ontology/sio.owl (which is licensed under http://creativecommons.org/licenses/by/4.0/)
 * Please refer to http://www.disgenet.org/ds/DisGeNET/html/images/ontology.svg for the Ontology overview.
 */
public enum DisgenetAssociationType {
    GENE_DISEASE("000897"),
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

    private static final String PREFIX = "sio:SIO_";
    private String id;

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
            return compareWithAvailableIds(m.group(5));
        } else {
            throw new InvalidStringFormatException(sio + " does not adhere the required format: ^(((sio|SIO):)?(sio|SIO)_)?([0-9]{6})$");
        }
    }

    private static DisgenetAssociationType compareWithAvailableIds(String sio) {
        DisgenetAssociationType[] allTypes = DisgenetAssociationType.values();
        for(int i = 0; i < allTypes.length; i++) {
            if(sio.toLowerCase().equals(allTypes[i].getId().toLowerCase())) {
                return allTypes[i];
            }
        }
        // If no match was found.
        return null;
    }
}
