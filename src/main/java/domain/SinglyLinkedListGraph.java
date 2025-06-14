package domain;

import domain.list.ListException;
import domain.list.SinglyLinkedList;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

import java.util.*;

public class SinglyLinkedListGraph implements Graph {
    private SinglyLinkedList vertexList; //lista enlazada de vértices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public SinglyLinkedListGraph() {
        this.vertexList = new SinglyLinkedList();
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
    }

    @Override
    public int size() throws ListException {
        return vertexList.size();
    }

    @Override
    public void clear() {
        vertexList.clear();
    }

    @Override
    public boolean isEmpty() {
        return vertexList.isEmpty();
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        return indexOf(element)!=-1;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        int index = indexOf(a); //buscamos el índice del elemento en la lista enlazada
        if(index ==-1) return false;
        Vertex vertex = (Vertex) vertexList.getNode(index).data;
        return vertex!=null && !vertex.edgesList.isEmpty()
                && vertex.edgesList.contains(new EdgeWeight(b, null));
    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if(vertexList.isEmpty())
            vertexList.add(new Vertex(element)); //agrego un nuevo objeto vertice
        else if(!vertexList.contains(element))
            vertexList.add(new Vertex(element));
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        addRemoveVertexEdgeWeight(a, b, null, "addEdge"); //agrego la arista
        //grafo no dirigido
        addRemoveVertexEdgeWeight(b, a, null, "addEdge"); //agrego la arista

    }

    private int indexOf(Object element) throws ListException {
        for(int i=1;i<=vertexList.size();i++){
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            if(util.Utility.compare(vertex.data, element)==0){
                return i; //encontro el vertice
            }
        }//for
        return -1; //significa q la data de todos los vertices no coinciden con element
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("There is no edge between the vertexes[" + a + "] y [" + b + "]");
        addRemoveVertexEdgeWeight(a, b, weight, "addWeight"); //agrego la arista
        //grafo no dirigido
        addRemoveVertexEdgeWeight(b, a, weight, "addWeight"); //agrego la arista
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("Cannot add edge between vertexes ["+a+"] y ["+b+"]");
        if(!containsEdge(a, b)) {
            addRemoveVertexEdgeWeight(a, b, weight, "addEdge"); //agrego la arista
            //grafo no dirigido
            addRemoveVertexEdgeWeight(b, a, weight, "addEdge"); //agrego la arista
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Singly Linked List Graph is Empty");
        boolean removed = false;
        if(!vertexList.isEmpty() && containsVertex(element)){
            for (int i = 1; !removed&&i <= vertexList.size(); i++) {
                Vertex vertex = (Vertex) vertexList.getNode(i).data;
                if(util.Utility.compare(vertex.data, element)==0){ //ya lo encontro
                    vertexList.remove(new Vertex(element));
                    removed = true;
                    //ahora se debe eliminar la entrada de ese vertice de todas
                    //las listas de aristas de los otros vertices
                    int n = vertexList.size();
                    for (int j=1; vertexList!=null&&!vertexList.isEmpty()&&j<=n; j++) {
                        vertex = (Vertex) vertexList.getNode(j).data;
                        if(!vertex.edgesList.isEmpty())
                            addRemoveVertexEdgeWeight(vertex.data, element, null, "remove");
                    }
                }//if
            }//for i
        }//if
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if(!containsVertex(a)||!containsVertex(b))
            throw new GraphException("There's no some of the vertexes");
        addRemoveVertexEdgeWeight(a, b, null, "remove"); //suprimo la arista
        //grafo no dirigido
        addRemoveVertexEdgeWeight(b, a, null, "remove"); //suprimo la arista
    }

    private void addRemoveVertexEdgeWeight(Object a, Object b, Object weight, String action) throws ListException{
        for (int i = 1; i <= vertexList.size(); i++) {
            Vertex vertex = (Vertex) vertexList.getNode(i).data;
            if(util.Utility.compare(vertex.data, a)==0){
                switch(action){
                    case "addEdge":
                        vertex.edgesList.add(new EdgeWeight(b, weight));
                        break;
                    case "addWeight":
                        vertex.edgesList.getNode(new EdgeWeight(b, weight))
                                .setData(new EdgeWeight(b, weight));
                        break;
                    case "remove":
                        if(vertex.edgesList!=null&&!vertex.edgesList.isEmpty())
                            vertex.edgesList.remove(new EdgeWeight(b, weight));
                }
            }
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        stack.clear();
        stack.push(1); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertex = (Vertex)vertexList.getNode(index).data;
                vertex.setVisited(true); // lo marca
                info+=vertex+", ";
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }//dfs

    // Recorrido en amplitud
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 1
        Vertex vertex = (Vertex)vertexList.getNode(1).data;
        String info =vertex+", ";
        vertex.setVisited(true); //lo marca
        queue.clear();
        queue.enQueue(1); // encola el elemento
        int index2;
        while(!queue.isEmpty()){
            int index1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((index2=adjacentVertexNotVisited(index1)) != -1 ){
                // obtiene uno
                vertex = (Vertex)vertexList.getNode(index2).data;
                vertex.setVisited(true); //lo marco
                info+=vertex+", ";
                queue.enQueue(index2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) throws ListException {
        for (int i=1; i<=vertexList.size(); i++) {
            Vertex vertex = (Vertex)vertexList.getNode(i).data;
            vertex.setVisited(value); //value==true or false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Vertex vertex1 = (Vertex)vertexList.getNode(index).data;
        for(int i=1; i<=vertexList.size(); i++){
            Vertex vertex2 = (Vertex)vertexList.getNode(i).data;
            if(!vertex2.edgesList.isEmpty()&&vertex2.edgesList
                    .contains(new EdgeWeight(vertex1.data, null))
                    && !vertex2.isVisited())
                return i;
        }
        return -1;
    }

    public Vertex getVertexByIndex(int i) throws ListException {
        return (Vertex) vertexList.getNode(i).data;
    }

    public Object getWeightEdges(Object a, Object b) throws ListException {
        Vertex vertex = (Vertex) vertexList.getNode(new Vertex(a)).data;
        Object result = vertex.edgesList.getNode(new EdgeWeight(b, null)).data;

        return result;
    }

    public int[] dijkstra(int startVertex) throws GraphException, ListException {
        int n = vertexList.size();
        if (startVertex < 0 || startVertex >= n) {
            throw new GraphException("Invalid start vertex");
        }

        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startVertex] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startVertex, 0));

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int currentVertex = node.vertex;

            if (visited[currentVertex]) {
                continue;
            }

            visited[currentVertex] = true;

            Vertex vtx = (Vertex) vertexList.getNode(currentVertex + 1).data;
            SinglyLinkedList edges = vtx.edgesList;

            for (int i = 1; i <= edges.size(); i++) {
                EdgeWeight edge = (EdgeWeight) edges.getNode(i).data;
                int neighborIndex = indexOf(edge.getEdge()); // Busca el índice del vértice destino
                int weight = (int) edge.getWeight();

                if (neighborIndex == -1) continue; // Seguridad extra

                // Ojo: indexOf es 1-based, pero arrays en Java son 0-based
                neighborIndex = neighborIndex - 1;

                if (!visited[neighborIndex] && distances[currentVertex] + weight < distances[neighborIndex]) {
                    distances[neighborIndex] = distances[currentVertex] + weight;
                    pq.add(new Node(neighborIndex, distances[neighborIndex]));
                }
            }
        }

        return distances;
    }
    public SinglyLinkedListGraph kruskalMST() throws ListException, GraphException {
        int n = size();
        SinglyLinkedListGraph mst = new SinglyLinkedListGraph();

        // 1. Agregar todos los vértices
        for (int i = 1; i <= n; i++) {
            mst.addVertex(getVertexByIndex(i).data);
        }

        // 2. Guardar todas las aristas (sin duplicados)
        List<Edge> edges = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Vertex v = getVertexByIndex(i);
            if (v.edgesList == null || v.edgesList.isEmpty()) continue;
            for (int j = 1; j <= v.edgesList.size(); j++) {
                EdgeWeight ew = (EdgeWeight) v.edgesList.getNode(j).data;
                int weight = (int) ew.getWeight();
                // Evita duplicados (asumiendo grafo no dirigido)
                if (!edges.stream().anyMatch(e ->
                        (e.from.equals(ew.getEdge()) && e.to.equals(v.data)) ||
                                (e.to.equals(ew.getEdge()) && e.from.equals(v.data)))) {
                    edges.add(new Edge(v.data, ew.getEdge(), weight));
                }
            }
        }
        Collections.sort(edges);

        // 3. Disjoint Set para componentes
        Map<Object, Object> parent = new HashMap<>();
        for (int i = 1; i <= n; i++)
            parent.put(getVertexByIndex(i).data, getVertexByIndex(i).data);

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
                mst.addEdgeWeight(edge.to, edge.from, edge.weight); // MST es no dirigido
                parent.put(root1, root2);
                edgeCount++;
                if (edgeCount == n - 1) break;
            }
        }
        return mst;
    }
    public SinglyLinkedListGraph primMST() throws ListException, GraphException {
        int n = size();
        if (n == 0) throw new GraphException("El grafo está vacío, no se puede calcular el MST.");

        SinglyLinkedListGraph mst = new SinglyLinkedListGraph();
        // Asegúrate de agregar los vértices usando el mismo rango que usas en getVertexByIndex
        for (int i = 1; i <= n; i++) {
            mst.addVertex(getVertexByIndex(i).data);
        }

        boolean[] visited = new boolean[n + 1];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Object start = getVertexByIndex(1).data;

        visited[1] = true;
        Vertex v = getVertexByIndex(1);
        for (int j = 1; j <= v.edgesList.size(); j++) {
            EdgeWeight ew = (EdgeWeight) v.edgesList.getNode(j).data;
            pq.add(new Edge(v.data, ew.getEdge(), (int) ew.getWeight()));
        }

        int edgeCount = 0;
        while (!pq.isEmpty() && edgeCount < n - 1) {
            Edge edge = pq.poll();
            int toIdx = indexOf(edge.to); // indexOf debe ser 1-based
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


    @Override
    public String toString() {
        String result = "Singly Linked List Graph Content...";
        try {
            for(int i=1; i<=vertexList.size(); i++){
                Vertex vertex = (Vertex)vertexList.getNode(i).data;
                result+="\nThe vertex in the position "+i+" is: "+vertex+"\n";
                if(!vertex.edgesList.isEmpty()){
                    result+="........EDGES AND WEIGHTS: "+vertex.edgesList+"\n";
                }//if

            }//for
        } catch (ListException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }
}
