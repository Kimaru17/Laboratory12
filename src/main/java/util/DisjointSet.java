package util;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T> {
    private final Map<T, T> parent = new HashMap<>();

    public void makeSet(T item) {
        parent.put(item, item);
    }

    public T findSet(T item) {
        if (parent.get(item) != item) {
            parent.put(item, findSet(parent.get(item)));
        }
        return parent.get(item);
    }

    public void union(T item1, T item2) {
        T root1 = findSet(item1);
        T root2 = findSet(item2);
        if (!root1.equals(root2)) {
            parent.put(root2, root1);
        }
    }
}
