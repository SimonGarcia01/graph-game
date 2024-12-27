package org.example.mapagrafos.structures;

import javafx.scene.canvas.GraphicsContext;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.util.Position;

import java.util.*;

public class MatrixGraph implements IGraph {

    //Graph properties
    private final List<VertexM<Events>> vertices;
    private final int SIZE;
    private final List<EdgeM<Events>>[][] edges;
    private final boolean simpleGraph;
    private final boolean directed;
    private final boolean allowLoops;

    private final Queue<Integer> availablePositions = new LinkedList<>();

    @SuppressWarnings("unchecked") //recommended in stack overflow
    public MatrixGraph(int size, boolean simpleGraph, boolean directed, boolean allowLoops) throws GraphException{
        if (size <= 0) {
            throw new GraphException("Size must be positive.");
        }

        this.vertices = new ArrayList<>();
        this.SIZE = size;
        edges = (List<EdgeM<Events>>[][]) new List[SIZE][SIZE];
        this.simpleGraph = simpleGraph;
        this.directed = directed;
        this.allowLoops = allowLoops;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                edges[i][j] = new ArrayList<>();
            }
        }
    }

    @Override
    public void add(Events value) {
        //Make sure the value is unique
        if (searchVertexValue(value.getPosition()) == null) {
            //take up the position of a removed vertex if there is one
            if (!availablePositions.isEmpty()) {
                int position = availablePositions.poll();
                vertices.set(position, new VertexM<>(value));
            } else if(vertices.size() < SIZE){
                vertices.add(new VertexM<>(value));
            }
        }
    }

    @Override
    public void addEdge(Events startValue, Events endValue, int weight) throws GraphException {
        VertexM<Events> startVertex = searchVertexValue(startValue.getPosition());
        VertexM<Events> endVertex = searchVertexValue(endValue.getPosition());

        if (startVertex == null || endVertex == null) {
            throw new GraphException("One or both vertices do not exist in the graph.");
        }

        if (startVertex.equals(endVertex) && !allowLoops) {
            throw new GraphException("Loops are not allowed.");
        }


        int intStartVertex = vertices.indexOf(startVertex);
        int intEndVertex = vertices.indexOf(endVertex);

        if (simpleGraph) {
            boolean edgeExists = !edges[intStartVertex][intEndVertex].isEmpty();

            if (!directed) {
                edgeExists = edgeExists || !edges[intEndVertex][intStartVertex].isEmpty();
            }

            if (edgeExists) {
                throw new GraphException("An edge already exists between the specified vertices.");
            }
        }

        // If it's a multi graph none of the preconditions from before must be checked
        addEdgeInternal(intStartVertex, intEndVertex, weight, startVertex, endVertex);

        if (!directed) {
            addEdgeInternal(intEndVertex, intStartVertex, weight, endVertex, startVertex);
        }
    }

    //Made this just to reduce the repetitive lines
    private void addEdgeInternal(int from, int to, int weight, VertexM<Events> startVertex, VertexM<Events> endVertex) {
        EdgeM<Events> edge = new EdgeM<>(weight, startVertex, endVertex);
        edges[from][to].add(edge);
    }

    @Override
    public void bFS(Events rootValue) throws GraphException {
        VertexM<Events> startVertex = searchVertexValue(rootValue.getPosition());

        if(startVertex == null){
            throw new GraphException("The vertex with the specified value was not found.");
        }

        for(VertexM<Events> vertex : vertices){
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }

        Queue<VertexM<Events>> queue = new LinkedList<>();
        startVertex.setDistance(0);
        startVertex.setColor(Color.GRAY);
        queue.add(startVertex);

        while(!queue.isEmpty()){
            VertexM<Events> current = queue.remove();
            int currentIndex = vertices.indexOf(current);
            for(int neighborIndex = 0; neighborIndex < SIZE; neighborIndex++){
                //Check if there is at least one edge
                if(!edges[currentIndex][neighborIndex].isEmpty()){
                    VertexM<Events> neighborVertex = vertices.get(neighborIndex);

                    if (neighborVertex.getColor() == Color.WHITE) {
                        neighborVertex.setColor(Color.GRAY);
                        neighborVertex.setDistance(current.getDistance() + 1);
                        neighborVertex.setPredecessor(current);
                        queue.add(neighborVertex);
                    }
                }
            }
            current.setColor(Color.BLACK);
        }
    }

    @Override
    public void dijkstra(Events rootValue) throws GraphException {
        VertexM<Events> startVertex = searchVertexValue(rootValue.getPosition());

        if (startVertex == null) {
            throw new GraphException("The vertex with the specified value was not found.");
        }

        for (VertexM<Events> vertex : vertices) {
            vertex.setPredecessor(null);
            vertex.setDistance(Integer.MAX_VALUE);
        }

        startVertex.setDistance(0);

        PriorityQueue<VertexM<Events>> queue = new PriorityQueue<>();
        queue.add(startVertex);

        while(!queue.isEmpty()){
            VertexM<Events> current = queue.poll();
            int currentIndex = vertices.indexOf(current);

            for(int neighborIndex = 0; neighborIndex < SIZE; neighborIndex++){
                List<EdgeM<Events>> edgeList = edges[currentIndex][neighborIndex];
                //Only do the process where the edge list is not empty
                if(!edgeList.isEmpty()){
                    VertexM<Events> neighborVertex = vertices.get(neighborIndex);
                    //Now I must go through all the edges to use the shortest one (if its multi graph)
                    for(EdgeM<Events> edge : edgeList){
                        int alt = current.getDistance() + edge.getWeight();
                        if(alt < neighborVertex.getDistance()){
                            neighborVertex.setDistance(alt);
                            neighborVertex.setPredecessor(current);

                            //Now re add with the newest distance
                            queue.remove(neighborVertex);
                            queue.add(neighborVertex);
                        }
                    }
                }
            }
        }
    }

    @Override //Doing similar things as list trying to mix bfs and dijkstra
    public void antiDijkstra(Events rootValue) throws GraphException {
        VertexM<Events> startVertex = searchVertexValue(rootValue.getPosition());

        if (startVertex == null) {
            throw new GraphException("The vertex with the specified value was not found.");
        }

        for (VertexM<Events> vertex : vertices) {
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
            vertex.setDistance(Integer.MIN_VALUE);
        }

        startVertex.setDistance(0);
        startVertex.setColor(Color.GRAY);

        PriorityQueue<VertexM<Events>> queue = new PriorityQueue<>(Comparator.comparingInt(VertexM<Events>::getDistance).reversed());
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            VertexM<Events> current = queue.poll();
            int currentIndex = vertices.indexOf(current);

            for (int neighborIndex = 0; neighborIndex < SIZE; neighborIndex++) {
                List<EdgeM<Events>> edgeList = edges[currentIndex][neighborIndex];

                // Only process if there are edges to this neighbor
                if (!edgeList.isEmpty()) {
                    VertexM<Events> neighborVertex = vertices.get(neighborIndex);

                    // Only consider white  vertices
                    if (neighborVertex.getColor() == Color.WHITE) {
                        for (EdgeM<Events> edge : edgeList) {
                            int alt = current.getDistance() + edge.getWeight();

                            if (alt > neighborVertex.getDistance()) {
                                neighborVertex.setDistance(alt);
                                neighborVertex.setPredecessor(current);
                            }
                        }

                        neighborVertex.setColor(Color.GRAY);
                        queue.add(neighborVertex);
                    }
                }
            }

            current.setColor(Color.BLACK);
        }
    }

    @Override
    public boolean hasNeighborInNextRow(int row, int col) throws GraphException {
        VertexM<Events> vertexM = searchVertexValue(new Position(row, col));
        if (vertexM == null) {
            System.out.println("row: " + row + ", col: " + col);
            throw new GraphException("VertexL not found for the specified position.");
        }
        int currentIndex = vertices.indexOf(vertexM);

        for(int neighborIndex = 0; neighborIndex < SIZE; neighborIndex++){
            List<EdgeM<Events>> edgeList = edges[currentIndex][neighborIndex];
            if(!edgeList.isEmpty()){
                for(EdgeM<Events> edge : edgeList){
                    Events neighborEvent = edge.getEndVertex().getValue();
                    if (neighborEvent.getPosition().getRow() == row + 2) {
                        return true; // Encontramos un vecino en la siguiente fila
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void drawShortestPath(GraphicsContext gc, Events rootEvent, Events targetEvent, int tileSize) throws GraphException {
        dijkstra(rootEvent);

        VertexM<Events> targetVertexM = searchVertexValue(targetEvent.getPosition());
        if (targetVertexM == null) {
            throw new GraphException("The target vertex was not found.");
        }

        List<VertexM<Events>> shortestPath = new ArrayList<>();
        VertexM<Events> current = targetVertexM;
        while (current != null) {
            shortestPath.add(current);
            current = current.getPredecessor();
        }

        gc.setLineWidth(3);

        for (int i = shortestPath.size() - 1; i > 0; i--) {
            VertexM<Events> from = shortestPath.get(i);
            VertexM<Events> to = shortestPath.get(i - 1);

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

        VertexM<Events> targetVertexM = searchVertexValue(targetEvent.getPosition());
        if (targetVertexM == null) {
            throw new GraphException("The target vertex was not found.");
        }

        List<VertexM<Events>> longestPath = new ArrayList<>();
        VertexM<Events> current = targetVertexM;
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

    public VertexM<Events> searchVertexValue(Position value) {
        for (VertexM<Events> vertex : vertices) {
            // Ignore RemovedVertex placeholders
            if (vertex instanceof RemovedVertex) {
                continue;
            }
            if (vertex.getValue().getCol() == value.getCol() && vertex.getValue().getRow() == value.getRow()) {
                return vertex;
            }
        }
        return null;
    }
}
