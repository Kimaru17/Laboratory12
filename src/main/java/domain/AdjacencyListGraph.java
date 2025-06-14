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
        Vertex current = vertexList[index];

        if (current.edgesList.isEmpty())
            return -1;

        // Recorre la lista de adyacencia del vértice actual
        for (int i = 1; i <= current.edgesList.size(); i++) {
            EdgeWeight edge = (EdgeWeight) current.edgesList.getNode(i).data;
            int pos = indexOf(edge.getEdge()); // buscamos índice del destino

            if (!vertexList[pos].isVisited()) {
                return pos;
            }
        }

        return -1; // no hay vecinos no visitados
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
    public AdjacencyListGraph kruskalMST() throws ListException, GraphException {
        int n = size();
        AdjacencyListGraph mst = new AdjacencyListGraph(n);

        // 1. Agregar todos los vértices
        for (int i = 0; i < n; i++) {
            mst.addVertex(getVertexByIndex(i).data);
        }

        // 2. Guardar todas las aristas (sin duplicados)
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vertex v = getVertexByIndex(i);
            if (v.edgesList == null || v.edgesList.isEmpty()) continue; // Protección contra vacíos
            for (int j = 1; j <= v.edgesList.size(); j++) {
                EdgeWeight ew = (EdgeWeight) v.edgesList.getNode(j).data;
                int weight = (int) ew.getWeight();
                // Evita agregar la arista dos veces (no dirigidos)
                if (!edges.stream().anyMatch(e ->
                        (e.from.equals(ew.getEdge()) && e.to.equals(v.data)) ||
                                (e.to.equals(ew.getEdge()) && e.from.equals(v.data)))) {
                    edges.add(new Edge(v.data, ew.getEdge(), weight));
                }
            }
        }
        Collections.sort(edges);

        // 3. Estructura para unión y búsqueda (disjoint set)
        Map<Object, Object> parent = new HashMap<>();
        for (int i = 0; i < n; i++)
            parent.put(getVertexByIndex(i).data, getVertexByIndex(i).data);

        // Encuentra representante
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
                parent.put(root1, root2);
                edgeCount++;
                if (edgeCount == n - 1) break; // Ya tenemos MST
            }
        }
        return mst;
    }



    public AdjacencyListGraph primMST() throws ListException, GraphException {
        int n = size();
        if (n == 0) return new AdjacencyListGraph(1); // Grafo vacío

        AdjacencyListGraph mst = new AdjacencyListGraph(n);
        for (int i = 0; i < n; i++) mst.addVertex(getVertexByIndex(i).data);

        // Buscar el primer vértice con al menos una arista
        int startIdx = -1;
        for (int i = 0; i < n; i++) {
            Vertex v = getVertexByIndex(i);
            if (v.edgesList != null && !v.edgesList.isEmpty()) {
                startIdx = i;
                break;
            }
        }
        if (startIdx == -1) return mst; // Todos los vértices están aislados

        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Object start = getVertexByIndex(startIdx).data;

        visited[startIdx] = true;
        Vertex v = getVertexByIndex(startIdx);
        for (int j = 1; j <= v.edgesList.size(); j++) {
            EdgeWeight ew = (EdgeWeight) v.edgesList.getNode(j).data;
            pq.add(new Edge(v.data, ew.getEdge(), (int) ew.getWeight()));
        }

        int edgeCount = 0;
        while (!pq.isEmpty() && edgeCount < n - 1) {
            Edge edge = pq.poll();
            int toIdx = indexOf(edge.to);
            if (!visited[toIdx]) {
                mst.addEdgeWeight(edge.from, edge.to, edge.weight);
                visited[toIdx] = true;
                Vertex newV = getVertexByIndex(toIdx);
                for (int j = 1; j <= newV.edgesList.size(); j++) {
                    EdgeWeight ew = (EdgeWeight) newV.edgesList.getNode(j).data;
                    if (!visited[indexOf(ew.getEdge())])
                        pq.add(new Edge(newV.data, ew.getEdge(), (int) ew.getWeight()));
                }
                edgeCount++;
            }
        }
        return mst;
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
}
