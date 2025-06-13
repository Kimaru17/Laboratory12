package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
            graph.addEdgeWeight('G', 'J',  "Elena");
            graph.addEdgeWeight('D', 'H', "Sofia");
            graph.addEdgeWeight('I', 'H', "Andres");
            graph.addEdgeWeight('H', 'J', "Laura");


            System.out.println(graph);  //toString
            System.out.println("DFS Transversal Tour: "+graph.dfs());
            System.out.println("BFS Transversal Tour: "+graph.bfs());

            //eliminemos vertices
            System.out.println("\nVertex deleted: F");
            graph.removeVertex('F');
            System.out.println("\nVertex deleted: H");
            graph.removeVertex('H');
            System.out.println("\nVertex deleted: J");
            graph.removeVertex('J');
            System.out.println("DFS Transversal Tour: "+graph.dfs());
            System.out.println("BFS Transversal Tour: "+graph.bfs());
            System.out.println(graph);  //toString

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }
}