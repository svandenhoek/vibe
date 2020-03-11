package org.molgenis.vibe.formats;

import java.util.*;

/**
 * A collection of {@link PhenotypeNetwork}{@code s}.
 */
public class PhenotypeNetworkCollection {
    /**
     * A collection of {@link PhenotypeNetwork}{@code s} stored by their source ({@link PhenotypeNetwork#getSource()}).
     */
    private Map<Phenotype, PhenotypeNetwork> phenotypeNetworks = new HashMap<>();

    public Set<Phenotype> getPhenotypes() {
        Set<Phenotype> phenotypes =  new HashSet<>();
        for(PhenotypeNetwork network : phenotypeNetworks.values()) {
            phenotypes.addAll(network.getPhenotypes());
        }
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
    }

    /**
     * Removes a {@link PhenotypeNetwork} from the {@link PhenotypeNetworkCollection}.
     * @param network the {@link PhenotypeNetwork} to be removed
     * @return {@code true} if it was removed, otherwise {@code false}
     */
    public boolean remove(PhenotypeNetwork network) {
        return phenotypeNetworks.values().remove(network);
    }

    /**
     * Removes a {@link PhenotypeNetwork} from the {@link PhenotypeNetworkCollection}.
     * @param phenotypeSource the {@link PhenotypeNetwork#getSource()} from the {@link PhenotypeNetwork} to be removed
     * @return the {@link PhenotypeNetwork} if it was removed, otherwise {@code null}
     */
    public PhenotypeNetwork remove(Phenotype phenotypeSource) {
        return phenotypeNetworks.remove(phenotypeSource);
    }

    /**
     * Clears the {@link PhenotypeNetworkCollection}
     */
    public void clear() {
        phenotypeNetworks.clear();
    }

    @Override
    public String toString() {
        return "PhenotypeNetworkCollection{" +
                "phenotypeNetworks=" + phenotypeNetworks +
                '}';
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
