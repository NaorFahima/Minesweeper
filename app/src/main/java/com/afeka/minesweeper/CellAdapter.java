package com.afeka.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.afeka.minesweeper.Logic.Board;

public class CellAdapter extends BaseAdapter {
    private Board board;
    private Context context;

    private int cachedWidth = -1;
    private int cachedHeight = -1;

    public CellAdapter(Board board, Context context) {
        this.board = board;
        this.context = context;
    }

    @Override
    public int getCount() {
        return board.getBoardSize();
    }

    @Override
    public Object getItem(int position) {
        return board.getCell(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position ----> return View


        CellView cellView = new CellView(context);

        GridView gridView = (GridView)parent;

        if(cachedHeight <= 0) {
            calculateSizes(gridView);
        }

        ViewGroup.LayoutParams layoutParams = new GridView.LayoutParams(cachedWidth,cachedHeight);
        cellView.setLayoutParams(layoutParams);



        cellView.textView.setText(board.getCell(position).getStateString());

         int size = board.getBoardSize();
         switch (size) {
             case 4:
                 cellView.textView.setTextSize(50);
                 break;
             case 8:
                 cellView.textView.setTextSize(20);
                 break;
             case 12:
                 cellView.textView.setTextSize(18);
                 break;
         }

        String state = board.getCell(position).getStateString();
        switch (state) {
            case " ":
                cellView.setBackgroundColor(Color.GRAY);
                break;
            case "flag":
                cellView.setBackground(2);
                cellView.textView.setText(null);
                break;
            case "*":
                cellView.setBackground(1);
                cellView.textView.setText(null);
                break;
            case "bombed":
                cellView.setBackground(3);
                cellView.textView.setText(null);
                break;
            case "1":
                cellView.textView.setTextColor(Color.BLUE);
                break;
            case "2":
                cellView.textView.setTextColor(Color.GREEN);
                break;
            case "3":
                cellView.textView.setTextColor(Color.RED);
                break;
            case "4":
                cellView.textView.setTextColor(Color.YELLOW);
                break;
            case "5":
                cellView.textView.setTextColor(Color.BLACK);
                break;
            case "6":
                cellView.textView.setTextColor(Color.CYAN);
                break;
            case "7":
                cellView.textView.setTextColor(Color.MAGENTA);
                break;
            case "8":
                cellView.textView.setTextColor(Color.WHITE);
                break;
            default:
                cellView.setBackgroundColor(Color.LTGRAY);
                cellView.textView.setText(null);
                break;
        }
        return cellView;
    }

    private void calculateSizes(GridView gridView) {

        int hSpacing =  gridView.getHorizontalSpacing();
        int vSpacing =  gridView.getVerticalSpacing();

        int columns = gridView.getNumColumns();
        int rows = board.getBoardSize() / columns;

        int hPaddingSize = hSpacing * (columns - 1);
        int vPaddingSize = vSpacing * (rows - 1);

        cachedWidth = (gridView.getWidth() - hPaddingSize) / columns;
        cachedHeight = (gridView.getHeight() - vPaddingSize) / rows;
    }
}
