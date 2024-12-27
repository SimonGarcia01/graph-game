package org.example.mapagrafos.structures;

import javafx.scene.canvas.GraphicsContext;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.util.Position;

public interface IGraph{
    public abstract void add(Events value);
    public abstract void addEdge(Events startValue, Events endValue, int weight) throws GraphException;
    public abstract void bFS(Events rootValue) throws GraphException;
    public abstract void dijkstra(Events rootValue) throws GraphException;
    public abstract void antiDijkstra(Events rootValue) throws GraphException;
    public abstract boolean hasNeighborInNextRow(int row, int col) throws GraphException;
    public abstract void drawShortestPath(GraphicsContext gc, Events rootEvent, Events targetEvent, int tileSize) throws GraphException;
    public abstract VertexM<Events> searchVertexValue(Position value);
    public void drawLongestPath(GraphicsContext gc, Events rootEvent, Events targetEvent, int tileSize) throws GraphException;
}
