package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

class DirectedSinglyLinkedListGraphTest {

    @Test
    void test() {
        try {

            DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();

            for (char i = 'A'; i <= 'J'; i++) {
                graph.addVertex(i);
            }

            graph.addEdgeWeight('A', 'B', "Juan");
            graph.addEdgeWeight('A', 'C', "Pedro");
            graph.addEdgeWeight('A', 'D', "Maria");
            graph.addEdgeWeight('B', 'F', "Jose");
            graph.addEdgeWeight('E', 'F', "Ana");
            graph.addEdgeWeight('F', 'J', "Luis");
            graph.addEdgeWeight('C', 'G', "Carlos");
            graph.addEdgeWeight('G', 'J', "Elena");
            graph.addEdgeWeight('D', 'H', "Sofia");
            graph.addEdgeWeight('I', 'H', "Andres");
            graph.addEdgeWeight('H', 'J', "Laura");


            System.out.println("==== CONTENIDO ORIGINAL DEL GRAFO ====");
            System.out.println(graph);


            System.out.println("DFS Traversal: " + graph.dfs());
            System.out.println("BFS Traversal: " + graph.bfs());


            System.out.println("\n==== ELIMINANDO VÉRTICES E, J, I ====");
            graph.removeVertex('E');
            graph.removeVertex('J');
            graph.removeVertex('I');


            System.out.println("\n==== ELIMINANDO ARISTAS C-G, D-H, A-B ====");
            graph.removeEdge('C', 'G');
            graph.removeEdge('D', 'H');
            graph.removeEdge('A', 'B');


            System.out.println("\n==== RECORRIDOS TRAS ELIMINACIONES ====");
            System.out.println("DFS Traversal: " + graph.dfs());
            System.out.println("BFS Traversal: " + graph.bfs());


            System.out.println("\n==== CONTENIDO FINAL DEL GRAFO ====");
            System.out.println(graph);

        } catch (GraphException | ListException | StackException | QueueException e) {
            e.printStackTrace();
        }
    }
}