package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectedAdjacencyListGraphTest {

    @Test
    void fullGraphTest() {
        try {
            DirectedAdjacencyListGraph graph = new DirectedAdjacencyListGraph(20);

            // Paso i: Crear los vértices A-M
            String[] vertices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
            for (String v : vertices) {
                graph.addVertex(v);
            }

            // Paso j: Agregar aristas según imagen
            addEdgeWithRandomWeight(graph, "A", "B");
            addEdgeWithRandomWeight(graph, "A", "C");
            addEdgeWithRandomWeight(graph, "A", "D");

            addEdgeWithRandomWeight(graph, "B", "E");
            addEdgeWithRandomWeight(graph, "E", "H");
            addEdgeWithRandomWeight(graph, "H", "K");

            addEdgeWithRandomWeight(graph, "C", "F");
            addEdgeWithRandomWeight(graph, "F", "I");
            addEdgeWithRandomWeight(graph, "I", "L");

            addEdgeWithRandomWeight(graph, "D", "G");
            addEdgeWithRandomWeight(graph, "G", "J");
            addEdgeWithRandomWeight(graph, "J", "M");

            // Paso l: Mostrar información del grafo
            System.out.println("Grafo original:\n" + graph);

            // Paso m: Mostrar recorridos DFS y BFS
            System.out.println("DFS Traversal: " + graph.dfs());
            System.out.println("BFS Traversal: " + graph.bfs());

            // Paso n: Eliminar vértices E, F, G
            graph.removeVertex("E");
            graph.removeVertex("F");
            graph.removeVertex("G");

            // Paso o: Eliminar aristas H-K, I-L, J-M
            graph.removeEdge("H", "K");
            graph.removeEdge("I", "L"); // I fue eliminado ya, así que esta puede lanzar excepción si no se controla
            graph.removeEdge("J", "M");

            // Paso p: Mostrar grafo actualizado
            System.out.println("Grafo después de eliminaciones:\n" + graph);

        } catch (GraphException | ListException | StackException | QueueException e) {
            e.printStackTrace();
            fail("Excepción durante la ejecución: " + e.getMessage());
        }
    }
    private void addEdgeWithRandomWeight(DirectedAdjacencyListGraph graph, String from, String to) throws GraphException, ListException {
        int weight = util.Utility.random(41) + 10; // Entre 10 y 50
        graph.addEdgeWeight(from, to, weight);
    }

}