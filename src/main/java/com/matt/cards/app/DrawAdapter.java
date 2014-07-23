package com.matt.cards.app;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawAdapter extends BaseAdapter{

    Context mContext;
    MainActivity.pac[] pacsForAdapter;

    public DrawAdapter (Context c, MainActivity.pac pacs[])
    {
        mContext = c;
        pacsForAdapter = pacs;
    }

    @Override
    public int getCount() {

        return pacsForAdapter.length;
    }

    @Override
    public Object getItem(int arg0) {

        return null;
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    static class ViewHolder
    {
        TextView text;
        ImageView icon;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = li.inflate(R.layout.drawer_item, null);
            viewHolder = new ViewHolder();

            viewHolder.text = (TextView) convertView.findViewById(R.id.icon_text);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon_image);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        //ImageView imageView = new ImageView(mContext);
        viewHolder.text.setText(pacsForAdapter[pos].label);
        viewHolder.icon.setImageDrawable(pacsForAdapter[pos].icon);
        /*imageView.setLayoutParams(new GridView.LayoutParams(140,140));
        imageView.setPadding(1,1,1,1);*/
        return convertView;
    }

}
