package util;

import domain.AdjacencyListGraph;
import domain.AdjacencyMatrixGraph;
import domain.GraphException;
import domain.SinglyLinkedListGraph;
import domain.list.ListException;

import java.util.*;

public class GraphAlgoriyhms {

    private static class Edge {
        Object from;
        Object to;
        int weight;

        public Edge(Object from, Object to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private static class DisjointSet {
        private final Map<Object, Object> parent = new HashMap<>();

        public void makeSet(List<Object> vertices) {
            for (Object v : vertices)
                parent.put(v, v);
        }

        public Object find(Object v) {
            if (!parent.get(v).equals(v))
                parent.put(v, find(parent.get(v)));
            return parent.get(v);
        }

        public void union(Object u, Object v) {
            Object rootU = find(u);
            Object rootV = find(v);
            if (!rootU.equals(rootV))
                parent.put(rootU, rootV);
        }
    }

    // ----------------------- PRIM -----------------------
    public static AdjacencyMatrixGraph prim(AdjacencyMatrixGraph graph) throws GraphException, ListException {
        int size = graph.size();
        boolean[] visited = new boolean[size];
        int[] minEdge = new int[size];
        Object[] parent = new Object[size];

        Arrays.fill(minEdge, Integer.MAX_VALUE);
        minEdge[0] = 0;

        for (int count = 0; count < size - 1; count++) {
            int u = getMinVertex(minEdge, visited);
            visited[u] = true;

            for (int v = 0; v < size; v++) {
                Object from = graph.getVertexByIndex(u).data;
                Object to = graph.getVertexByIndex(v).data;
                Object weightObj = graph.getWeightEdges(from, to);
                int weight = (weightObj instanceof Integer) ? (int) weightObj : 0;

                if (!visited[v] && weight != 0 && weight < minEdge[v]) {
                    parent[v] = from;
                    minEdge[v] = weight;
                }
            }
        }

        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(size);
        for (int i = 0; i < size; i++) {
            mst.addVertex(graph.getVertexByIndex(i).data);
        }

        for (int i = 1; i < size; i++) {
            Object to = graph.getVertexByIndex(i).data;
            Object from = parent[i];
            Object weight = graph.getWeightEdges(from, to);
            mst.addEdgeWeight(from, to, weight);
        }

        return mst;
    }

    private static int getMinVertex(int[] weights, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < weights.length; i++) {
            if (!visited[i] && weights[i] < min) {
                min = weights[i];
                index = i;
            }
        }

        return index;
    }

    // PRIM para AdjacencyListGraph
    public static AdjacencyMatrixGraph prim(AdjacencyListGraph graph) throws GraphException, ListException {
        List<Object> vertices = graph.getVertices();
        int size = vertices.size();
        boolean[] visited = new boolean[size];
        int[] minEdge = new int[size];
        Object[] parent = new Object[size];

        Arrays.fill(minEdge, Integer.MAX_VALUE);
        minEdge[0] = 0;

        for (int count = 0; count < size - 1; count++) {
            int u = getMinVertex(minEdge, visited);
            visited[u] = true;

            Object from = vertices.get(u);
            List<Object> adjacents = graph.getAdjacents(from);

            for (int v = 0; v < size; v++) {
                Object to = vertices.get(v);
                if (adjacents.contains(to)) {
                    Object weightObj = graph.getWeightEdges(from, to);
                    int weight = (weightObj instanceof Integer) ? (int) weightObj : 0;

                    if (!visited[v] && weight != 0 && weight < minEdge[v]) {
                        parent[v] = from;
                        minEdge[v] = weight;
                    }
                }
            }
        }

        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(size);
        for (Object v : vertices) {
            mst.addVertex(v);
        }

        for (int i = 1; i < size; i++) {
            Object to = vertices.get(i);
            Object from = parent[i];
            Object weight = graph.getWeightEdges(from, to);
            mst.addEdgeWeight(from, to, weight);
        }

        return mst;
    }

    // PRIM para SinglyLinkedListGraph (similar al anterior)
    public static AdjacencyMatrixGraph prim(SinglyLinkedListGraph graph) throws GraphException, ListException {
        List<Object> vertices = graph.getVertices();
        int size = vertices.size();
        boolean[] visited = new boolean[size];
        int[] minEdge = new int[size];
        Object[] parent = new Object[size];

        Arrays.fill(minEdge, Integer.MAX_VALUE);
        minEdge[0] = 0;

        for (int count = 0; count < size - 1; count++) {
            int u = getMinVertex(minEdge, visited);
            visited[u] = true;

            Object from = vertices.get(u);
            List<Object> adjacents = graph.getAdjacents(from);

            for (int v = 0; v < size; v++) {
                Object to = vertices.get(v);
                if (adjacents.contains(to)) {
                    Object weightObj = graph.getWeightEdges(from, to);
                    int weight = (weightObj instanceof Integer) ? (int) weightObj : 0;

                    if (!visited[v] && weight != 0 && weight < minEdge[v]) {
                        parent[v] = from;
                        minEdge[v] = weight;
                    }
                }
            }
        }

        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(size);
        for (Object v : vertices) {
            mst.addVertex(v);
        }

        for (int i = 1; i < size; i++) {
            Object to = vertices.get(i);
            Object from = parent[i];
            Object weight = graph.getWeightEdges(from, to);
            mst.addEdgeWeight(from, to, weight);
        }

        return mst;
    }

    // ----------------------- KRUSKAL -----------------------

    public static AdjacencyMatrixGraph kruskal(AdjacencyMatrixGraph graph) throws GraphException, ListException {
        int size = graph.size();
        List<Edge> edges = new ArrayList<>();
        List<Object> vertices = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Object from = graph.getVertexByIndex(i).data;
            vertices.add(from);
            for (int j = i + 1; j < size; j++) {
                Object to = graph.getVertexByIndex(j).data;
                Object weightObj = graph.getWeightEdges(from, to);
                if (weightObj instanceof Integer && (int) weightObj != 0) {
                    edges.add(new Edge(from, to, (int) weightObj));
                }
            }
        }

        edges.sort(Comparator.comparingInt(e -> e.weight));
        DisjointSet ds = new DisjointSet();
        ds.makeSet(vertices);

        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(size);
        for (Object v : vertices) {
            mst.addVertex(v);
        }

        for (Edge edge : edges) {
            Object rootU = ds.find(edge.from);
            Object rootV = ds.find(edge.to);
            if (!rootU.equals(rootV)) {
                mst.addEdgeWeight(edge.from, edge.to, edge.weight);
                ds.union(rootU, rootV);
            }
        }

        return mst;
    }
}
