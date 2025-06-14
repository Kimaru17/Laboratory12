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
    public AdjacencyMatrixGraph kruskalMST() throws GraphException, ListException {
        int n = size();
        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(n);

        // 1. Agregar todos los vértices
        for (int i = 0; i < n; i++) {
            mst.addVertex(getVertexByIndex(i).data);
        }

        // 2. Guardar todas las aristas (sin duplicados, solo parte superior de la matriz)
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // Solo una vez cada arista
                Object weightObj = adjacencyMatrix[i][j];
                if (weightObj != null && !weightObj.equals(0)) {
                    int weight = (int) weightObj;
                    edges.add(new Edge(vertexList[i].data, vertexList[j].data, weight));
                }
            }
        }
        Collections.sort(edges);

        // 3. Disjoint Set para componentes
        Map<Object, Object> parent = new HashMap<>();
        for (int i = 0; i < n; i++) {
            parent.put(getVertexByIndex(i).data, getVertexByIndex(i).data);
        }
        java.util.function.Function<Object, Object> find = new java.util.function.Function<>() {
            @Override
            public Object apply(Object v) {
                if (!parent.get(v).equals(v))
                    parent.put(v, this.apply(parent.get(v)));
                return parent.get(v);
            }
        };

        // 4. Construye el MST
        int edgeCount = 0;
        for (Edge edge : edges) {
            Object root1 = find.apply(edge.from);
            Object root2 = find.apply(edge.to);
            if (!root1.equals(root2)) {
                mst.addEdgeWeight(edge.from, edge.to, edge.weight);
                mst.addEdgeWeight(edge.to, edge.from, edge.weight); // Porque el MST es no dirigido
                parent.put(root1, root2);
                edgeCount++;
                if (edgeCount == n - 1) break;
            }
        }
        return mst;
    }
    public AdjacencyMatrixGraph primMST() throws GraphException, ListException {
        int n = size();
        AdjacencyMatrixGraph mst = new AdjacencyMatrixGraph(n);
        for (int i = 0; i < n; i++) mst.addVertex(getVertexByIndex(i).data);

        boolean[] visited = new boolean[n];
        int[] minEdge = new int[n];
        Object[] parent = new Object[n];
        Arrays.fill(minEdge, Integer.MAX_VALUE);
        minEdge[0] = 0;
        parent[0] = null;

        for (int count = 0; count < n - 1; count++) {
            int u = -1;
            for (int i = 0; i < n; i++)
                if (!visited[i] && (u == -1 || minEdge[i] < minEdge[u]))
                    u = i;
            visited[u] = true;
            for (int v = 0; v < n; v++) {
                if (adjacencyMatrix[u][v] instanceof Integer && (int) adjacencyMatrix[u][v] != 0 && !visited[v] && (int) adjacencyMatrix[u][v] < minEdge[v]) {
                    minEdge[v] = (int) adjacencyMatrix[u][v];
                    parent[v] = vertexList[u].data;
                }
            }
        }
        for (int v = 1; v < n; v++) {
            if (parent[v] != null) {
                mst.addEdgeWeight(parent[v], vertexList[v].data, minEdge[v]);
            }
        }
        return mst;
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
}
