package org.molgenis.vibe.core.formats;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * A network of {@link Phenotype}{@code s} with a single source {@link Phenotype} from which the network is created.
 */
public class PhenotypeNetwork {
    /**
     * A collection of all stored phenotypes with their distance from the source.
     */
    private Map<Phenotype, Integer> items = new HashMap<>();

    /**
     * The phenotypes stored per distance level.
     */
    private Map<Integer, Set<Phenotype>> network = new HashMap<>();

    public PhenotypeNetwork(Phenotype phenotype) {
        items.put(phenotype, 0);
        Set<Phenotype> sourceSet = new HashSet<>();
        sourceSet.add(requireNonNull(phenotype));
        network.put(0, sourceSet);
    }

    /**
     * @return the source phenotype (the only {@link Phenotype} with {@code distance} 0)
     */
    public Phenotype getSource() {
        return network.get(0).iterator().next();
    }

    /**
     *
     * @return all stored {@link Phenotype}{@code s}
     */
    public Set<Phenotype> getPhenotypes() {
        return Collections.unmodifiableSet(items.keySet());
    }

    /**
     * @return all {@code distances} for which {@link Phenotype}{@code s} are stored.
     */
    public Set<Integer> getDistances() {
        return Collections.unmodifiableSet(network.keySet());
    }

    /**
     * Retrieves all {@link Phenotype}{@code s} that have the specified {@code distance}
     * @param distance value to be used for {@link Phenotype} retrieval
     * @return all {@link Phenotype}{@code s} with the given {@code distance}
     */
    public Set<Phenotype> getByDistance(int distance) {
        return Collections.unmodifiableSet(network.get(distance));
    }

    /**
     * Retrieve the stored {@code distance} belonging to a {@link Phenotype}
     * @param phenotype the {@link Phenotype} to retrieve the {@code distance} from
     * @return the {@code distance} belonging to the {@code phenotype}
     */
    public int getDistance(Phenotype phenotype) {
        return items.get(phenotype);
    }

    public void add(Collection<Phenotype> phenotypes, int distance) {
        for (Iterator<Phenotype> it = phenotypes.iterator(); it.hasNext(); ) {
            add(it.next(), distance);
        }
    }

    /**
     * Adds a {@link Phenotype} to the {@link PhenotypeNetwork}. If the {@link Phenotype} is already stored, compares the new
     * {@code distance} to the currently stored one. If the already stored distance is closer or equal, nothing is done.
     * Otherwise, the old {@code distance} is replaced with the new one. If the {@link Phenotype} is not stored yet, it is
     * simply added.
     * @param phenotype the {@link Phenotype} to be added to the {@code network}
     * @param distance the {@code distance} the {@link Phenotype} is from the {@code source}
     * @return {@code true} if added/distance is updated, otherwise {@code false}
     */
    public boolean add(Phenotype phenotype, int distance) {
        // Checks if the given distance is 0.
        if(distance == 0) {
            // If given phenotype is the source, nothing happens.
            if(phenotype.equals(getSource())) {
                return false;
            } else { // If it is another phenotype, an exception is thrown.
                throw new IllegalArgumentException("The given phenotype with distance 0 does not equal the source phenotype.");
            }
        }

        // Tries to retrieve the distance of the given phenotype.
        Integer retrievedPhenotypeDistance = items.get(phenotype);

        // If no distance was retrieved, phenotype was not yet stored and will be added.
        if(retrievedPhenotypeDistance == null) {
            items.put(phenotype, distance);
            getPhenotypesForDistance(distance).add(phenotype);
            return true;

        } else { // If phenotype is already stored within the network.
            // If new distance is higher or equal to currently stored one, nothing happens.
            if(retrievedPhenotypeDistance <= distance) {
                return false;
            } else { // Adjusts the phenotype with the new distance if the new distance is closer.
                items.put(phenotype, distance);
                getPhenotypesForDistance(retrievedPhenotypeDistance).remove(phenotype);
                getPhenotypesForDistance(distance).add(phenotype);
                return true;
            }
        }
    }

    /**
     * Tries to retrieve all phenotypes belonging to the defined {@code distance}. Note that this functions differently
     * than the {@code public} version ({@link #getByDistance(int)}) and should be used internally only. It mainly functions
     * as a safe method for further processing as when a distance is not yet stored. Instead of returning {@code null} in
     * such cases, a new {@link Set} is created and added to the {@code network} before returning a {@link Set} for further
     * usage.
     * @param distance the {@code distance} for which {@link Phenotype}{@code s} should be returned
     * @return {@link Phenotype}{@code s} currently stored for that distance or an empty {@link Set} if no {@link Phenotype}{@code s}
     * are stored for that {@code distance}
     */
    private Set<Phenotype> getPhenotypesForDistance(int distance) {
        // Tries to retrieve the HashSet for that distance.
        Set<Phenotype> distancePhenotypes = network.get(distance);

        // Adds a HashSet for that distance if not already available.
        if(distancePhenotypes == null) {
            distancePhenotypes = new HashSet<>();
            network.put(distance, distancePhenotypes);
        }

        return distancePhenotypes;
    }

    public boolean contains(Phenotype phenotype) {
        return items.keySet().contains(phenotype);
    }

    @Override
    public String toString() {
        return "PhenotypeNetwork{" +
                "network=" + network +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhenotypeNetwork that = (PhenotypeNetwork) o;
        return Objects.equals(network, that.network);
    }

    @Override
    public int hashCode() {
        return Objects.hash(network);
    }
}
