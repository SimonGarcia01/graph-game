package org.example.mapagrafos.structures;

import javafx.scene.canvas.GraphicsContext;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.util.Position;



import java.util.*;

public class ListGraph implements IGraph {
    private List<VertexL<Events>> vertices;
    private List<Edge<Events>> edges;

    // Properties of the Graph
    private final boolean simpleGraph;
    private final boolean directed;
    private final boolean allowLoops;

    public ListGraph(boolean simpleGraph, boolean directed, boolean allowLoops) {
        this.simpleGraph = simpleGraph;
        this.directed = directed;
        this.allowLoops = allowLoops;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    @Override
    public void bFS(Events rootValue) throws GraphException {
        VertexL<Events> startVertexL = searchVertexValue(rootValue.getPosition());

        if (startVertexL == null) {
            throw new GraphException("The vertex with the specified value was not found.");
        }

        for (VertexL<Events> vertexL : vertices) {
            vertexL.setColor(Color.WHITE);
            vertexL.setPredecessor(null);
            vertexL.setDistance(Integer.MAX_VALUE);
        }

        Queue<VertexL<Events>> queue = new LinkedList<>();
        startVertexL.setDistance(0);
        startVertexL.setColor(Color.GRAY);
        queue.add(startVertexL);

        while (!queue.isEmpty()) {
            VertexL<Events> current = queue.remove();
            for (Edge<Events> edge : current.getEdges()) {
                VertexL<Events> adjVertexL = edge.getEndVertex();
                if (adjVertexL.getColor() == Color.WHITE) {
                    adjVertexL.setColor(Color.GRAY);
                    adjVertexL.setDistance(current.getDistance() + 1);
                    adjVertexL.setPredecessor(current);
                    queue.add(adjVertexL);
                }
            }
            current.setColor(Color.BLACK);
        }
    }

    @Override
    public void dijkstra(Events rootValue) throws GraphException{
        if(rootValue == null) {
            throw new GraphException("A null event was searched.");
        }

        VertexL<Events> startVertexL = searchVertexValue(rootValue.getPosition());

        if (startVertexL == null) {
            throw new GraphException("The vertex with the specified value was not found.");
        }

        for (VertexL<Events> vertexL : vertices) {
            vertexL.setPredecessor(null);
            vertexL.setDistance(Integer.MAX_VALUE);
        }

        startVertexL.setDistance(0);

        PriorityQueue<VertexL<Events>> queue = new PriorityQueue<>();
        queue.add(startVertexL);

        while(!queue.isEmpty()){
            VertexL<Events> current = queue.poll();
            for(Edge<Events> edge : current.getEdges()){
                VertexL<Events> neighbor = edge.getEndVertex();
                int alt = current.getDistance() + edge.getWeight();
                if(alt < neighbor.getDistance()){

                    neighbor.setDistance(alt);
                    neighbor.setPredecessor(current);

                    //Re add the vertex but with the new priority
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    @Override //This is a mix between dijkstra and bfs trying to find longest path
    public void antiDijkstra(Events rootValue) throws GraphException {
        VertexL<Events> startVertexL = searchVertexValue(rootValue.getPosition());

        if (startVertexL == null) {
            throw new GraphException("The vertex with the specified value was not found.");
        }


        for (VertexL<Events> vertexL : vertices) {
            vertexL.setColor(Color.WHITE);
            vertexL.setPredecessor(null);
            vertexL.setDistance(Integer.MIN_VALUE);
        }

        startVertexL.setDistance(0);
        startVertexL.setColor(Color.GRAY);

        PriorityQueue<VertexL<Events>> queue = new PriorityQueue<>(Comparator.comparingInt(VertexL<Events>::getDistance).reversed());
        queue.add(startVertexL);

        while (!queue.isEmpty()) {
            VertexL<Events> current = queue.poll();

            for (Edge<Events> edge : current.getEdges()) {
                VertexL<Events> neighbor = edge.getEndVertex();

                if (neighbor.getColor() == Color.WHITE) { // Prevent inifinite loop using colors
                    int alt = current.getDistance() + edge.getWeight();

                    if (alt > neighbor.getDistance()) {
                        neighbor.setDistance(alt);
                        neighbor.setPredecessor(current);
                    }

                    neighbor.setColor(Color.GRAY);
                    queue.add(neighbor);
                }
            }

            current.setColor(Color.BLACK);
        }
    }

    public VertexL<Events> searchVertexValue(Position value) {
        for (VertexL<Events> vertexL : vertices) {
            if (vertexL.getValue().getCol() == value.getCol() && vertexL.getValue().getRow() == value.getRow()) {
                return vertexL;
            }
        }
        return null;
    }

    @Override
    public void add(Events value) {
        //Make sure the value is unique
        if (searchVertexValue(value.getPosition()) == null) {
            vertices.add(new VertexL<>(value));
        }
    }

    @Override
    public void addEdge(Events startValue, Events endValue, int weight) throws GraphException {
        VertexL<Events> startVertexL = searchVertexValue(startValue.getPosition());
        VertexL<Events> endVertexL = searchVertexValue(endValue.getPosition());

        if (startVertexL == null || endVertexL == null) {
            throw new GraphException("One or both vertices do not exist in the graph.");
        }

        if (startVertexL.equals(endVertexL) && !allowLoops) {
            throw new GraphException("Loops are not allowed.");
        }

        if (simpleGraph) {
            //Must check if there is an edge with that vertex
            boolean edgeExists = startVertexL.searchEdge(endVertexL) != null;
            if (!directed) {
                //There shouldn't be an edge coming back either way
                edgeExists = edgeExists || endVertexL.searchEdge(startVertexL) != null;
            }

            if (!edgeExists) {
                addEdgeInternal(weight, startVertexL, endVertexL);
            }
        } else {
            //If it's a multi graph there's no need to check
            addEdgeInternal(weight, startVertexL, endVertexL);
        }
    }

    private void addEdgeInternal(int weight, VertexL<Events> startVertexL, VertexL<Events> endVertexL) {
        Edge<Events> startEdge = new Edge<>(weight, startVertexL, endVertexL);
        startVertexL.addEdge(startEdge);
        edges.add(startEdge);
        if (!directed) {
            Edge<Events> endEdge = new Edge<>(weight, endVertexL, startVertexL);
            endVertexL.addEdge(endEdge);
            edges.add(endEdge);
        }
    }

    public boolean hasNeighborInNextRow(int row, int col) throws GraphException {
        // Buscar el vértice correspondiente a la posición (row, col)
        VertexL<Events> vertexL = searchVertexValue(new Position(row, col));
        if (vertexL == null) {
            System.out.println("row: " + row + ", col: " + col);
            throw new GraphException("VertexL not found for the specified position.");
        }

        // Recorrer las aristas del vértice actual
        for (Edge<Events> edge : vertexL.getEdges()) {
            // Obtener el valor del vecino (evento)
            Events neighborEvent = edge.getEndVertex().getValue();

            // Verificar si el vecino está en la siguiente fila
            if (neighborEvent.getPosition().getRow() == row + 2) {
                return true; // Encontramos un vecino en la siguiente fila
            }
        }

        return false; // No hay vecinos en la siguiente fila
    }

    public void drawShortestPath(GraphicsContext gc, Events rootEvent, Events targetEvent, int tileSize) throws GraphException {
        // Ejecutar Dijkstra para calcular distancias mínimas y predecesores
        dijkstra(rootEvent);

        // Buscar el nodo objetivo
        VertexL<Events> targetVertexL = searchVertexValue(targetEvent.getPosition());
        if (targetVertexL == null) {
            throw new GraphException("The target vertex was not found.");
        }

        // Reconstruir el camino mínimo usando los predecesores
        List<VertexL<Events>> shortestPath = new ArrayList<>();
        VertexL<Events> current = targetVertexL;

        while (current != null) {
            shortestPath.add(current);
            current = (VertexL<Events>) current.getPredecessor();
        }

        // Dibujar el camino en el GraphicsContext
        gc.setLineWidth(3);

        // Recorrer el camino y dibujar líneas entre los nodos
        for (int i = shortestPath.size() - 1; i > 0; i--) {
            VertexL<Events> from = shortestPath.get(i);
            VertexL<Events> to = shortestPath.get(i - 1);

            Position fromPos = from.getValue().getPosition();
            Position toPos = to.getValue().getPosition();

            double fromX = fromPos.getCol() * tileSize + tileSize / 2.0;
            double fromY = fromPos.getRow() * tileSize + tileSize / 2.0;
            double toX = toPos.getCol() * tileSize + tileSize / 2.0;
            double toY = toPos.getRow() * tileSize + tileSize / 2.0;

            gc.strokeLine(fromX, fromY, toX, toY);
        }
    }

    @Override
    public void drawLongestPath(GraphicsContext gc, Events rootEvent, Events targetEvent, int tileSize) throws GraphException {
        antiDijkstra(rootEvent);

        VertexL<Events> targetVertexL = searchVertexValue(targetEvent.getPosition());
        if (targetVertexL == null) {
            throw new GraphException("The target vertex was not found.");
        }

        List<VertexM<Events>> longestPath = new ArrayList<>();
        VertexM<Events> current = targetVertexL;
        while (current != null) {
            longestPath.add(current);
            current = current.getPredecessor();
        }

        gc.setLineWidth(3);

        for (int i = longestPath.size() - 1; i > 0; i--) {
            VertexM<Events> from = longestPath.get(i);
            VertexM<Events> to = longestPath.get(i - 1);

            Position fromPos = from.getValue().getPosition();
            Position toPos = to.getValue().getPosition();

            double fromX = fromPos.getCol() * tileSize + tileSize / 2.0;
            double fromY = fromPos.getRow() * tileSize + tileSize / 2.0;
            double toX = toPos.getCol() * tileSize + tileSize / 2.0;
            double toY = toPos.getRow() * tileSize + tileSize / 2.0;

            gc.strokeLine(fromX, fromY, toX, toY);
        }
    }
}
