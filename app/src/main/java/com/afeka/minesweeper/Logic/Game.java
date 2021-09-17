package com.afeka.minesweeper.Logic;

import java.util.Random;

public class Game {
    private Board realBoard,dataBoard;
    private int[][] mines;
    private Difficulty difficulty;
    private boolean gameOver, isWin;
    private int bestTime, countMoves, numFlags;

    public Game(Difficulty diff) {
        difficulty = diff;
        realBoard = new Board(difficulty.gameSize());
        dataBoard = new Board(difficulty.gameSize());
        mines = new int[numbersOfMines(difficulty)][2];
        placeMines();
        gameOver = false;
        bestTime = 0;
        countMoves = 0;
        numFlags = 0;
        isWin = false;
    }

    public int numbersOfMines(Difficulty diff){
        switch (diff){
            case Beginner:
                return 4;
            case Intermediate:
                return 10;
            case Advanced:
                return 20;
            default:
                return 0;
        }
    }

    public enum Difficulty {
        Beginner, Intermediate, Advanced;

        public int gameSize() {
            switch (this) {
                case Beginner:
                    return 4;
                case Intermediate:
                    return 8;
                case Advanced:
                    return 12;
                default:
                   return 0;
            }
        }
    }

    //check if all flags are matching the mines on the board
    public boolean checkFlags() {
        int count = 0;
        for(int i = 0; i < realBoard.getBoardSize(); i++) {
            Cell flag = realBoard.getCell(i);
            Cell mine = dataBoard.getCell(i);
            if(flag.getState() == Cell.CellState.FLAG && mine.getState() == Cell.CellState.MINE) {
                realBoard.getCell(i).setState(Cell.CellState.BOMBED);
                count++;
            }
        }
        if(count == numbersOfMines(difficulty))
            return true;
        else
            return false;
    }

    //check if all board cells are uncovered
    public boolean checkIfAllBoardOver() {
        Cell[] cells = realBoard.getCells();
            for(Cell cell: cells )
                if(cell.getState() == Cell.CellState.COVERED)
                    return false;

        return true;
    }

    public boolean isValid(int row, int col)
    {
        // Returns true if row number and column number
        // is in range
        return (row >= 0) && (row < difficulty.gameSize()) &&
                (col >= 0) && (col < difficulty.gameSize());
    }

    //check if a cell is a mine
    public boolean isMine (int position)
    {
        if (dataBoard.getCell(position).getState() == Cell.CellState.MINE)
            return true;
        else
            return false;
    }

    // A Function to place the mines randomly
    // on the board
    public void placeMines()
    {
        boolean[] mark = new boolean[dataBoard.getBoardSize()];
        Random rand = new Random();
        int gameSize = difficulty.gameSize();

        // Continue until all random mines have been created.
        for (int i=0; i<numbersOfMines(difficulty);)
        {
            int random = rand.nextInt(dataBoard.getBoardSize());
            int x = random / gameSize;
            int y = random % gameSize;

            // Add the mine if no mine is placed at this
            // position on the board
            if (mark[random] == false)
            {
                // Row Index of the Mine
                mines[i][0]= x;
                // Column Index of the Mine
                mines[i][1] = y;

                // Place the mine
                dataBoard.getCell(dataBoard.getPosition(x,y,gameSize)).setState(Cell.CellState.MINE);
                mark[random] = true;
                i++;
            }
        }
    }

    // A Function to play Minesweeper game
    public void playMinesweeper (int position)
    {
        int[] location = realBoard.getLocation(position,difficulty.gameSize());

        //check if the first move hit a mine
        if (countMoves == 0)
        {
          //If the first move itself is a mine, change places with a new location which is not a mine
          if (isMine (position) == true)
              replaceMine (position);
        }
            setCountMoves(getCountMoves()+1);
            gameOver = playMinesweeperUtil(location[0],location[1]);

    }

// A Function to count the number of mines in the adjacent cells
    int countAdjacentMines(int row, int col)
    {
        int count = 0;
        int size = difficulty.gameSize();
        int position = realBoard.getPosition(row,col,size);

        //----------- 1st Neighbour (North) ------------

        // Only process this cell if this is a valid one
        if (isValid (row-1, col) == true)
        {
            if (isMine (position-size) == true)
                count++;
        }

        //----------- 2nd Neighbour (South) ------------

        // Only process this cell if this is a valid one
        if (isValid (row+1, col) == true)
        {
            if (isMine (position+size) == true)
                count++;
        }

        //----------- 3rd Neighbour (East) ------------

        // Only process this cell if this is a valid one
        if (isValid (row, col+1) == true)
        {
            if (isMine (position+1) == true)
                count++;
        }

        //----------- 4th Neighbour (West) ------------

        // Only process this cell if this is a valid one
        if (isValid (row, col-1) == true)
        {
            if (isMine (position-1) == true)
                count++;
        }

        //----------- 5th Neighbour (North-East) ------------

        // Only process this cell if this is a valid one
        if (isValid (row-1, col+1) == true)
        {
            if (isMine (position-size+1) == true)
                count++;
        }

        //----------- 6th Neighbour (North-West) ------------

        // Only process this cell if this is a valid one
        if (isValid (row-1, col-1) == true)
        {
            if (isMine (position-size-1) == true)
                count++;
        }

        //----------- 7th Neighbour (South-East) ------------

        // Only process this cell if this is a valid one
        if (isValid (row+1, col+1) == true)
        {
            if (isMine (position+size+1) == true)
                count++;
        }

        //----------- 8th Neighbour (South-West) ------------

        // Only process this cell if this is a valid one
        if (isValid (row+1, col-1) == true)
        {
            if (isMine (position+size-1) == true)
                count++;
        }
        return count;
    }


