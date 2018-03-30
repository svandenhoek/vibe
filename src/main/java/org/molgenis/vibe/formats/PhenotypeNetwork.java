package org.molgenis.vibe.formats;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * A network of {@link Phenotype}{@code s} with a single source {@link Phenotype} from which the network is created.
 */
public class PhenotypeNetwork {
    /**
     * A collection of all stored phenotypes.
     */
    private Set<Phenotype> items = new HashSet<>();

    /**
     * The phenotypes stored per distance level.
     */
    private List<Set<Phenotype>> network = new ArrayList<>();

    public PhenotypeNetwork(Phenotype phenotype) {
        Set<Phenotype> sourceSet = new HashSet<>();
        sourceSet.add(requireNonNull(phenotype));
        network.add(sourceSet);
    }

    public Phenotype getSource() {
        return network.get(0).iterator().next();
    }

    public Set<Phenotype> getByDistance(int distance) {
        return Collections.unmodifiableSet(network.get(distance));
    }

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
     * @throws IllegalArgumentException if {@code distance} 0 is given in combination with a a different {@link Phenotype}
     * than used during {@link PhenotypeNetwork} creation or if the {@code distance} is 2 or more higher than the currently
     * stored highest {@code distance} (as this would indicate a missing {@link Phenotype} to link the two {@code distances})
     */
    public void add(Phenotype phenotype, int distance) {
        // Checks if the given distance is 0.
        if(distance == 0) {
            // If given phenotype is the source, nothing happens.
            if(phenotype.equals(getSource())) {
                return;
            } else { // If it is another phenotype, an exception is thrown.
                throw new IllegalArgumentException("The given phenotype with distance 0 does not equal the source phenotype.");
            }
        }

        // If phenotype is already stored within the network.
        if(items.contains(phenotype)) {
            // Checks if stored phenotype is simply a duplicate, and if so, leaves data as it is.
            if(network.get(distance).contains(phenotype)) {
                return;
            }

            // If no duplicate, compares distances and chooses lower distance to be stored.
            for(int i = 0; i < network.size(); i++) {
                Set<Phenotype> distancePhenotypes = network.get(i);
                if(distancePhenotypes.contains(phenotype)) {
                    if(i > distance) {
                        // Moves phenotype to different distance.
                        distancePhenotypes.remove(phenotype);
                        network.get(distance).add(phenotype);
                        return;
                    } else { // Currently stored phenotype distance is closer or equal to new one.
                        return;
                    }
                }
            }

            // If items indicate phenotype should be stored but it cannot be found in network, something has gone horribly wrong.
            throw new RuntimeException("An error has occurred. Please contact the developer.");

        } else { // If phenotype is not stored yet.
            if(distance == network.size()) { // Input 1 higher than current max is allowed.
                network.add(new HashSet<>());
            } else if(distance > network.size()) { // if gap is bigger, the phenotypes cannot be connected to each other as a connecting "in-between" phenotype is missing.
                throw new IllegalArgumentException("Input distance may only be 1 higher than currently stored highest distance.");
            }

            // Adds phenotype.
            items.add(phenotype);
            network.get(distance).add(phenotype);
        }
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
