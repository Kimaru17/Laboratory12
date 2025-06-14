package domain;

public class Edge implements Comparable<Edge> {
    public Object from, to;
    public int weight;
    public Edge(Object from, Object to, int weight) {
        this.from = from; this.to = to; this.weight = weight;
    }
    @Override
    public int compareTo(Edge e) { return Integer.compare(this.weight, e.weight); }
}
