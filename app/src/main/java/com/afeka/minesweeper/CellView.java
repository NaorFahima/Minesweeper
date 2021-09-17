package com.afeka.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CellView  extends LinearLayout {

    TextView textView;

    public CellView(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
        textView = new TextView(context);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);
        addView(textView);

        setBackgroundColor(Color.GRAY);
    }

    public void setBackground(int num) {
        switch (num) {
            case 1:
                setBackground(getResources().getDrawable(R.drawable.mine));
                break;
            case 2:
                setBackground(getResources().getDrawable(R.drawable.flag));
                break;
            case 3:
                setBackground(getResources().getDrawable(R.drawable.explosion));
                break;
        }
    }
}
