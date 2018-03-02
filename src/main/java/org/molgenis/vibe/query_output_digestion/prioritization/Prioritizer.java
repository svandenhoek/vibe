package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.BiologicalEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Prioritizer<T extends BiologicalEntity> {
    private List<T> data;

    public List<T> getPriority() {
        return data;
    }

    protected void setPriority(List<T> data) {
        this.data = data;
    }

    public Prioritizer(List<T> data) {
        this.data = data;
    }

    public Prioritizer(Set<T> data) {
        this.data = new LinkedList<>(data);
    }

    public abstract void run();
}
