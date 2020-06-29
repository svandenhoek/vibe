package org.molgenis.vibe.core.query_output_digestion.prioritization;

import org.molgenis.vibe.core.formats.BiologicalEntity;
import org.molgenis.vibe.core.formats.BiologicalEntityCollection;

import java.util.List;

/**
 * Returns a priority of {@code T1} from {@code T2}. {@code T1} should be one of the {@link BiologicalEntity}{@code s}
 * stored inside {@link T2}.
 * @param <T1> the returned priority
 * @param <T2> stores the data of which a priority should be created from
 */
public interface Prioritizer<T1 extends BiologicalEntity, T2 extends BiologicalEntityCollection> {
    /**
     * Sorts the given list.
     * @param collection
     */
    List<T1> sort(T2 collection);
}
