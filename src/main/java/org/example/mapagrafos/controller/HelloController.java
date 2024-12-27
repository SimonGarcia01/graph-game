package org.example.mapagrafos.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.mapagrafos.model.MapLogic;
import org.example.mapagrafos.exceptions.GraphException;
import org.example.mapagrafos.model.ButtonGenerator;
import org.example.mapagrafos.model.Events;
import org.example.mapagrafos.util.GifBattle;
import org.example.mapagrafos.util.GifCampamento;
import org.example.mapagrafos.util.GifEquipamiento;
import org.example.mapagrafos.util.GifSacrifico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HelloController {
    @FXML
    private Canvas canvas;  // Referencia al Canvas definido en el archivo FXML

    @FXML
    private Canvas canvasPlayer;

    private GraphicsContext gcPlayer;

    private int PlayerLife;
    private int PlayerScore;
    private int PlayerLuck;

    private boolean easyRoad;

    @FXML
    private VBox buttonContainer;  // Contenedor donde se agregarán los botones dinámicamente

    @FXML
    private ScrollPane rowButtons;  // El contenedor donde se muestran los botones de la fila

    private MapLogic mapLogic;  // Instancia de MapLogic para manejar la lógica del mapa
    private ButtonGenerator buttonGenerator;  // Instancia de ButtonGenerator para generar los botones
    private List <Events> eventsList;

    private Image fondoMapa;
    private Image batalla;
    private Image campamento;
    private Image sacrificio;
    private Image equipamiento;

    private GifBattle gifBattleScene;
    private GifCampamento gifCampingScene;
    private GifSacrifico gifSacrificeScene;
    private GifEquipamiento gifEquipment;

    private GraphicsContext gc;

    private Scanner lector;

    @FXML
    public void initialize() throws GraphException {

        this.lector=new Scanner(System.in);
        this.PlayerLife = 100;
        this.PlayerScore = 0;
        this.PlayerLuck = 50;
        this.easyRoad=false;

        this.gifSacrificeScene = new GifSacrifico();
        this.gifEquipment = new GifEquipamiento();
        this.gifCampingScene = new GifCampamento();
        this.gifBattleScene = new GifBattle();

        this.eventsList = new ArrayList<>();

        this.gcPlayer = canvasPlayer.getGraphicsContext2D();

        //aqui defines que el mapa tendra 10 filas y 10 columnas
        mapLogic = new MapLogic(25, 18);  // Crear una matriz de 10x10

        //talvez quitar, aun nose
        // Construir el grafo después de inicializar el mapa
        //mapLogic.buildGraph();

        // Inicializar la lógica de búsqueda de caminos (BFS)

        // Inicializar el generador de botones
        buttonGenerator = new ButtonGenerator();

        fondoMapa = new Image(getClass().getResourceAsStream("/fondoJuego/IlustraciónFondoJuegoGrafos.png"));
        batalla = new Image(getClass().getResourceAsStream("/iconos/IlustraciónIconosInscription.png"));
        campamento = new Image(getClass().getResourceAsStream("/iconos/IlustraciónCampamento.png"));
        equipamiento = new Image(getClass().getResourceAsStream("/iconos/IlustraciónIconosEquipamiento.png"));
        sacrificio = new Image(getClass().getResourceAsStream("/iconos/IlustraciónSacrificio.png"));
        drawMap();

        // Generar los botones para la última fila (por ejemplo, fila 18)
        generateRowButtons(24);


    }

    private void drawMap() throws GraphException {
        this.gc = canvas.getGraphicsContext2D();
        gc.drawImage(fondoMapa, 150, 140, fondoMapa.getWidth(), fondoMapa.getHeight() + 150,
                0, 0, canvas.getWidth() - 50, canvas.getHeight() + 120);

        int tileSize = 32;  // Tamaño de cada celda

        // Recorremos primera vez para dibujar lineas
        for (int row = 0; row < mapLogic.getRows(); row++) {
            for (int col = 0; col < mapLogic.getCols(); col++) {
                double x = col * tileSize;
                double y = row * tileSize;

                // Luego, dibujar las conexiones
                drawConnections(gc, row, col, x, y, tileSize);
            }
        }

        // Recorremos segunda vez para dibujar puntos de interes
        for (int row = 0; row < mapLogic.getRows(); row++) {
            for (int col = 0; col < mapLogic.getCols(); col++) {
                double x = col * tileSize;
                double y = row * tileSize;

                int tileType = mapLogic.getTile(row, col);
                if (tileType != 0) {
                    switch (tileType) {
                        case 1 -> {
                            gc.drawImage(campamento, x, y, tileSize + 20, tileSize + 20);//evento campamento-> dificultad =2
                            eventsList.add(new Events(canvas,row,col,"camping",2));
                        }
                        case 2 -> {
                            gc.drawImage(equipamiento, x, y, tileSize + 20, tileSize + 20);//evento equipamiento-> dificultad=1
                            eventsList.add(new Events(canvas,row,col,"equipment",0));
                        }
                        case 3 -> {
                            gc.drawImage(sacrificio, x, y, tileSize + 20, tileSize + 20);//evento sacrificio-> dificultad = 3
                            eventsList.add(new Events(canvas,row,col,"sacrifice",3));
                        }
                        case 4 -> {
                            gc.drawImage(batalla, x, y, tileSize + 20, tileSize + 20);//evento batalla -> dificultad = 4
                            eventsList.add(new Events(canvas,row,col,"battle",4));
                        }
                        case 11 -> {
                            gc.setFill(Color.GREEN);
                            gc.fillRect(x, y, tileSize, tileSize);
                            eventsList.add(new Events(canvas,row,col,"start/end",0));
                        }
                        default -> {
                            gc.setFill(Color.BLACK);
                            gc.fillRect(x, y, tileSize, tileSize);
                        }
                    }
                }

            }
        }

        mapLogic.buildGraphList(eventsList);
    }

    private void drawConnections(GraphicsContext gc, int row, int col, double x, double y, int tileSize) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Vecinos vertical y horizontal
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Vecinos diagonales
        };

        for (int[] direction : directions) {
            int neighborRow = row + direction[0];
            int neighborCol = col + direction[1];

            // Verificar que el vecino está dentro de los límites y es una casilla válida
            if (neighborRow >= 0 && neighborRow < mapLogic.getRows() &&
                    neighborCol >= 0 && neighborCol < mapLogic.getCols() &&
                    mapLogic.getTile(neighborRow, neighborCol) != 0) {

                // Calcular las coordenadas del vecino
                double neighborX = neighborCol * tileSize + tileSize / 2.0;
                double neighborY = neighborRow * tileSize + tileSize / 2.0;

                // Dibujar una línea desde el centro de la casilla actual al centro del vecino
                gc.setStroke(Color.RED);
                gc.setLineWidth(2);
                gc.strokeLine(x + tileSize / 2.0, y + tileSize / 2.0, neighborX, neighborY);
            }
        }
    }

    // Método para generar los botones en la fila actual
    private void generateRowButtons(int currentRow) {
        List<Button> buttons = buttonGenerator.generateButtonsForRow(currentRow, mapLogic, mapLogic.getGraphList(),eventsList);
        buttonContainer.getChildren().clear();  // Limpiar los botones anteriores

        // Iterar sobre los botones generados y agregarles el manejador de eventos
        for (int i = 0; i < buttons.size(); i ++) {  // Nota el incremento de 2
            Button button = buttons.get(i);
            int finalCol = buttonGenerator.getListOfColumnsOfButtons().get(i);;  // Capturar la columna para el evento


            button.setOnAction(event -> {
                try {
                    onButtonClicked(currentRow, finalCol);
                } catch (GraphException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        buttonContainer.getChildren().addAll(buttons);  // Agregar los nuevos botones


    }

    public Events findEvents(int row, int col) {
        Events wantedEvent = null;
        for (Events event : eventsList) {
            if (event.getRow() == row && event.getCol() == col) {
                wantedEvent = event;
                System.out.println("Found matching event at row: " + row + ", col: " + col);
                break;
            }
        }

        if (wantedEvent == null) {
            System.out.println("No matching event found for row: " + row + ", col: " + col);
        }
        return wantedEvent;
    }

    public void displayOfEvent(int row, int col){
        // Crear el Stage para la ventana emergente
        Stage popupStage = new Stage();

        Events actualEvent = findEvents(row,col);

        String typeEvent = actualEvent.getType();

        Image gifImage = null;
        ImageView imageView = null;

        // Crear dos botones
        Button button1 = null;
        Button button2 = null;

        switch (typeEvent) {
            case "camping":
                // Crear el ImageView para el GIF
                 gifImage = gifCampingScene.getImage();
                 imageView = new ImageView(gifImage);

                // Crear dos botones
                 button1 = new Button("try fire?");
                 button2 = new Button("try pass");

                break;
            case "equipment":
                // Crear el ImageView para el GIF
                gifImage = gifEquipment.getImage();
                imageView = new ImageView(gifImage);

                button1 = new Button("trust and try to exchange?");
                button2 = new Button("try pass");

                break;
            case "sacrifice":
                // Crear el ImageView para el GIF
                gifImage = gifSacrificeScene.getImage();
                imageView = new ImageView(gifImage);

                button1 = new Button("try to obtain life?");
                button2 = new Button("try pass");

                break;
            case "battle":
                // Crear el ImageView para el GIF
                gifImage = gifBattleScene.getImage();
                imageView = new ImageView(gifImage);

                button1 = new Button("try fight");
                button2 = new Button("try flee");

                break;
        }


        // Crear la acción para cerrar la ventana cuando se presione cualquiera de los botones
        button1.setOnAction(e -> {
            EventLuckAction(typeEvent);
            System.out.println("----------------------");
            System.out.println("actual life : "+PlayerLife);
            System.out.println("actual luck : "+PlayerLuck);
            System.out.println("----------------------");
            popupStage.close();
            if(PlayerLife<=0){
                System.out.println("YOU DIED");
                System.out.println("PLAYER SCORE : "+PlayerScore);
                Platform.exit();
            }

        });

        button2.setOnAction(e ->{
            EventLuckFlee(typeEvent);
            System.out.println("----------------------");
            System.out.println("actual life : "+PlayerLife);
            System.out.println("actual luck : "+PlayerLuck);
            System.out.println("----------------------");
            popupStage.close();
            if(PlayerLife<=0){
                System.out.println("YOU DIED");
                System.out.println("PLAYER SCORE : "+PlayerScore);
                Platform.exit();
            }

        });

        // Crear el layout de la ventana emergente
        StackPane popupRoot = new StackPane();
        popupRoot.getChildren().addAll(imageView, button1, button2);

        // Disponer los botones en la parte inferior
        StackPane.setAlignment(button1, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button2, javafx.geometry.Pos.BOTTOM_RIGHT);

        // Crear la escena de la ventana emergente
        Scene popupScene = new Scene(popupRoot, 600, 550);

        // Configurar la ventana emergente
        popupStage.setTitle("Ventana Emergente con GIF");
        popupStage.setScene(popupScene);

        // Mostrar la ventana emergente
        popupStage.show();

    }

    public void EventLuckAction(String typeEvent) {
        Random random = new Random();
        switch (typeEvent) {
            case "camping":
                if ((random.nextInt(60))*2 < PlayerLuck) {
                    this.PlayerLife=PlayerLife+50;
                    this.PlayerScore=PlayerScore+15;
                    System.out.println("you let the power of the flame surrounds you...you obtain 50 points of life");
                }else{
                    this.PlayerLife=PlayerLife-30;
                    this.PlayerScore=PlayerScore+5;
                    System.out.println("you let the power of the flame surrounds you...you burn and loose 30 points of life");
                }
                break;
            case "equipment":
                if ((random.nextInt(45)) < PlayerLuck) {
                    this.PlayerLuck=PlayerLuck+20;
                    this.PlayerScore=PlayerScore+5;
                    System.out.println("you accept the trader offering and let her see your things...she gives you a useful tool and you obtain 20 points of luck");
                }else{
                    this.PlayerLuck=PlayerLuck-10;
                    System.out.println("you accept the trader offering and let her see your things...When he gets close he blinds you with a powder and steals one of your things, you loose 10 points of luck");
                }

                break;
            case "sacrifice":
                if ((random.nextInt(80))*3 < PlayerLuck) {
                    this.PlayerLife=PlayerLife*2;
                    this.PlayerScore=PlayerScore+100;
                    System.out.println("You accept the price and let a force beyond the dark take you...your life is double ");
                }else{
                    this.PlayerLife=PlayerLife-75;
                    this.PlayerScore=PlayerScore+10;
                    System.out.println("You accept the price and let a force beyond the dark take you...you loose 75 points of life ");
                }

                break;
            case "battle":
                if ((random.nextInt(75))*4 < PlayerLuck) {
                    this.PlayerLife=PlayerLife+10;
                    this.PlayerLuck=PlayerLuck+30;
                    this.PlayerScore=PlayerScore+50;
                    System.out.println("You give everything in the surge of battle and the beating of blood...you win and obtain 10 points of life and 30 of luck");
                }else{
                    this.PlayerLife=PlayerLife-50;
                    this.PlayerLuck=PlayerLuck-5;
                    this.PlayerScore=PlayerScore+5;
                    System.out.println("You give everything in the surge of battle and the beating of blood...you lose the fight and the enemy does not hesitate to take away 50 points of life and 5 of luck");
                }

                break;

        }
    }

    public void EventLuckFlee(String typeEvent) {
        Random random = new Random();
        switch (typeEvent) {
            case "camping":
                if ((random.nextInt(10))*2 < PlayerLuck) {
                    this.PlayerLife=PlayerLife-10;
                    this.PlayerScore=PlayerScore-5;
                }
                break;
            case "equipment":
                if ((random.nextInt(50)) < PlayerLuck) {
                    this.PlayerLuck=PlayerLuck-5;
                    this.PlayerScore=PlayerScore-15;
                }
                break;
            case "sacrifice":
                if ((random.nextInt(10)) < PlayerLuck) {
                    this.PlayerLife=PlayerLife-10;
                    this.PlayerScore=PlayerScore-30;
                }
                break;
            case "battle":
                if ((random.nextInt(10))*2 < PlayerLuck) {
                    this.PlayerLife=PlayerLife-15;
                    this.PlayerLuck=PlayerLuck-5;
                    this.PlayerScore=PlayerScore-30;
                }
                break;
        }
    }

    @FXML
    private void onButtonClicked(int row, int col) throws GraphException {

        gcPlayer.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

       if(row!=24 && col!=5) {
           buttonGenerator.handleButtonClick(row, col, mapLogic.getGraphList());
        }

        // Mover al jugador a la nueva posición (fila, columna)
        mapLogic.setPlayerPosition(row, col);  // Actualiza la posición del jugador

        mapLogic.paintPlayer(32,gcPlayer);

        if(row==24 && col==5){
            System.out.println("enter in inscription forest");
        }else if (col==0) {
            displayOfEvent(24,col+1);
        }else {
            displayOfEvent(row, col);
        }

        if(!easyRoad) {
            System.out.println("Enter 1 to visualize the easiest path.\nEnter 2 to visualize the hardest path\nEnter 3 to show neither.");
            int decision = lector.nextInt();

            if (decision == 1) {
                gc.setStroke(Color.BLUE);
                mapLogic.getGraphList().drawShortestPath(gc, eventsList.get(0), findEvents(24, 5), 32);
                this.easyRoad=true;
            }else if(decision == 2){
                gc.setStroke(Color.CRIMSON);
                mapLogic.getGraphList().drawLongestPath(gc, eventsList.get(0), findEvents(24, 5), 32);
                this.easyRoad=true;
            } else {
                this.easyRoad=true;
            }
        }



        // Avanzar a la siguiente fila (si es posible)
        int nextRow = row - 2;  // Mover al siguiente nivel

        if (nextRow > 0) {
            // Si la siguiente fila existe, genera los botones para la nueva fila
            generateRowButtons(nextRow);
        } else {
            // Si ya no hay más filas, puedes hacer alguna acción de fin del juego o mensaje
            System.out.println("¡Congratulations! You have escaped from the ancient inscryption forest.");
            System.out.println("PLAYER SCORE : "+PlayerScore);
            Platform.exit();

        }
    }


}