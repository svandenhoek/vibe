package org.molgenis.vibe.formats;

import java.util.*;

/**
 * A collection of 0 or more {@link BiologicalEntityCombination}{@code s} that have the same {@link T1} and {@link T2} class types.
 * @param <T1> the first {@link BiologicalEntity} subclass type
 * @param <T2> the second {@link BiologicalEntity} subclass type
 * @param <T3> the {@link BiologicalEntityCombination} combining {@link T1} and {@link T2}
 */
public abstract class BiologicalEntityCollection<T1 extends BiologicalEntity, T2 extends BiologicalEntity, T3 extends BiologicalEntityCombination<T1,T2>> implements Collection<T3> {
    /**
     * All the {@link BiologicalEntityCombination}{@code s} (retrievable by themselves as key).
     */
    private Map<T3, T3> combinationsMap = new HashMap<>();

    /**
     * The {@link BiologicalEntityCombination}{@code s} grouped per {@link T1}.
     */
    private Map<T1, Set<T3>> combinationsByT1 = new HashMap<>();

    /**
     * The {@link BiologicalEntityCombination}{@code s} grouped per {@link T2}.
     */
    private Map<T2, Set<T3>> combinationsByT2 = new HashMap<>();

    public T3 get(T3 t3) {
        return combinationsMap.get(t3);
    }

    public Set<T1> getT1() {
        return Collections.unmodifiableSet(combinationsByT1.keySet());
    }

    public Set<T2> getT2() {
        return Collections.unmodifiableSet(combinationsByT2.keySet());
    }

    public Set<T3> getT3() {
        return Collections.unmodifiableSet(combinationsMap.keySet());
    }

    /**
     * Uses {@link #getT3()} to create an ordered {@link List} first ordered based on {@link T1} and then ordered based
     * on {@link T2}.
     * @return ordered {@link List} of {@link T3}
     */
    public List<T3> getT3Ordered() {
        List<T3> orderedList = new ArrayList<>();
        orderedList.addAll(combinationsMap.keySet());
        orderedList.sort(Comparator.comparing(T3::getT1).thenComparing(T3::getT2));

        return orderedList;
    }

    /**
     * Get all {@link T3} belonging to a single {@link T1}.
     * @param t1
     * @return all {@link T3} belonging to {@code t1}
     */
    public Set<T3> getByT1(T1 t1) {
        return (combinationsByT1.get(t1) == null) ? null : (Collections.unmodifiableSet(combinationsByT1.get(t1)));
    }

    /**
     * Get all {@link T3} belonging to a single {@link T2}.
     * @param t2
     * @return all {@link T3} belonging to {@code t2}
     */
    public Set<T3> getByT2(T2 t2) {
        return ((combinationsByT2.get(t2) == null) ? null : Collections.unmodifiableSet(combinationsByT2.get(t2)));
    }

    public BiologicalEntityCollection() {
    }

    public BiologicalEntityCollection(Set<T3> combinations) {
        addAll(combinations);
    }

    @Override
    public int size() {
        return combinationsMap.size();
    }

    @Override
    public boolean isEmpty() {
        return combinationsMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return combinationsMap.containsKey(o);
    }

    @Override
    public Iterator<T3> iterator() {
        return combinationsMap.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return combinationsMap.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return combinationsMap.keySet().toArray(a);
    }

    @Override
    public boolean add(T3 t3) {
        if(!combinationsMap.containsKey(t3)) {
            combinationsMap.put(t3,t3);
            addCombinationToT1Map(t3, combinationsByT1); // "? extends BiologicalEntity" causes issues
            addCombinationToT2Map(t3, combinationsByT2); // "? extends BiologicalEntity" causes issues
            return true;
        }
        return false;
    }

    private void addCombinationToT1Map(T3 t3, Map<T1, Set<T3>> combinationsMap) {
        Set<T3> valueSet = combinationsMap.get(t3.getT1());
        if(valueSet == null) {
            valueSet = new HashSet<>();
            combinationsMap.put(t3.getT1(), valueSet);
        }
        valueSet.add(t3);
    }

    private void addCombinationToT2Map(T3 t3, Map<T2, Set<T3>> combinationsMap) {
        Set<T3> valueSet = combinationsMap.get(t3.getT2());
        if(valueSet == null) {
            valueSet = new HashSet<>();
            combinationsMap.put(t3.getT2(), valueSet);
        }
        valueSet.add(t3);
    }

    @Override
    public boolean remove(Object o) {
        // Casts Object to T3.
        T3 t3 = (T3) o;

        // Removes item from general collection.
        Object removeObject = combinationsMap.remove(t3);

        // removeObject is null if combinationsMap.remove(o) did NOT remove something.
        boolean removedSomething = !Objects.isNull(removeObject);

        // If something was removed, makes sure it is also removed from Maps grouped by T1/T2.
        if(removedSomething) {
            removeCombinationFromT1Map(t3, combinationsByT1);
            removeCombinationFromT2Map(t3, combinationsByT2);
        }

        // Returns whether something was removed.
        return removedSomething;
    }

    private void removeCombinationFromT1Map(T3 t3, Map<T1, Set<T3>> combinationsMap) {
        Set<T3> valueSet = combinationsMap.get(t3.getT1());
        valueSet.remove(t3);
        if(valueSet.size() == 0) {
            combinationsMap.remove(t3.getT1());
        }
    }

    private void removeCombinationFromT2Map(T3 t3, Map<T2, Set<T3>> combinationsMap) {
        Set<T3> valueSet = combinationsMap.get(t3.getT2());
        valueSet.remove(t3);
        if(valueSet.size() == 0) {
            combinationsMap.remove(t3.getT2());
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return combinationsMap.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T3> c) {
        boolean changed = false;

        Iterator<? extends T3> cIterator = c.iterator();
        while(cIterator.hasNext()) {
            boolean itemAdded = add(cIterator.next());
            if(itemAdded) {changed = true; }
        }

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removedSomething = false;
        for(Object o:c) {
            if(remove(o)) {
                removedSomething = true;
            }
        }
        return removedSomething;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = combinationsMap.keySet().retainAll(c);
        combinationsByT1.values().forEach(s->s.retainAll(c));
        combinationsByT2.values().forEach(s->s.retainAll(c));

        removeEmptySets(combinationsByT1);
        removeEmptySets(combinationsByT2);

        return changed;
    }

    private void removeEmptySets(Map<? extends BiologicalEntity, Set<T3>> combinationsByKey) {
        for(BiologicalEntity key:combinationsByKey.keySet()) {
            if(combinationsByKey.get(key).isEmpty()) {
                combinationsByKey.remove(key);
            }
        }
    }

    @Override
    public void clear() {
        combinationsMap.clear();
        combinationsByT1.clear();
        combinationsByT2.clear();
    }

    @Override
    public String toString() {
        return "BiologicalEntityCollection{" +
                "combinationsMap.keySet()=" + combinationsMap.keySet() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiologicalEntityCollection<?, ?, ?> that = (BiologicalEntityCollection<?, ?, ?>) o;
        return Objects.equals(combinationsMap, that.combinationsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combinationsMap);
    }
}
