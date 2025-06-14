package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;
import util.Utility;

class DirectedAdjacencyMatrixGraphTest {

    @Test
    void test() {
        try {
            DirectedAdjacencyMatrixGraph graph = new DirectedAdjacencyMatrixGraph(10);

            for (int i = 1; i <= 5; i++) {
                graph.addVertex(i);
            }

            graph.addEdgeWeight(1, 1, getRandomWeight());
            graph.addEdgeWeight(1, 2, getRandomWeight());
            graph.addEdgeWeight(1, 3, getRandomWeight());
            graph.addEdgeWeight(1, 4, getRandomWeight());
            graph.addEdgeWeight(1, 5, getRandomWeight());

            graph.addEdgeWeight(2, 2, getRandomWeight());
            graph.addEdgeWeight(2, 1, getRandomWeight());
            graph.addEdgeWeight(2, 4, getRandomWeight());

            graph.addEdgeWeight(3, 3, getRandomWeight());
            graph.addEdgeWeight(3, 4, getRandomWeight());

            graph.addEdgeWeight(4, 4, getRandomWeight());
            graph.addEdgeWeight(4, 2, getRandomWeight());
            graph.addEdgeWeight(4, 3, getRandomWeight());
            graph.addEdgeWeight(4, 5, getRandomWeight());

            graph.addEdgeWeight(5, 5, getRandomWeight());
            graph.addEdgeWeight(5, 1, getRandomWeight());
            graph.addEdgeWeight(5, 4, getRandomWeight());

            System.out.println("Grafo inicial:");
            System.out.println(graph);

            System.out.println("\nDFS traversal:");
            System.out.println(graph.dfs());
            System.out.println("\nBFS traversal:");
            System.out.println(graph.bfs());

            System.out.println("\nEliminando vértices 1, 3 y 4...");
            graph.removeVertex(1);
            graph.removeVertex(3);
            graph.removeVertex(4);

            for (int i = 0; i < graph.size(); i++) {
                Object v = graph.getVertexByIndex(i).data;
                graph.removeEdge(2, v);
                graph.removeEdge(v, 2);
                graph.removeEdge(5, v);
                graph.removeEdge(v, 5);
            }

            System.out.println("\nGrafo modificado tras eliminaciones:");
            System.out.println(graph);

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }
    private int getRandomWeight() {
        return 49 + util.Utility.random(250); // Devuelve entre 50 y 300
    }
}