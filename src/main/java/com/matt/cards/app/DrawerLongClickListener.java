package com.matt.cards.app;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AdapterView.OnItemLongClickListener;

import java.net.ContentHandler;

public class DrawerLongClickListener implements OnItemLongClickListener{

    Context mContext;
    SlidingDrawer drawerForAdapter;
    RelativeLayout homeViewForAdapter;
    MainActivity.pac[] pacsForListener;

    public DrawerLongClickListener(Context ctxt, SlidingDrawer slidingDrawer, RelativeLayout homeView, MainActivity.pac[] pacs)
    {
        mContext = ctxt;
        drawerForAdapter = slidingDrawer;
        homeViewForAdapter = homeView;
        pacsForListener = pacs;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        MainActivity.appLaunchable = false;

        LayoutParams lp = new LayoutParams(view.getWidth(), view.getHeight());
        lp.leftMargin = (int) view.getX();
        lp.topMargin = (int) view.getY();

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.drawer_item, null);

        ((ImageView)ll.findViewById(R.id.icon_image)).setImageDrawable(((ImageView) view.findViewById(R.id.icon_image)).getDrawable());
        ((TextView)ll.findViewById(R.id.icon_text)).setText(((TextView)view.findViewById(R.id.icon_text)).getText());

        ll.setOnTouchListener(new AppTouchListener(view.getWidth()));
        ll.setOnClickListener(new AppClickListener(pacsForListener, mContext));

        String[] data = new String[2];
        data[0] = pacsForListener[i].packageName;
        data[1] = pacsForListener[i].name;

        ll.setTag(data);

        homeViewForAdapter.addView(ll, lp);
        drawerForAdapter.animateClose();
        drawerForAdapter.bringToFront();

        return false;
    }
}
