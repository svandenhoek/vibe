package org.molgenis.vibe.formats;

import java.util.*;

public abstract class BiologicalEntityCollection<T1 extends BiologicalEntity, T2 extends BiologicalEntity, T3 extends BiologicalEntityCombination<T1,T2>> implements Collection<T3> {
    private Map<T3, T3> combinationsMap = new HashMap<>();

    private Map<T1, Set<T3>> combinationsByT1 = new HashMap<>();

    private Map<T2, Set<T3>> combinationsByT2 = new HashMap<>();

    public T3 get(T3 t3) {
        return  combinationsMap.get(t3);
    }

    public Set<T3> getAll() {
        return combinationsMap.keySet();
    }

    public Set<T3> getByT1(T1 t1) {
        return combinationsByT1.get(t1);
    }

    public Set<T3> getByT2(T2 t2) {
        return combinationsByT2.get(t2);
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
            addCombinationToSearchKeyMaps(t3, (Map<BiologicalEntity, Set<T3>>) combinationsByT1, t3.getT1());
            addCombinationToSearchKeyMaps(t3, (Map<BiologicalEntity, Set<T3>>) combinationsByT2, t3.getT2());
            return true;
        }
        return false;
    }

    private void addCombinationToSearchKeyMaps(T3 t3, Map<BiologicalEntity, Set<T3>> combinationsMap, BiologicalEntity bioEntity) {
        Set<T3> valueSet = combinationsMap.get(bioEntity);
        if(valueSet == null) {
            valueSet = new HashSet<>();
            combinationsMap.put(bioEntity, valueSet);
        }
        valueSet.add(t3);
    }

    @Override
    public boolean remove(Object o) {
        Object object = combinationsMap.remove(o);
        combinationsByT1.values().remove(o);
        combinationsByT2.values().remove(o);
        return !Objects.isNull(object);
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
        Object object = combinationsMap.keySet().removeAll(c);
        combinationsByT1.values().removeAll(c);
        combinationsByT2.values().removeAll(c);
        return !Objects.isNull(object);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Object object = combinationsMap.keySet().retainAll(c);
        combinationsByT1.values().retainAll(c);
        combinationsByT2.values().retainAll(c);
        return !Objects.isNull(object);
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
                "combinationsMap=" + combinationsMap +
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
