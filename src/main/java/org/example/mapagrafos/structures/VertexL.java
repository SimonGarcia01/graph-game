package org.example.mapagrafos.structures;

import java.util.ArrayList;
import java.util.List;

public class VertexL<V> extends VertexM<V> implements Comparable<VertexM<V>>{

    private List<Edge<V>> edges;

    public VertexL(V value){
        super(value);
        this.edges = new ArrayList<>();
    }

    public void addEdge(Edge<V> edge) {
        edges.add(edge);
    }

    public boolean removeEdge(Edge<V> edge) {
        return edges.remove(edge);
    }

    public Edge<V> searchEdge(VertexL<V> endVertexL){

        for(Edge<V> edge : edges){
            if(edge.getEndVertex().equals(endVertexL)){
                return edge;
            }
        }

        return null;
    }

    public List<Edge<V>> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge<V>> edges) {
        this.edges = edges;
    }

}