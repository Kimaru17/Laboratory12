package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectedAdjacencyListGraphTest {

    @Test
    void test() {
        try {
            DirectedAdjacencyListGraph graph = new DirectedAdjacencyListGraph(50);
            for (int i = 0; i < 30; i++) {
                graph.addVertex(util.Utility.random(40));  // Adding random vertices from 1 to 40
            }

            System.out.println(graph);  //toString



            graph.connectEvenAndOddVertices();
            System.out.println("\n\n\nGraph after connecting even and odd vertices:");
            System.out.println(graph);  //toString
            connectRandomEvenOddVertices(graph);
            System.out.println("\n\n\nGraph after connecting random even and odd vertices:");
            System.out.println(graph);  //toString
            System.out.println("DFS Transversal Tour: "+graph.dfs());
            System.out.println("BFS Transversal Tour: "+graph.bfs());
            int deleted = 5; // Number of vertices to delete
            for (int i = 0; i <deleted ;) {
                int g= util.Utility.random(40);
                if(graph.containsVertex(g)) {
                    System.out.println("\nVertex deleted: " + g);
                    graph.removeVertex(g);
                    i++;
                }
            }
            System.out.println(graph);  //toString

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectRandomEvenOddVertices(DirectedAdjacencyListGraph graph) throws GraphException, ListException {
        int counter = graph.size();
        int[] even = new int[counter];
        int[] odd = new int[counter];
        int evenCount = 0;
        int oddCount = 0;

        for (int i = 0; i < counter; i++) {
            int value = Integer.parseInt(graph.vertexList[i].data.toString());
            if (value % 2 == 0) {
                even[evenCount++] = value;
            } else {
                odd[oddCount++] = value;
            }
        }

        if (evenCount < 10 || oddCount < 10)
            throw new GraphException("Not enough even or odd vertices (need at least 10 of each)");

        int[] selectedEven = selectRandomSubset(even, evenCount, 10);
        int[] selectedOdd = selectRandomSubset(odd, oddCount, 10);


        for (int i = 0; i < 10; i++) {
            int weight = util.Utility.random(40) ;
            graph.addEdgeWeight(selectedEven[i], selectedOdd[i], weight);
        }
    }

    private int[] selectRandomSubset(int[] source, int size, int subsetSize) {

        boolean[] used = new boolean[size];
        int[] result = new int[subsetSize];
        int count = 0;

        while (count < subsetSize) {
            int index =util.Utility.random(size);
            if (!used[index]) {
                used[index] = true;
                result[count++] = source[index];
            }
        }
        return result;
    }
}