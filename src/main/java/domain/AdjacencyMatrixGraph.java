package domain;

import domain.list.ListException;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

import java.util.*;

public class AdjacencyMatrixGraph implements Graph {
    private Vertex[] vertexList; //arreglo de objetos tupo vértice
    private Object[][] adjacencyMatrix; //arreglo bidimensional
    private int n; //max de elementos
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyMatrixGraph(int n) {
        if (n <= 0) System.exit(1); //sale con status==1 (error)
        this.n = n;
        this.counter = 0;
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = new Object[n][n];
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
        initMatrix(); //inicializa matriz de objetos con cero
    }

    private void initMatrix() {

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.adjacencyMatrix[i][j] = 0; //init con ceros
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = new Object[n][n];
        this.counter = 0; //inicializo contador de vértices
        this.initMatrix();
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");
        //opcion-1
       /* for (int i = 0; i < counter; i++) {
            if(util.Utility.compare(vertexList[i].data, element)==0)
                return true;
        }*/
        //opcion-2
        return indexOf(element)!=-1;
        //return false;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");
        return !(util.Utility.compare(adjacencyMatrix[indexOf(a)][indexOf(b)], 0)==0);
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(counter>=vertexList.length)
            throw new GraphException("Adjacency Matrix Graph is Full");
        vertexList[counter++] = new Vertex(element);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = 1; //hay una arista
        //grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = 1; //hay una arista

    }

    private int indexOf(Object element){
        for (int i = 0; i < counter; i++) {
            if(util.Utility.compare(vertexList[i].data, element)==0)
                return i; //retorna la pos en el arreglo de objectos vertexList
        }
        return -1; //significa q la data de todos los vertices no coinciden con element
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsEdge(a, b))
            throw new GraphException("There is no edge between the vertexes["+a+"] y ["+b+"]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight; //hay una arista
        //grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight; //hay una arista
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight; //hay una arista
        //grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight; //hay una arista
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");
        int index = indexOf(element);
        if(index!=-1){ //si existe el indice
            for (int i = index; i < counter-1; i++) {
                vertexList[i] = vertexList[i+1];
                //elimino el vertice, tambien debo eliminar todas las aristas

                //movemos todas las filas, una posición hacia arriba
                for (int j = 0; j < counter; j++)
                    adjacencyMatrix[i][j] = adjacencyMatrix[i+1][j];
            }
            //ahora, movemos todas las columnas, una posición a la izq
            for (int i = 0; i < counter; i++) {
                for (int j = index; j < counter-1; j++) {
                    adjacencyMatrix[i][j] = adjacencyMatrix[i][j+1];
                }
            }
            counter--; //decrementamos el contador de vertices agregados
        }
        //que pasa, si ya no quedan vertices
        if(counter==0) initMatrix();
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("There's no some of the vertexes");
        int i = indexOf(a);
        int j = indexOf(b);
        if(i!=-1 && j!=-1){
            adjacencyMatrix[i][j] = 0;
            adjacencyMatrix[j][i] = 0; //grafo no dirigido
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

    private int adjacentVertexNotVisited(int index) {
        for (int i = 0; i < counter; i++) {
            if (!adjacencyMatrix[index][i].equals(0)
                    && !vertexList[i].isVisited())
                return i;//retorna la posicion del vertice adyacente no visitado
        }//for i
        return -1;
    }
    public Vertex getVertexByIndex(int i){
        return vertexList[i];
    }

    public Object getWeightEdges(Object a, Object b){
        return adjacencyMatrix[indexOf(a)][indexOf(b)];
    }

    public int[] dijkstra(int startVertex) throws GraphException {
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

            for (int i = 0; i < counter; i++) {
                if (!adjacencyMatrix[currentVertex][i].equals(0)) {
                    int weight = (int) adjacencyMatrix[currentVertex][i];
                    if (!visited[i] && distances[currentVertex] + weight < distances[i]) {
                        distances[i] = distances[currentVertex] + weight;
                        pq.add(new Node(i, distances[i]));
                    }
                }
            }
        }

        return distances;
    }

    @Override
    public String toString() {
        String result = "Adjacency Matrix Graph Content...";
        //se muestran todos los vértices del grafo
        for (int i = 0; i < counter; i++) {
            result+="\nThe vextex in the position: "+i+" is: "+vertexList[i].data;
        }
        //agregamos la info de las aristas y pesos
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if(util.Utility.compare(adjacencyMatrix[i][j], 0)!=0) {//si existe arista
                    //si existe una arista
                    result+="\nThere is edge between the vertexes: "+vertexList[i].data+"...."
                            +vertexList[j].data;
                    //si existe peso que lo muestre
                    if(util.Utility.compare(adjacencyMatrix[i][j], 0)!=0){
                        //si matriz[fila][col] !=1 existe un peso agregado
                        result+="_____WEIGHT: "+adjacencyMatrix[i][j];
                    }
                }
            }
        }

        return result;
    }

    /// ///////////////////////////Prim y Kruskal//////////////////////////
    public int prim(int startVertex) throws GraphException {
        if (startVertex < 0 || startVertex >= counter)
            throw new GraphException("Invalid start vertex");

        boolean[] inMST = new boolean[counter];
        int[] key = new int[counter];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[startVertex] = 0;

        int totalWeight = 0;

        for (int count = 0; count < counter; count++) {
            int u = -1;
            int minKey = Integer.MAX_VALUE;
            for (int v = 0; v < counter; v++) {
                if (!inMST[v] && key[v] < minKey) {
                    minKey = key[v];
                    u = v;
                }
            }

            if (u == -1) break;

            inMST[u] = true;
            totalWeight += key[u];

            for (int v = 0; v < counter; v++) {
                if (!inMST[v] && util.Utility.compare(adjacencyMatrix[u][v], 0) != 0) {
                    int weight = (int) adjacencyMatrix[u][v];
                    if (weight < key[v]) {
                        key[v] = weight;
                    }
                }
            }
        }

        return totalWeight;
    }

    // --- KRUSKAL ---

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

        @Override
        public String toString() {
            return "Edge{" + "src=" + vertexList[src].data + ", dest=" + vertexList[dest].data + ", weight=" + weight + '}';
        }
    }

    private class UnionFind {
        int[] parent;
        int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
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

    public List<Edge> kruskal() throws GraphException {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            for (int j = i + 1; j < counter; j++) {
                if (util.Utility.compare(adjacencyMatrix[i][j], 0) != 0) {
                    edges.add(new Edge(i, j, (int) adjacencyMatrix[i][j]));
                }
            }
        }

        Collections.sort(edges);

        UnionFind uf = new UnionFind(counter);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            if (uf.find(edge.src) != uf.find(edge.dest)) {
                uf.union(edge.src, edge.dest);
                mst.add(edge);
            }
        }

        return mst;
    }
}
