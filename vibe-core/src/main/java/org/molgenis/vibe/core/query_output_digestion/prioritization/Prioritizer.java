package org.molgenis.vibe.core.query_output_digestion.prioritization;

import org.molgenis.vibe.core.formats.BiologicalEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Generates a priority order of a {@link BiologicalEntity} subclass.
 * @param <T>
 */
public abstract class Prioritizer<T extends BiologicalEntity> {
    /**
     * The data from which a priority order should be created from.
     */
    private List<T> data;

    public List<T> getPriority() {
        return data;
    }

    protected void setPriority(List<T> data) {
        this.data = data;
    }

    public Prioritizer(List<T> data) {
        this.data = requireNonNull(data);
    }

    public Prioritizer(Set<T> data) {
        this.data = new ArrayList<>(requireNonNull(data));
    }

    public abstract void run();
}
