package org.molgenis.vibe.io;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

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
        return ModelFactory.createOntologyModel();
    }
}
