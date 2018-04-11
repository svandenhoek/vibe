package org.molgenis.vibe.formats;

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
    private List<Set<Phenotype>> network = new ArrayList<>();

    public PhenotypeNetwork(Phenotype phenotype) {
        items.put(phenotype, 0);
        Set<Phenotype> sourceSet = new HashSet<>();
        sourceSet.add(requireNonNull(phenotype));
        network.add(sourceSet);
    }

    /**
     * @return the source phenotype (the only {@link Phenotype} with {@code distance} 0)
     */
    public Phenotype getSource() {
        return network.get(0).iterator().next();
    }

    public Set<Phenotype> getAll() {
        return items.keySet();
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

    /**
     * @return the highest {@code distance} currently stored
     */
    public int getMaxDistance() {
        return network.size() - 1;
    }

    public void add(Phenotype[] phenotypes, int distance) {
        for(Phenotype phenotype:phenotypes) {
            add(phenotype, distance);
        }
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
     * @throws IllegalArgumentException if {@code distance} 0 is given in combination with a a different {@link Phenotype}
     * than used during {@link PhenotypeNetwork} creation or if the {@code distance} is 2 or more higher than the currently
     * stored highest {@code distance} (as this would indicate a missing {@link Phenotype} to link the two {@code distances})
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

        // If phenotype is already stored within the network.
        if(items.keySet().contains(phenotype)) {
            int currentlyStoredDistance = items.get(phenotype);

            // If new distance is higher or equal to currently stored one, nothing happens.
            if(currentlyStoredDistance <= distance) {
                return false;
            } else { // Adjusts the phenotype with the new distance if this was closer.
                items.put(phenotype, distance);
                network.get(currentlyStoredDistance).remove(phenotype);
                network.get(distance).add(phenotype);
                return true;
            }

        } else { // If phenotype is not stored yet.
            if(distance == network.size()) { // Input 1 higher than current max is allowed.
                network.add(new HashSet<>());
            } else if(distance > network.size()) { // if gap is bigger, the phenotypes cannot be connected to each other as a connecting "in-between" phenotype is missing.
                throw new IllegalArgumentException("Input distance may only be 1 higher than currently stored highest distance.");
            }

            // Adds phenotype.
            items.put(phenotype, distance);
            network.get(distance).add(phenotype);
            return true;
        }
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
