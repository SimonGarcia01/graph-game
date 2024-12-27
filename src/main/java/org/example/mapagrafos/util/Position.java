package org.example.mapagrafos.util;

public class Position {
    private int col;
    private int row;

    public Position(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {

        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {

        this.row = row;
    }
}
