package org.example.mapagrafos.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.example.mapagrafos.util.Position;

public class Events {
    Position position;
    private int frame;
    private String Type;
    private int dificulty;
    private boolean recorrido;
    private int row;
    private int col;

    private Canvas canvas;
    private GraphicsContext gc;

    public Events(Canvas canvas, int row, int col, String Type, int dificulty) {
        this.canvas = canvas;
        position = new Position(row, col);
        this.row = row;
        this.col = col;
        this.Type = Type;
        this.dificulty = dificulty;
        this.recorrido = false;

    }

    public boolean getRecorrido() {
        return recorrido;
    }
    public void setRecorrido(boolean recorrido) {
        this.recorrido = recorrido;
    }
    public int getRow() {
        return position.getRow();
    }
    public int getCol() {
        return position.getCol();
    }
    public Position getPosition(){
        return position;
    }
    public String getType() {
        return Type;
    }
    public int getDificulty() {
        return dificulty;
    }

}
