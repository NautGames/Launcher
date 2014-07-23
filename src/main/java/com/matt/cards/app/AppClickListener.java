package com.matt.cards.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class AppClickListener implements View.OnClickListener
{
    MainActivity.pac[] pacsForListener;
    Context mContext;

    public AppClickListener(MainActivity.pac[] pacs, Context ctxt)
    {
        pacsForListener = pacs;
        mContext = ctxt;
    }

    @Override
    public void onClick(View v) {

        String[] data;
        data = (String[]) v.getTag();

        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cp = new ComponentName(data[0], data[1]);
        launchIntent.setComponent(cp);

        mContext.startActivity(launchIntent);
    }
}
