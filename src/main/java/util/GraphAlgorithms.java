package util;

import domain.*;

import java.util.*;

public class GraphAlgorithms {

//    public static List<Edge> kruskal(Graph graph) throws GraphException {
//        List<Edge> result = new ArrayList<>();
//        List<Edge> edges = graph.getAllEdges(); // Este método debe estar en el grafo
//        edges.sort(Comparator.comparingInt(Edge::getWeight));
//
//        DisjointSet<Object> ds = new DisjointSet<>();
//        for (Object vertex : graph.getAllVertices()) {
//            ds.makeSet(vertex);
//        }
//
//        for (Edge edge : edges) {
//            Object from = edge.getFrom();
//            Object to = edge.getTo();
//
//            if (!ds.findSet(from).equals(ds.findSet(to))) {
//                result.add(edge);
//                ds.union(from, to);
//            }
//        }
//
//        return result;
//    }
//
//    public static List<Edge> prim(Graph graph) throws GraphException {
//        List<Edge> result = new ArrayList<>();
//        Set<Object> visited = new HashSet<>();
//        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
//
//        Object startVertex = graph.getAllVertices().get(0);
//        visited.add(startVertex);
//        for (Object neighbor : graph.getNeighbors(startVertex)) {
//            int weight = graph.getWeight(startVertex, neighbor);
//            minHeap.add(new Edge(startVertex, neighbor, weight));
//        }
//
//        while (!minHeap.isEmpty() && visited.size() < graph.size()) {
//            Edge edge = minHeap.poll();
//            if (!visited.contains(edge.getTo())) {
//                visited.add(edge.getTo());
//                result.add(edge);
//
//                for (Object neighbor : graph.getNeighbors(edge.getTo())) {
//                    if (!visited.contains(neighbor)) {
//                        int weight = graph.getWeight(edge.getTo(), neighbor);
//                        minHeap.add(new Edge(edge.getTo(), neighbor, weight));
//                    }
//                }
//            }
//        }
//
//        return result;
//    }
}