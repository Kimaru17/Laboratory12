package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

class DirectedAdjacencyMatrixGraphTest {

    @Test
    void test() {
        try {
            DirectedAdjacencyMatrixGraph graph = new DirectedAdjacencyMatrixGraph(50);
            char vertices[] = {'P', 'T', 'K', 'D', 'S', 'M', 'H', 'A', 'E', 'Q', 'G', 'R', 'B', 'J'};
            String colors[] = {"red", "blue", "green", "yellow", "orange", "purple", "pink", "brown", "cyan", "magenta", "gray", "black", "white", "violet", "indigo", "teal", "lime", "coral", "navy", "olive", "maroon"};

            for (int i = 0; i < vertices.length; i++) {
                graph.addVertex(vertices[i]);
            }
            graph.addEdgeWeight('P', 'T', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('P', 'K', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('P', 'D', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('T', 'S', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('S', 'A', colors[util.Utility.random(colors.length)]);

            graph.addEdgeWeight('K', 'M', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('D', 'H', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('A', 'G', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('M', 'E', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('H', 'Q', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('Q', 'R', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('G', 'B', colors[util.Utility.random(colors.length)]);
            graph.addEdgeWeight('R', 'J', colors[util.Utility.random(colors.length)]);
            System.out.println(graph);
            System.out.println("DFS Transversal Tour: "+graph.dfs());
            System.out.println("BFS Transversal Tour: "+graph.bfs());





            //eliminemos vertices
            System.out.println("\nVertex deleted: T");
            graph.removeVertex('T');
            System.out.println("\nVertex deleted: K");
            graph.removeVertex('K');
            System.out.println("\nVertex deleted: H");
            graph.removeVertex('H');
            System.out.println(graph);  //toString


        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }
}