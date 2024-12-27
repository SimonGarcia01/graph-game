package org.example.mapagrafos.structures;

public class Edge <V>{

    private int weight;
    private VertexL<V> startVertexL;
    private VertexL<V> endVertexL;

    public Edge(int weight, VertexL<V> startVertexL, VertexL<V> endVertexL){
        this.weight = weight;
        this.startVertexL = startVertexL;
        this.endVertexL = endVertexL;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public VertexL<V> getEndVertex() {
        return endVertexL;
    }

    public void setEndVertex(VertexL<V> endVertexL) {
        this.endVertexL = endVertexL;
    }

    public VertexL<V> getStartVertex() {
        return startVertexL;
    }

    public void setStartVertex(VertexL<V> startVertexL) {
        this.startVertexL = startVertexL;
    }

    @Override
    public String toString(){
        return String.format("Start: %s - End: %s - Weight: %d", startVertexL, endVertexL, weight);
    }

}