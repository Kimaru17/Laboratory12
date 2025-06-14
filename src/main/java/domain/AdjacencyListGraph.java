package domain;

import domain.list.ListException;
import domain.list.SinglyLinkedList;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

import java.util.*;

public class AdjacencyListGraph implements Graph {
    public Vertex[] vertexList; //arreglo de objetos tupo vértice
    private int n; //max de elementos
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyListGraph(int n) {
        if (n <= 0) System.exit(1); //sale con status==1 (error)
        this.n = n;
        this.counter = 0;
        this.vertexList = new Vertex[n];
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.counter = 0; //inicializo contador de vértices
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");
        //opcion-1
       /* for (int i = 0; i < counter; i++) {
            if(util.Utility.compare(vertexList[i].data, element)==0)
                return true;
        }*/
        //opcion-2
        return indexOf(element) != -1;
        //return false;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");
        return !vertexList[indexOf(a)].edgesList.isEmpty()
                && vertexList[indexOf(a)].edgesList.contains(new EdgeWeight(b, null));
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if (counter >= vertexList.length)
            throw new GraphException("Adjacency List Graph is Full");
        vertexList[counter++] = new Vertex(element);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes [" + a + "] y [" + b + "]");
        vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, null));
        //grafo no dirigido
        vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, null));

    }

    private int indexOf(Object element) {
        for (int i = 0; i < counter; i++) {
            if (util.Utility.compare(vertexList[i].data, element) == 0)
                return i; //retorna la pos en el arreglo de objectos vertexList
        }
        return -1; //significa q la data de todos los vertices no coinciden con element
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between the vertexes[" + a + "] y [" + b + "]");
        updateEdgesListEdgeWeight(a, b, weight);
        //GRAFO NO DIRIGIDO
        updateEdgesListEdgeWeight(b, a, weight);
    }

    private void updateEdgesListEdgeWeight(Object a, Object b, Object weight) throws ListException {
        EdgeWeight ew = (EdgeWeight) vertexList[indexOf(a)].edgesList
                .getNode(new EdgeWeight(b, null)).getData();
        //setteo el peso en el campo respectivo
        ew.setWeight(weight);
        //ahora actualizo la info en la lista de aristas correspondiente
        vertexList[indexOf(a)].edgesList.getNode(new EdgeWeight(b, null))
                .setData(ew);
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes [" + a + "] y [" + b + "]");
        if (!containsEdge(a, b)) { //si no existe la arista
            vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, weight));
            //grafo no dirigido
            vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, weight));
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");
        if (containsVertex(element)) {
            for (int i = 0; i < counter; i++) {
                if (util.Utility.compare(vertexList[i].data, element) == 0) {
                    //ya lo encontro, ahora
                    //se debe suprimir el vertice a eliminar de todas las listas
                    //enlazadas de los otros vértices
                    for (int j = 0; j < counter; j++) {
                        if (containsEdge(vertexList[j].data, element))
                            removeEdge(vertexList[j].data, element);
                    }

                    //ahora, debemos suprimir el vértice
                    for (int j = i; j < counter - 1; j++) {
                        vertexList[j] = vertexList[j + 1];
                    }
                    counter--; //decrementamos el contador de vértices
                }
            }
        }
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("There's no some of the vertexes");
        if (!vertexList[indexOf(a)].edgesList.isEmpty()) {
            vertexList[indexOf(a)].edgesList.remove(new EdgeWeight(b, null));
        }
        //grafo no dirigido
        if (!vertexList[indexOf(b)].edgesList.isEmpty()) {
            vertexList[indexOf(b)].edgesList.remove(new EdgeWeight(a, null));
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); //lo apila
        while (!stack.isEmpty()) {
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if (index == -1) // no lo encontro
                stack.pop();
            else {
                vertexList[index].setVisited(true); // lo marca
                info += vertexList[index].data + ", "; //lo muestra
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }

    //Recorrido en amplitud
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        queue.clear();
        queue.enQueue(0); // encola el elemento
        int v2;
        while (!queue.isEmpty()) {
            int v1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while ((v2 = adjacentVertexNotVisited(v1)) != -1) {
                // obtiene uno
                vertexList[v2].setVisited(true); // lo marca
                info += vertexList[v2].data + ", "; //lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Object vertexData = vertexList[index].data;
        for (int i = 0; i < counter; i++) {
            // Verificar primero si la lista de aristas está vacía
            if (vertexList[i].edgesList.isEmpty()) {
                continue; // Saltar si está vacía
            }
            // Luego verificar si contiene el vértice y si no ha sido visitado
            if (vertexList[i].edgesList.contains(new EdgeWeight(vertexData, null))
                    && !vertexList[i].isVisited()) {
                return i;
            }
        }
        return -1;
    }

    public Vertex getVertexByIndex(int i){
        return vertexList[i];
    }
    public Object getWeightEdges(Object a, Object b) throws ListException {
        EdgeWeight ed = (EdgeWeight) vertexList[indexOf(a)].edgesList.getNode(new EdgeWeight(b, null)).data;
        return ed.getWeight();
    }

    @Override
    public String toString() {
        String result = "Adjacency List Graph Content...";
        //se muestran todos los vértices del grafo
        for (int i = 0; i < counter; i++) {
            result+="\nThe vextex in the position: "+i+" is: "+vertexList[i].data;
            if(!vertexList[i].edgesList.isEmpty())
                result+="\n......EDGES AND WEIGHTS: "+vertexList[i].edgesList.toString();
        }
        return result;

    }
    public int[] dijkstra(int startVertex) throws GraphException, ListException {
        if (startVertex < 0 || startVertex >= counter) {
            throw new GraphException("Invalid start vertex");
        }

        int[] distances = new int[counter];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startVertex] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startVertex, 0));

        boolean[] visited = new boolean[counter];

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int currentVertex = node.vertex;

            if (visited[currentVertex]) {
                continue;
            }

            visited[currentVertex] = true;

            SinglyLinkedList edges = vertexList[currentVertex].edgesList;
            for (int i = 1; i <= edges.size(); i++) {
                EdgeWeight edge = (EdgeWeight) edges.getNode(i).data;
                int neighborIndex = indexOf(edge.getEdge());
                int weight = (int) edge.getWeight();

                if (!visited[neighborIndex] && distances[currentVertex] + weight < distances[neighborIndex]) {
                    distances[neighborIndex] = distances[currentVertex] + weight;
                    pq.add(new Node(neighborIndex, distances[neighborIndex]));
                }
            }
        }

        return distances;
    }

    public void connectEvenAndOddVertices() throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");


        // Conectar todos los pares entre sí
        for (int i = 0; i < counter; i++) {
            int dataI = Integer.parseInt(vertexList[i].data.toString());
            if (dataI % 2 == 0) {
                for (int j = i + 1; j < counter; j++) {
                    int dataJ = Integer.parseInt(vertexList[j].data.toString());
                    if (dataJ % 2 == 0) {
                        int weight = util.Utility.random(40)+1; // entre 1 y 40
                        addEdgeWeight(dataI, dataJ, weight);
                    }
                }
            }
        }

        // Conectar todos los impares entre sí
        for (int i = 0; i < counter; i++) {
            int dataI = Integer.parseInt(vertexList[i].data.toString());
            if (dataI % 2 != 0) {
                for (int j = i + 1; j < counter; j++) {
                    int dataJ = Integer.parseInt(vertexList[j].data.toString());
                    if (dataJ % 2 != 0) {
                        int weight = util.Utility.random(40)+1; // entre 1 y 40
                        addEdgeWeight(dataI, dataJ, weight);
                    }
                }
            }
        }
    }
