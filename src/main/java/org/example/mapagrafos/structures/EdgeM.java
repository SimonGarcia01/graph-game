package org.example.mapagrafos.structures;

public class EdgeM <V> implements Comparable<EdgeM<V>>{

    private int weight;
    private VertexM<V> startVertex;
    private VertexM<V> endVertex;

    public EdgeM(int weight, VertexM<V> startVertex, VertexM<V> endVertex){
        this.weight = weight;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    

    @Override
    public int compareTo(EdgeM<V> o) {
        return weight - o.getWeight();
    }



    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public VertexM<V> getEndVertex() {
        return endVertex;
    }

    public void setEndVertex(VertexM<V> endVertex) {
        this.endVertex = endVertex;
    }

    public VertexM<V> getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(VertexM<V> startVertex) {
        this.startVertex = startVertex;
    }

    @Override
    public String toString(){
        return String.format("Start: %s - End: %s - Weight: %d", startVertex, endVertex, weight);
    }
    
}
