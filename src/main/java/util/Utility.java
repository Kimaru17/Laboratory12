package util;

import domain.EdgeWeight;
import domain.Graph;
import domain.GraphException;
import domain.Vertex;
import domain.list.ListException;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;
import java.util.Random;

public class Utility {

    //static init
    static {
    }

    public static String format(double value){
        return new DecimalFormat("###,###,###.##").format(value);
    }
    public static String $format(double value){
        return new DecimalFormat("$###,###,###.##").format(value);
    }

    public static void fill(int[] a, int bound) {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Random().nextInt(bound);
        }
    }

    public static int random(int bound) {
        return new Random().nextInt(bound)+1;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)){
            case "Integer":
                Integer int1 = (Integer)a; Integer int2 = (Integer)b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; //0 == equal
            case "String":
                String st1 = (String)a; String st2 = (String)b;
                return st1.compareTo(st2)<0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character)a; Character c2 = (Character)b;
                return c1.compareTo(c2)<0 ? -1 : c1.compareTo(c2)>0 ? 1 : 0;
            case "EdgeWeight":
                EdgeWeight ew1 = (EdgeWeight) a; EdgeWeight ew2 = (EdgeWeight) b;
                return compare(ew1.getEdge(), ew2.getEdge());
            case "Vertex":
                Vertex v1 = (Vertex) a; Vertex v2 = (Vertex) b;
                return compare(v1.data, v2.data);
        }
        return 2; //Unknown
    }

    private static String instanceOf(Object a, Object b) {
        if(a instanceof Integer && b instanceof Integer) return "Integer";
        if(a instanceof String && b instanceof String) return "String";
        if(a instanceof Character && b instanceof Character) return "Character";
        if(a instanceof EdgeWeight && b instanceof EdgeWeight) return "EdgeWeight";
        if(a instanceof Vertex && b instanceof Vertex) return "Vertex";
        return "Unknown";
    }

    public static int maxArray(int[] a) {
        int max = a[0]; //first element
        for (int i = 1; i < a.length; i++) {
            if(a[i]>max){
                max=a[i];
            }
        }
        return max;
    }

    public static int[] getIntegerArray(int n) {
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = random(9999);
        }
        return newArray;
    }


    public static int[] copyArray(int[] a) {
        int n = a.length;
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = a[i];
        }
        return newArray;
    }

    public static String show(int[] a, int n) {
        String result="";
        for (int i = 0; i < n; i++) {
            result+=a[i]+" ";
        }
        return result;
    }
    public static int getDifferentRandom(Graph graph, int bound) throws GraphException, ListException {
        int value = new Random().nextInt(bound)+1;

        if (graph.isEmpty()) return value;
        else if (graph.containsVertex(value)) return getDifferentRandom(graph, bound);

        return value;
    }
    public static void drawArrow(Pane pane, double startX, double startY, double endX, double endY) {
        // Tamaño de la flecha
        double arrowLength = 18;
        double arrowWidth = 9;

        // Ángulo de la línea
        double angle = Math.atan2(endY - startY, endX - startX);

        // Punto base de la flecha (antes del círculo final)
        double arrowEndX = endX - 20 * Math.cos(angle); // 20 es el radio del nodo
        double arrowEndY = endY - 20 * Math.sin(angle);

        // Los dos puntos para los lados de la flecha
        double x1 = arrowEndX - arrowLength * Math.cos(angle - Math.PI / 8);
        double y1 = arrowEndY - arrowLength * Math.sin(angle - Math.PI / 8);

        double x2 = arrowEndX - arrowLength * Math.cos(angle + Math.PI / 8);
        double y2 = arrowEndY - arrowLength * Math.sin(angle + Math.PI / 8);

        // Línea principal (puedes omitirla si ya la tienes)
        // Line line = new Line(startX, startY, arrowEndX, arrowEndY);
        // pane.getChildren().add(line);

        // Dos líneas de la flecha
        javafx.scene.shape.Line arrowLine1 = new javafx.scene.shape.Line(arrowEndX, arrowEndY, x1, y1);
        javafx.scene.shape.Line arrowLine2 = new javafx.scene.shape.Line(arrowEndX, arrowEndY, x2, y2);
        arrowLine1.setStrokeWidth(2.5);
        arrowLine2.setStrokeWidth(2.5);

        pane.getChildren().addAll(arrowLine1, arrowLine2);
    }

}
