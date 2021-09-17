package com.afeka.minesweeper.Logic;

public class Board {

    private Cell cells[];
    private int boardSize;

    public Board(int size) {
        this.boardSize = size*size;
        this.cells = new Cell[boardSize];
        initialize();
    }

    public Cell getCell(int position) {
        return cells[position];
    }

    public void initialize()
    {
        // Assign all the cells as mine-free
        for (int i=0; i<boardSize; i++)
        {
                cells[i] = new Cell();
                cells[i].setState(Cell.CellState.COVERED);
        }
    }

    public int[] getLocation(int position, int gameSize){
        int[] location = new int[2];
        location[0] = position/gameSize;
        location[1] = position%gameSize;
        return location;
    }

    public int getPosition(int row , int col,int gameSize){
        return row*gameSize+col;
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
}
