package util;

public class Edge {
    private final Object from;
    private final Object to;
    private final int weight;

    public Edge(Object from, Object to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
}

