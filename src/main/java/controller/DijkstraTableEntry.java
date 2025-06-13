package controller;

public class DijkstraTableEntry {
    private int position;
    private Object vertex;
    private int distance;

    public DijkstraTableEntry(int position, Object vertex, int distance) {
        this.position = position;
        this.vertex = vertex;
        this.distance = distance;
    }

    public int getPosition() {
        return position;
    }

    public Object getVertex() {
        return vertex;
    }

    public int getDistance() {
        return distance;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setVertex(Object vertex) {
        this.vertex = vertex;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
