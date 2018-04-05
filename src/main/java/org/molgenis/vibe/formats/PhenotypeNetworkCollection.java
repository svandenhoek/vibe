package org.molgenis.vibe.formats;

import java.util.*;

/**
 * A collection of {@link PhenotypeNetwork}{@code s}.
 */
public class PhenotypeNetworkCollection {
    /**
     * The individual {@link Phenotype}{@code s} collected among all {@link PhenotypeNetwork}{@code s}.
     */
    private Set<Phenotype> phenotypes = new HashSet<>();

    /**
     * A collection of {@link PhenotypeNetwork}{@code s} stored by their source ({@link PhenotypeNetwork#getSource()}).
     */
    private Map<Phenotype, PhenotypeNetwork> phenotypeNetworks = new HashMap<>();

    public Set<Phenotype> getPhenotypes() {
        return phenotypes;
    }

    /**
     * Retrieve a {@link PhenotypeNetwork} based on its {@link PhenotypeNetwork#getSource()}.
     * @param source the {@code source} belonging to a {@link PhenotypeNetwork}
     * @return the {@link PhenotypeNetwork} belonging to the {@code source}, or {@code null} if there is no
     * {@link PhenotypeNetwork} available with that {@code source}
     */
    public PhenotypeNetwork getPhenotypeNetworkBySource(Phenotype source) {
        return phenotypeNetworks.get(source);
    }

    /**
     * Adds a {@link PhenotypeNetwork} to the {@link PhenotypeNetworkCollection}
     * @param network the {@link PhenotypeNetwork} to be added
     */
    public void add(PhenotypeNetwork network) {
        phenotypeNetworks.put(network.getSource(), network);
        phenotypes.addAll(network.getAll());

    }

    /**
     * Removes a {@link PhenotypeNetwork} from the {@link PhenotypeNetworkCollection}.
     * @param network the {@link PhenotypeNetwork} to be removed
     * @return {@code true} if the {@code network} was removed, otherwise {@code false}
     */
    public boolean remove(PhenotypeNetwork network) {
        boolean removed = phenotypeNetworks.values().remove(network);
        generatePhenotypes();
        return removed;
    }

    /**
     * Clears the {@link PhenotypeNetworkCollection}
     */
    public void clear() {
        phenotypes.clear();
        phenotypeNetworks.clear();
    }

    /**
     * Generates the {@code phenotypes} from all stored {@link PhenotypeNetwork}{@code s}.
     */
    private void generatePhenotypes() {
        for(PhenotypeNetwork network : phenotypeNetworks.values()) {
            phenotypes.addAll(network.getAll());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhenotypeNetworkCollection that = (PhenotypeNetworkCollection) o;
        return Objects.equals(phenotypeNetworks, that.phenotypeNetworks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phenotypeNetworks);
    }
}
