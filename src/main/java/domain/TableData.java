package domain;

public class TableData {
    private int position;
    private Object vertex;
    private String distance;

    public TableData(int position, Object vertex, String distance) {
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

    public String getDistance() {
        return distance;
    }
}