/// ////////////////////////Metodos Prim y Kruskal//////////////////
    private class Edge implements Comparable<Edge> {
        int src;
        int dest;
        int weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    // Clase auxiliar para Kruskal: estructura Union-Find (Disjoint Set Union - DSU)
    private class UnionFind {
        private int[] parent, rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }

        public int find(int u) {
            if (parent[u] != u) parent[u] = find(parent[u]);
            return parent[u];
        }

        public void union(int u, int v) {
            int rootU = find(u);
            int rootV = find(v);
            if (rootU != rootV) {
                if (rank[rootU] < rank[rootV]) parent[rootU] = rootV;
                else if (rank[rootU] > rank[rootV]) parent[rootV] = rootU;
                else {
                    parent[rootV] = rootU;
                    rank[rootU]++;
                }
            }
        }
    }

    // Método Prim: retorna el costo total del árbol de expansión mínima (MST)
    public int prim(int startVertex) throws GraphException, ListException {
        if (startVertex < 0 || startVertex >= counter)
            throw new GraphException("Invalid start vertex");

        boolean[] inMST = new boolean[counter];
        int[] key = new int[counter];
        for (int i = 0; i < counter; i++) {
            key[i] = Integer.MAX_VALUE;
        }
        key[startVertex] = 0;

        for (int count = 0; count < counter - 1; count++) {
            int u = minKeyVertex(key, inMST);
            inMST[u] = true;

            SinglyLinkedList edges = vertexList[u].edgesList;
            for (int i = 1; i <= edges.size(); i++) {
                EdgeWeight edge = (EdgeWeight) edges.getNode(i).data;
                int v = indexOf(edge.getEdge());
                int weight = (int) edge.getWeight();

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                }
            }
        }

        int totalWeight = 0;
        for (int w : key) {
            if (w != Integer.MAX_VALUE) totalWeight += w;
        }
        return totalWeight;
    }

    // Método auxiliar para Prim: encuentra el vértice con el valor mínimo de key que no está en MST
    private int minKeyVertex(int[] key, boolean[] inMST) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < counter; v++) {
            if (!inMST[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Método Kruskal: retorna una lista de aristas que forman el MST
    public List<Edge> kruskal() throws GraphException, ListException {
        List<Edge> edges = new ArrayList<>();

        // Obtener todas las aristas del grafo (sin duplicados, ya que el grafo es no dirigido)
        for (int i = 0; i < counter; i++) {
            SinglyLinkedList edgesList = vertexList[i].edgesList;
            for (int j = 1; j <= edgesList.size(); j++) {
                EdgeWeight ew = (EdgeWeight) edgesList.getNode(j).data;
                int dest = indexOf(ew.getEdge());
                int weight = (int) ew.getWeight();

                if (i < dest) { // Evitar duplicados porque es no dirigido
                    edges.add(new Edge(i, dest, weight));
                }
            }
        }

        Collections.sort(edges);

        UnionFind uf = new UnionFind(counter);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            int rootSrc = uf.find(edge.src);
            int rootDest = uf.find(edge.dest);

            if (rootSrc != rootDest) {
                mst.add(edge);
                uf.union(rootSrc, rootDest);
            }
        }

        return mst;
    }
}
