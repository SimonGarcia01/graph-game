package structures;
import javafx.scene.canvas.Canvas;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.structures.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListGraphTest {
    private IGraph graph;

    public void defaultSetup(){
        this.graph = new ListGraph(true,false,false);
    }

    @Test
    public void addVertexTest1() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,9,"number",4);
        graph.add(event1);
        graph.add(event2);
        Assert.assertEquals(9,event2.getCol());
        
    }
    @Test
    public void addVertexTest2() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,7,7,"number",4);
        graph.add(event1);
        graph.add(event2);
        //assert
        Assert.assertEquals(7,event2.getCol());

    }
    @Test
    public void addVertexTest3() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,15,15,"number",4);
        graph.add(event1);
        Assert.assertEquals(15,event1.getCol());
    }
    @Test
    public void addEdgeTest1() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        graph.add(event1);
        graph.add(event2);
        try{
            graph.addEdge(event1,event2,35);
        } catch (GraphException e){
            e.printStackTrace();
        }
    }
    @Test
    public void addEdgeLoop() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        graph.add(event1);
        //assert
        String message="";
        try{
            graph.addEdge(event1,event1,10);
        } catch (GraphException e){
            message=e.getMessage();
        }
        Assert.assertEquals("Loops are not allowed.",message);
    }
    @Test
    public void addEdgeUnExistsVertex() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,0,0,null,0);
        graph.add(event1);
        //assert
        String message="";
        try{
            graph.addEdge(event1,event2,34);
        } catch (GraphException e){
            message=e.getMessage();
        }
        Assert.assertEquals("One or both vertices do not exist in the graph.",message);
    }
    @Test
    public void bfsTest() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        graph.add(event1);
        graph.add(event2);
        graph.add(event3);

        //Assert
        VertexM<Events> vertex=graph.searchVertexValue(event2.getPosition());
        String message = "";
        try{
            graph.addEdge(event1,event2,10);
            graph.addEdge(event1,event3,15);
            graph.addEdge(event2,event3,17);
            graph.bFS(event1);
        } catch (GraphException e){
            message = e.getMessage();
        }

        assertEquals(Color.BLACK,vertex.getColor());
    }
    @Test
    public void bfsTestNotExistsEvent() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        graph.add(event1);
        graph.add(event2);
        //assert
        String message = "";
        try {
            graph.addEdge(event1,event2,10);
            graph.addEdge(event1,event3,15);
            graph.addEdge(event2,event3,17);
            graph.bFS(event3);
        } catch (GraphException e){
            message = e.getMessage();
        }
        Assert.assertEquals("One or both vertices do not exist in the graph.", message);

    }
    @Test
    public void bfsTestWithEdge() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1 = new Events(canvas, 7, 7, "number", 4);
        Events event2 = new Events(canvas, 8, 8, "number", 4);
        graph.add(event1);
        graph.add(event2);
        VertexM<Events> vertexL=graph.searchVertexValue(event2.getPosition());
        //assert
        try {
            graph.bFS(event1);
        }catch (GraphException e){
            e.printStackTrace();
        }
        Assert.assertEquals(Color.WHITE,vertexL.getColor());
    }
    @Test
    public void dijkstraTest() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        Events event4= new Events(canvas,10,10,"number",4);
        graph.add(event1);
        graph.add(event2);
        graph.add(event3);
        graph.add(event4);
        //assert

        VertexM<Events> vertexEvent2=graph.searchVertexValue(event2.getPosition());
        try {
            graph.addEdge(event1,event2,10);
            graph.addEdge(event2,event3,17);
            graph.addEdge(event3,event4,20);
            graph.addEdge(event4,event2,5);
            graph.dijkstra(event1);
        }catch (GraphException e){
            e.printStackTrace();
        }
        Assert.assertEquals(10,vertexEvent2.getDistance());

    }
    @Test
    public void dijkstraNullTest() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        Events event4= new Events(canvas,10,10,"number",4);
        graph.add(event1);
        graph.add(event2);
        graph.add(event3);
        graph.add(event4);
        //assert
        String message = "";
        try {
            graph.dijkstra(null);
        }catch (GraphException e){
            message = e.getMessage();
        }
        Assert.assertEquals("A null event was searched.", message);

    }
    @Test
    public void dijkstraPredecessorTest() {
        //init
        defaultSetup();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        graph.add(event1);
        graph.add(event2);
        //assert
        int predecessor=0;
        VertexM<Events> vertexEvent2=graph.searchVertexValue(event2.getPosition());
        VertexM<Events> vertexEvent1=graph.searchVertexValue(event1.getPosition());
        try {
            graph.addEdge(event1,event2,10);
            graph.dijkstra(event1);
        }catch (GraphException e){
            e.printStackTrace();
        }
        if(vertexEvent2.getPredecessor().compareTo(vertexEvent1)==0){
            predecessor=1;
        }
        Assert.assertEquals(1,predecessor);

        
    }

}
