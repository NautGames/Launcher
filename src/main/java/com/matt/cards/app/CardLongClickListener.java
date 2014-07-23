package com.matt.cards.app;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class CardLongClickListener implements RelativeLayout.OnLongClickListener{

    RelativeLayout card3;
    Context mContext;


    public CardLongClickListener(Context ctxt, RelativeLayout cardGrid3)
    {
        ctxt = mContext;
        cardGrid3 = card3;
    }

    @Override
    public boolean onLongClick(View view) {


        return false;
    }
}
