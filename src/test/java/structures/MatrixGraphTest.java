package structures;

import javafx.scene.canvas.Canvas;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.structures.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixGraphTest {
    private IGraph graph;

    public void defaultSetup() throws GraphException {
        try {
            this.graph = new MatrixGraph(50, true, false, false);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addVertexTest1() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 9, "number", 4);
            graph.add(event1);
            graph.add(event2);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addVertexTest2() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 7, 7, "number", 4);
            graph.add(event1);
            graph.add(event2);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addVertexTest3() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 15, 15, "number", 4);
            graph.add(event1);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addEdgeTest1() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            graph.add(event1);
            graph.add(event2);
            try {
                graph.addEdge(event1, event2, 35);
            } catch (GraphException e) {
                e.printStackTrace();
            }
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addEdgeLoop() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            graph.add(event1);
            //assert
            String message = "";
            try {
                graph.addEdge(event1, event1, 10);
            } catch (GraphException e) {
                message = e.getMessage();
            }
            Assert.assertEquals("Loops are not allowed.", message);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addEdgeUnExistsVertex() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 0, 0, null, 0);
            graph.add(event1);
            //assert
            String message = "";
            try {
                graph.addEdge(event1, event2, 34);
            } catch (GraphException e) {
                message = e.getMessage();
            }
            Assert.assertEquals("One or both vertices do not exist in the graph.", message);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bfsTest() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            Events event3 = new Events(canvas, 9, 9, "number", 4);
            graph.add(event1);
            graph.add(event2);
            graph.add(event3);

            //Assert
            VertexM<Events> vertex = graph.searchVertexValue(event2.getPosition());
            String message = "";
            try {
                graph.addEdge(event1, event2, 10);
                graph.addEdge(event1, event3, 15);
                graph.addEdge(event2, event3, 17);
                graph.bFS(event1);
            } catch (GraphException e) {
                message = e.getMessage();
            }

            assertEquals(Color.BLACK, vertex.getColor());
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bfsTestNotExistsEvent() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            Events event3 = new Events(canvas, 9, 9, "number", 4);
            graph.add(event1);
            graph.add(event2);
            //assert
            String message = "";
            try {
                graph.addEdge(event1, event2, 10);
                graph.addEdge(event1, event3, 15);
                graph.addEdge(event2, event3, 17);
                graph.bFS(event3);
            } catch (GraphException e) {
                message = e.getMessage();
            }
            Assert.assertEquals("One or both vertices do not exist in the graph.", message);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bfsTestWithEdge() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            graph.add(event1);
            graph.add(event2);
            VertexM<Events> vertexL = graph.searchVertexValue(event2.getPosition());
            //assert
            try {
                graph.bFS(event1);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(Color.WHITE, vertexL.getColor());
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dijkstraTest() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            Events event3 = new Events(canvas, 9, 9, "number", 4);
            Events event4 = new Events(canvas, 10, 10, "number", 4);
            graph.add(event1);
            graph.add(event2);
            graph.add(event3);
            graph.add(event4);
            //assert

            VertexM<Events> vertexEvent2 = graph.searchVertexValue(event2.getPosition());
            try {
                graph.addEdge(event1, event2, 10);
                graph.addEdge(event2, event3, 17);
                graph.addEdge(event3, event4, 20);
                graph.addEdge(event4, event2, 5);
                graph.dijkstra(event1);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(10, vertexEvent2.getDistance());
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dijkstraNullTest() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            Events event3 = new Events(canvas, 9, 9, "number", 4);
            Events event4 = new Events(canvas, 10, 10, "number", 4);
            graph.add(event1);
            graph.add(event2);
            graph.add(event3);
            //assert
            String message = "";
            try {
                graph.dijkstra(event4);
            } catch (GraphException e) {
                message = e.getMessage();
            }
            Assert.assertEquals("The vertex with the specified value was not found.", message);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dijkstraPredecessorTest() {
        try {
            defaultSetup();
            Canvas canvas = new Canvas();
            //act
            Events event1 = new Events(canvas, 7, 7, "number", 4);
            Events event2 = new Events(canvas, 8, 8, "number", 4);
            graph.add(event1);
            graph.add(event2);
            //assert
            int predecessor = 0;
            VertexM<Events> vertexEvent2 = graph.searchVertexValue(event2.getPosition());
            VertexM<Events> vertexEvent1 = graph.searchVertexValue(event1.getPosition());
            try {
                graph.addEdge(event1, event2, 10);
                graph.dijkstra(event1);
            } catch (GraphException e) {
                e.printStackTrace();
            }
            if (vertexEvent2.getPredecessor().compareTo(vertexEvent1) == 0) {
                predecessor = 1;
            }
            Assert.assertEquals(1, predecessor);
        } catch (GraphException e) {
            e.printStackTrace();
        }
    }
}
