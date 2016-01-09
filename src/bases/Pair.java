/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package bases;

/**
 * A pair of two generic instances.
 */
public class Pair<T1, T2> {
    public final T1 first;
    public final T2 last;

    public Pair(T1 first, T2 last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ last.hashCode();
    }

    public boolean consitsOf(T1 t1, T2 t2) {
        return (first == t1 && last == t2) || (first == t2 && last == t1);
    }
}
