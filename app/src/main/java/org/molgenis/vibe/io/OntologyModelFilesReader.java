package org.molgenis.vibe.io;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Generates an {@link OntModel} using {@link OntModelSpec#OWL_MEM}. This disables any reasoner resulting in manual
 * traversal of the subclasses.
 *
 * @see <a href="https://jena.apache.org/documentation/inference/index.html">https://jena.apache.org/documentation/inference/index.html</a>
 * @see <a href="https://jena.apache.org/documentation/ontology/#creating-ontology-models">https://jena.apache.org/documentation/ontology/#creating-ontology-models</a>
 * @see <a href=https://jena.apache.org/documentation/inference/#direct-and-indirect-relationships">https://jena.apache.org/documentation/inference/#direct-and-indirect-relationships</a>
 */
public class OntologyModelFilesReader extends ModelFilesReader {
    public OntologyModelFilesReader(String file) {
        super(file);
    }

    @Override
    public OntModel getModel() {
        return (OntModel) super.getModel();
    }

    @Override
    protected OntModel generateModel() {
        return ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    }
}
