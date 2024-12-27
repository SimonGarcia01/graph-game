package structures;
import javafx.scene.canvas.Canvas;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.model.MapLogic;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.structures.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ModelMatrixGraphTest {
    private List<Events> eventsList;
    private MapLogic mapLogic;
    public void defaultTest(){
        eventsList=new ArrayList<>();
        mapLogic=new MapLogic(4,4);
    }
    @Test
    public void addVertexTest1(){
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,9,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        try {
            mapLogic.buildGraphList(eventsList);
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
        VertexM<Events> found=mapLogic.getGraphList().searchVertexValue(event1.getPosition());
        int col=found.getValue().getCol();
        //assert
        Assert.assertEquals(7,col);
    }
    @Test
    public void addVertexTest2()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,9,9,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        try {
            mapLogic.buildGraphList(eventsList);
        } catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> foundEvent2=mapLogic.getGraphList().searchVertexValue(event2.getPosition());
        int foundCol=foundEvent2.getValue().getCol();
        //assert
        Assert.assertEquals(9,foundCol);
    }
    @Test
    public void addVertexTest3()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,15,15,"number",4);
        eventsList.add(event1);
        try {
            mapLogic.buildGraphList(eventsList);
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
        VertexM<Events> foundEvent1=mapLogic.getGraphList().searchVertexValue(event1.getPosition());
        int foundCol=foundEvent1.getValue().getCol();
        //assert
        Assert.assertEquals(15,foundCol);
    }
    @Test
    public void addEdgeTest1() {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        //assert
        int edge=0;
        try {
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,20);
        }catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> found=mapLogic.getGraphList().searchVertexValue(event1.getPosition());
        VertexM<Events> found2=mapLogic.getGraphList().searchVertexValue(event2.getPosition());
        if(!found2.getValue().equals(found.getValue())){
            edge=1;
        }
        Assert.assertEquals(1,edge);



    }
    @Test
    public void addEdgeLoop() {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        String message="";
        //act
        try {
            Events event1= new Events(canvas,7,7,"number",4);
            eventsList.add(event1);
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event1,22);
        }catch (GraphException e) {
            message=e.getMessage();
        }
        //assert
        Assert.assertEquals("Loops are not allowed.",message);

    }
    @Test
    public void addEdgeUnExistsVertex() {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        String message="";
        //act
        try {
            Events event1= new Events(canvas,7,7,"number",4);
            Events event2= new Events(canvas,0,0,null,0);
            eventsList.add(event1);
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,22);
        }catch (GraphException e) {
            message=e.getMessage();
        }
        //assert
        Assert.assertEquals("One or both vertices do not exist in the graph.",message);

    }
    @Test
    public void bfsTest()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);
        try {
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,10);
            mapLogic.getGraphList().bFS(event1);
        }catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> foundEvent1 =  mapLogic.getGraphList().searchVertexValue(event1.getPosition());
        //assert
        Assert.assertEquals(Color.BLACK,foundEvent1.getColor());
    }
    @Test
    public void bfsTestNotExistsEvent() {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        String message="";
        //act
        try {
            Events event1= new Events(canvas,7,7,"number",4);
            Events event2= new Events(canvas,8,8,"number",4);
            Events event3= new Events(canvas,0,0,null,0);
            eventsList.add(event1);
            eventsList.add(event2);
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,10);
            mapLogic.getGraphList().bFS(event3);
        }catch (GraphException e) {
            message=e.getMessage();
        }
        Assert.assertEquals("The vertex with the specified value was not found.",message);

    }
    @Test
    public void bfsTestWithoutEdge()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        try {
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().bFS(event1);
        }catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> foundEvent2 = mapLogic.getGraphList().searchVertexValue(event2.getPosition());
        //assert
        Assert.assertEquals(Color.WHITE,foundEvent2.getColor());
    }
    @Test
    public void dijkstraTest()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        Events event3= new Events(canvas,9,9,"number",4);
        Events event4= new Events(canvas,10,10,"number",4);
        try {
            eventsList.add(event1);
            eventsList.add(event2);
            eventsList.add(event3);
            eventsList.add(event4);
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,10);
            mapLogic.getGraphList().addEdge(event2,event3,15);
            mapLogic.getGraphList().addEdge(event3,event4,20);
            mapLogic.getGraphList().dijkstra(event1);
        }catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> foundEvent3 =  mapLogic.getGraphList().searchVertexValue(event3.getPosition());
        VertexM<Events> vertexPredecessor =foundEvent3.getPredecessor();
        //assert
        int searchPredecessor=0;
        if(vertexPredecessor.compareTo(foundEvent3.getPredecessor())==0){
            searchPredecessor=1;
        }
        Assert.assertEquals(1,searchPredecessor);
    }
    @Test
    public void dijkstraNullTest() {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        String message="";
        //act
        try {
            Events event1= new Events(canvas,7,7,"number",4);
            Events event2= new Events(canvas,8,8,"number",4);
            Events event3= new Events(canvas,9,9,"number",4);
            Events event4= new Events(canvas,10,10,"number",4);
            eventsList.add(event1);
            eventsList.add(event2);
            eventsList.add(event3);
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().addEdge(event1,event2,10);
            mapLogic.getGraphList().addEdge(event2,event3,15);
            mapLogic.getGraphList().dijkstra(event4);
        }catch (GraphException e) {
            message=e.getMessage();
        }
        //assert
        Assert.assertEquals("The vertex with the specified value was not found.",message);


    }
    @Test
    public void dijkstraPredecessorTest()  {
        //init
        defaultTest();
        Canvas canvas = new Canvas();
        //act
        Events event1= new Events(canvas,7,7,"number",4);
        Events event2= new Events(canvas,8,8,"number",4);
        eventsList.add(event1);
        eventsList.add(event2);
        try {
            mapLogic.buildGraphList(eventsList);
            mapLogic.getGraphList().dijkstra(event1);
        }catch (GraphException e) {
            e.printStackTrace();
        }
        VertexM<Events> foundEvent1 = mapLogic.getGraphList().searchVertexValue(event1.getPosition());
        //assert
        Assert.assertNull(foundEvent1.getPredecessor());

    }

}

