package com.matt.cards.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardView;
import android.view.Gravity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

    DrawAdapter drawerAdapterObject;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    class pac
    {
        Drawable icon;
        String name;
        String packageName;
        String label;
    }

    pac[] pacs;
    PackageManager pm;
    GridView drawerGrid;
    SlidingDrawer slidingDrawer;
    RelativeLayout homeView;
    RelativeLayout widgetView;
    Context mContext;
    int REQUEST_CREATE_APPWIDGET = 900;
    EditText numRowsET;
    RelativeLayout box3;

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    //action id
    private static final int ID_RENAME    = 1;
    private static final int ID_DELETE   = 2;
    private static final int ID_CB = 3;

    //int iconViewX;
    //int iconViewY;

    //String numRowsString;
    //int num_rows;

    GridView grid;

    static boolean appLaunchable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

        /*//Calling the Middle Card
        MiddleCard();
        //Calling the Bottom Card
        BottomCard();
        //Calling the Second Middle Card
        MiddleCard1();
        //Calling the top card
        TopCard();
        //Calling the other top card
        TopCard1();*/

        pm = getPackageManager();

        mContext = this;

        //Create layoutinflater
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_settings, null);

        numRowsET = (EditText) view.findViewById(R.id.num_rows_et);

        /*LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.box_middle, null);*/

        drawerGrid = (GridView) findViewById(R.id.gridMain);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
        homeView = (RelativeLayout) findViewById(R.id.icon_view3);
        widgetView = (RelativeLayout) findViewById(R.id.home_view);

        set_pacs();
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                appLaunchable = true;
            }
        });

        widgetView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                selectWidget();
                return false;
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(new PacReceiver(), filter);

        /*slideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
        b1 = (Button) findViewById(R.id.Button01);
        b2 = (Button) findViewById(R.id.Button02);
        b3 = (Button) findViewById(R.id.Button03);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slideButton.setText("");
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slideButton.setText("");
            }
        });*/

        QuickActionItem mailItem    = new QuickActionItem(ID_RENAME, "Rename", getResources().getDrawable(R.drawable.rename_ic));
        QuickActionItem vlcItem     = new QuickActionItem(ID_DELETE, "Delete", getResources().getDrawable(R.drawable.delete_ic));
        QuickActionItem safariItem  = new QuickActionItem(ID_CB, "Change Image", getResources().getDrawable(R.drawable.image_ic));
//create QuickActionPopup. Use QuickActionPopup.VERTICAL or QuickActionPopup.HORIZONTAL //param to define orientation
        final QuickActionPopup quickActionPopup1 = new QuickActionPopup(this, QuickActionPopup.VERTICAL);

        //add action items into QuickActionPopup
        quickActionPopup1.addActionItem(mailItem);
        quickActionPopup1.addActionItem(vlcItem);
        quickActionPopup1.addActionItem(safariItem);

        //Set listener for action item clicked
        quickActionPopup1.setOnActionItemClickListener(new QuickActionPopup.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActionPopup source, int pos, int actionId) {

                //filtering items by id
                if (actionId == ID_RENAME) {
                    Toast.makeText(getApplicationContext(), "Mail clicked", Toast.LENGTH_SHORT).show();
                } else if (actionId == ID_DELETE) {
                    Toast.makeText(getApplicationContext(), "VLC clicked", Toast.LENGTH_SHORT).show();
                } else if(actionId == ID_CB){
                    Toast.makeText(getApplicationContext(), "Safari clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set dismiss listener
        quickActionPopup1.setOnDismissListener(new QuickActionPopup.OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
            }
        });



        //show on btn1
        RelativeLayout btn1 = (RelativeLayout) this.findViewById(R.id.icon_view3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickActionPopup1.show(v);
            }
        });


    }

    /*private void getNumRows()
    {
        numRowsString = numRowsET.getText().toString();
        num_rows = Integer.parseInt(numRowsString);

        Toast.makeText(this, num_rows, Toast.LENGTH_LONG).show();
    }*/

    void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
    }
    void addEmptyData(Intent pickIntent) {
        ArrayList customInfo = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList customExtras = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            }
            else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        }
        else if (resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        LayoutParams lp = new RelativeLayout.LayoutParams(500,500);
        widgetView.addView(hostView,lp);
        slidingDrawer.bringToFront();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    public void set_pacs()
    {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
        pacs = new pac[pacsList.size()];
        for (int I = 0; I < pacsList.size(); I++)
        {
            pacs[I] = new pac();
            pacs[I].icon = pacsList.get(I).loadIcon(pm);
            pacs[I].packageName = pacsList.get(I).activityInfo.packageName;
            pacs[I].name = pacsList.get(I).activityInfo.name;
            pacs[I].label = pacsList.get(I).loadLabel(pm).toString();
        }

        new SortApps().exchange_sort(pacs);

        drawerAdapterObject = new DrawAdapter(this, pacs);
        drawerGrid.setAdapter(drawerAdapterObject);

        drawerGrid.setOnItemClickListener(new DrawerClickListener(this, pacs, pm));
        drawerGrid.setOnItemLongClickListener(new DrawerLongClickListener(this, slidingDrawer, homeView, pacs));
        //box3.setOnLongClickListener(new CardLongClickListener(this, box3));
    }

    public class PacReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            set_pacs();
        }
    }

    /*public void BottomCard()
    {

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.bottomGrid);

        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }

    public void MiddleCard()
    {

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.middleGrid);

        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }

    public void MiddleCard1()
    {

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.middleGrid1);

        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }

    public void TopCard()
    {

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.TopGrid);

        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }

    public void TopCard1()
    {

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        cards.add(card);

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.TopGrid1);

        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }*/

    /*public void onClick(View v)
    {
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.android.contacts");
        startActivity(LaunchIntent);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                setContentView(R.layout.activity_settings);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
