package org.example.mapagrafos.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.structures.*;
import org.example.mapagrafos.util.Position;

import java.util.*;

public class MapLogic {
    private int[][] mapMatrix;
    private int rows;
    private int cols;
    private int playerRow;  // Fila del jugador
    private int playerCol;  // Columna del jugador

    private IGraph graph;

    public MapLogic(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.mapMatrix = new int[rows][cols];
        this.playerRow = 24;  // El jugador comienza en la última fila
        this.playerCol = 5;  // El jugador comienza en la columna del medio

        initializeMap();  // Inicializar el mapa con valores
    }
//------------------------MATRIZ METHODS----------------------------------------------------------------

    // Método para inicializar la matriz del mapa con espacios vacíos y valores aleatorios en las posiciones numeradas
    private void initializeMap() {
        Random random = new Random();
        int tileType = 0;

        for (int row = 0; row < rows; row++) {
            boolean hasBlackOrBlue = false;  // Bandera para asegurar una celda negra o azul
            int blackProbability = 40;       // Probabilidad inicial de generar una celda negra (50%)
            int emptyProbability = 60;
            boolean hasNumberedPosition = false;  // Bandera para verificar si hay posiciones numeradas en la fila

            for (int col = 0; col < cols; col++) {
                if (isNumberedPosition(row, col)) {
                    hasNumberedPosition = true;  // Marcar que esta fila tiene una posición numerada

                    // Generar un número aleatorio entre 0 y 99 y compararlo con la probabilidad actual
                    if (random.nextInt(100) < blackProbability) {
                        tileType = 4;  // Asignar negro
                        hasBlackOrBlue = true; // Marcar que ya hay una celda negra o azul en la fila
                        blackProbability -= 15; // Disminuir la probabilidad de otra celda negra en la misma fila
                    } else if (random.nextInt(100) < 50) { // 30% de probabilidad para azul
                        tileType = 2;  // Asignar azul
                        hasBlackOrBlue = true; // Marcar que ya hay una celda negra o azul en la fila
                    } else if (random.nextInt(100) < emptyProbability) {
                        tileType = 0;
                    } else {
                        tileType = random.nextBoolean() ? 1 : 3;
                    }

                    // Asignar valores específicos a ciertas posiciones fijas
                    if ((row == 0 && col == 5) || (row == 24 && col == 5)) {
                        tileType = 11;
                    }
                } else {
                    // Posiciones no numeradas (espacios vacíos)
                    tileType = 0;
                }

                mapMatrix[row][col] = tileType;
            }

            // Asegurar que al menos una celda negra o azul esté presente en las filas que contienen posiciones numeradas
            if (hasNumberedPosition && !hasBlackOrBlue) {
                int randomCol;
                do {
                    randomCol = random.nextInt(cols);
                } while (!isNumberedPosition(row, randomCol));  // Asegurarse de que la posición elegida esté numerada

                // Asignar valores específicos a ciertas posiciones fijas
                if ((row == 0 && randomCol == 5) || (row == 24 && randomCol == 5)) {
                    tileType = 11;
                }else {
                    mapMatrix[row][randomCol] = random.nextBoolean() ? 3 : 2; // Forzar negro o azul
                }
            }
        }
    }



    // Método para verificar si una posición es numerada o un espacio vacío
    boolean isNumberedPosition(int row, int col) {
        // Definir las coordenadas específicas para las posiciones numeradas
        return ((row == 0 && col == 5) ||
                row == 2 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9 )) ||
                (row == 4 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 6 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 8 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 10 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 12 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 14 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 16 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 18 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 20 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 22 && (col == 1 || col == 3 || col == 5 || col == 7 || col == 9)) ||
                (row == 24 && col == 5);
    }

    // Obtener el valor de una celda en una posición específica
    public int getTile(int row, int col) {
        return mapMatrix[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    //----------------------------GRAPH METHODS-------------------------------------------------------

    public void buildGraphList(List<Events> eventsList) throws GraphException {

        this.graph = new MatrixGraph(60, true, true, false);

        // Crear conexiones entre vecinos
        for (int i = 0; i<eventsList.size();i++){
            Events event = eventsList.get(i);
            graph.add(event);
        }

        for(int i = 0; i<eventsList.size();i++){
            Events event = eventsList.get(i);
            Position currentPosition = event.getPosition();
            int row = currentPosition.getRow();

            List<Events>  neighborEvents= searchEventsByRow(eventsList,row+2);

            for (int e = 0; e < neighborEvents.size(); e++) {
                Events neighbor = neighborEvents.get(e);

                graph.addEdge(event, neighbor, event.getDificulty()); // Peso por defecto: 1
            }

        }
    }

    public List<Events> searchEventsByRow(List<Events> eventsList, int row) {
        List<Events> eventsListRow = new ArrayList<>();
        for (int i = 0; i < eventsList.size(); i++) {
            if (eventsList.get(i).getPosition().getRow() == row) {
                eventsListRow.add(eventsList.get(i)); // Agregar el evento a la nueva lista
            }
        }
        return eventsListRow;
    }

    public List<String> getNeighbors(int row, int col) throws GraphException {
        // Buscar el vértice correspondiente a la posición
        VertexM<Events> vertex = graph.searchVertexValue(new Position(row, col));
        if (vertex == null) {
            throw new GraphException("VertexL not found for the specified position.");
        }

        List<String> neighbors = new ArrayList<>();

        // Recopilar los vecinos del vértice a partir de sus aristas
        if(graph.getClass() == ListGraph.class){
                VertexL<Events> vertexL = (VertexL<Events>) vertex;
                for (Edge<Events> edge : vertexL.getEdges()) {
                    // Obtener el valor del vecino (evento)
                    Events neighborEvent = edge.getEndVertex().getValue();

                    // Convertir la posición del vecino en formato de cadena (row,col)
                    String neighborPosition = neighborEvent.getPosition().getRow() + "," + neighborEvent.getPosition().getCol();
                    neighbors.add(neighborPosition);  // Añadir la posición como string
                }

        } else { //Matrix Graph

        }
        return neighbors;
    }

    public IGraph getGraphList(){
        if(!(graph ==null)) {
            return graph;
        }
        return null;
    }

    //----------------------------------PLAYER METHODS-------------------------------------------------

    // Método para actualizar la posición del jugador
    public void setPlayerPosition(int row, int col) {
        // Verificar si la posición es válida
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            this.playerRow = row;
            this.playerCol = col;
            System.out.println("The player moved to the queue: " + playerRow + ", column: " + playerCol);

        } else {
            System.out.println("Posición no válida");
        }
    }

    public void paintPlayer(int tileSize, GraphicsContext gc) {
        double x = playerCol * tileSize;
        double y = playerRow * tileSize;

        Image player = new Image(getClass().getResourceAsStream("/iconos/IlustraciónJugadorCara.png"));

        gc.drawImage(player, x, y, tileSize , tileSize );
    }

    // Métodos para obtener la posición del jugador
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }


}