    // A Recursive Fucntion to play the Minesweeper Game
    public boolean playMinesweeperUtil(int row, int col)
    {
        int gameSize = difficulty.gameSize();
        int position = realBoard.getPosition(row,col,gameSize);

        // Base Case of Recursion
        if (realBoard.getCell(position).getState() != Cell.CellState.COVERED)
            return false;

        // You opened a mine,
        if (dataBoard.getCell(position).getState() == Cell.CellState.MINE && realBoard.getCell(position).getState() != Cell.CellState.FLAG)
        {
            int cellCalculate = 0;
                for (int i = 0; i < numbersOfMines(difficulty); i++) {
                    cellCalculate = (mines[i][0] * gameSize) + mines[i][1];
                    if (realBoard.getCell(cellCalculate).getState() == Cell.CellState.FLAG)
                        continue;
                    realBoard.getCell((mines[i][0] * gameSize) + mines[i][1]).setState(Cell.CellState.MINE);
                }
            gameOver = true;
            return true;
        }
        else
        {
            // Calculate the number of adjacent mines and put it
            // on the board
            int count = countAdjacentMines(row, col);
            realBoard.getCell(position).setState(realBoard.getCell(position).numberToState(count));

            if (count == 0)
            {
                //----------- 1st Neighbour (North) ------------

                // Only process this cell if this is a valid one
                if (isValid (row-1, col) == true)
                {
                    if (isMine (position-gameSize) == false)
                        playMinesweeperUtil(row-1, col);
                }

                //----------- 2nd Neighbour (South) ------------

                // Only process this cell if this is a valid one
                if (isValid (row+1, col) == true)
                {
                    if (isMine (position+gameSize) == false)
                        playMinesweeperUtil(row+1, col);
                }

                //----------- 3rd Neighbour (East) ------------

                // Only process this cell if this is a valid one
                if (isValid (row, col+1) == true)
                {
                    if (isMine (position+1) == false)
                        playMinesweeperUtil( row, col+1);
                }

                //----------- 4th Neighbour (West) ------------

                // Only process this cell if this is a valid one
                if (isValid (row, col-1) == true)
                {
                    if (isMine (position-1) == false)
                        playMinesweeperUtil(row, col-1);
                }

                //----------- 5th Neighbour (North-East) ------------

                // Only process this cell if this is a valid one
                if (isValid (row-1, col+1) == true)
                {
                    if (isMine (position-gameSize+1) == false)
                        playMinesweeperUtil(row-1, col+1);
                }

                //----------- 6th Neighbour (North-West) ------------

                // Only process this cell if this is a valid one
                if (isValid (row-1, col-1) == true)
                {
                    if (isMine (position-gameSize-1) == false)
                        playMinesweeperUtil( row-1, col-1);
                }

                //----------- 7th Neighbour (South-East) ------------

                // Only process this cell if this is a valid one
                if (isValid (row+1, col+1) == true)
                {
                    if (isMine (position+gameSize+1) == false)
                        playMinesweeperUtil(row+1, col+1);
                }

                //----------- 8th Neighbour (South-West) ------------

                // Only process this cell if this is a valid one
                if (isValid (row+1, col-1) == true)
                {
                    if (isMine (position+gameSize-1) == false)
                        playMinesweeperUtil(row+1, col-1);
                }
            }
            return false;
        }
    }

    //replace the mine with a different location
    void replaceMine (int position)
    {
        boolean found = false;
        int boardSize = dataBoard.getBoardSize();
        for (int i=0; i<boardSize; i++)
        {
            for (int j=0; j<boardSize; j++)
            {
                // Find the first location in the board
                // which is not having a mine and put a mine
                // there.
                if (dataBoard.getCell(dataBoard.getPosition(i,j,boardSize)).getState() != Cell.CellState.MINE)
                {
                    dataBoard.getCell(dataBoard.getPosition(i,j,boardSize)).setState(Cell.CellState.MINE);
                    int[] location = dataBoard.getLocation(position, difficulty.gameSize());
                    for(int k = 0; k < numbersOfMines(difficulty); k++) {
                        if(mines[k][0] == location[0] && mines[k][1] == location[1]) {
                            mines[k][0] = i;
                            mines[k][1] = j;
                            dataBoard.getCell(position).setState(Cell.CellState.COVERED);
                            found = true;
                            break;
                        }
                     }
                    if(found) {
                        break;
                    }
                }
            }
            if(found)
                break;
        }
    }

    public Board getRealBoard() {
        return realBoard;
    }

    public void setRealBoard(Board realBoard) {
        this.realBoard = realBoard;
    }

    public Board getDataBoard() {
        return dataBoard;
    }

    public void setDataBoard(Board dataBoard) {
        this.dataBoard = dataBoard;
    }

    public int[][] getMines() {
        return mines;
    }

    public void setMines(int[][] mines) {
        this.mines = mines;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getBestTime() {
        return bestTime;
    }

    public void setBestTime(int bestTime) { this.bestTime = bestTime; }

    public int getCountMoves() {
        return countMoves;
    }

    public void setCountMoves(int countMoves) {
        this.countMoves = countMoves;
    }

    public int getNumFlags() {
        return numFlags;
    }

    public void setNumFlags(int numFlags) {
        this.numFlags = numFlags;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

}
