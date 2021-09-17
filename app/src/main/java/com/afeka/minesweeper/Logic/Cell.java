package com.afeka.minesweeper.Logic;

public class Cell {

private CellState state;

    public enum CellState {
        COVERED, UNCOVERED, FLAG, MINE, BOMBED, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT;

        public String toString() {
            switch (this) {
                case COVERED:
                    return "-";
                case UNCOVERED:
                    return " ";
                case FLAG:
                    return "flag";
                case MINE:
                    return "*";
                case BOMBED:
                    return "bombed";
                case ONE:
                    return "1";
                case TWO:
                    return "2";
                case THREE:
                    return "3";
                case FOUR:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                case EIGHT:
                    return "8";

                default:
                    return "  ";
            }
        }
    }


    public CellState numberToState(int num){
        switch(num){
            case 1:
                return CellState.ONE;
            case 2:
                return CellState.TWO;
            case 3:
                return CellState.THREE;
            case 4:
                return CellState.FOUR;
            case 5:
                return CellState.FIVE;
            case 6:
                return CellState.SIX;
            case 7:
                return CellState.SEVEN;
            case 8:
                return CellState.EIGHT;
            default:
                return CellState.UNCOVERED;
        }
    }

    public Cell() { state = CellState.COVERED; }

    public CellState getState() { return state; }

    public String getStateString() { return state.toString(); }

    public void setState(CellState state) { this.state = state; }

}
