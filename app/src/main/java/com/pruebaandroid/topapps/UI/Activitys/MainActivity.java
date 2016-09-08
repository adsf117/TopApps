package com.pruebaandroid.topapps.UI.Activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.EventBus.EntryBus;
import com.pruebaandroid.topapps.R;
import com.pruebaandroid.topapps.UI.Fragments.DetailsFragment;
import com.pruebaandroid.topapps.UI.Fragments.ListAplicationsFragment;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    FrameLayout mFrameLayoutListas,mFrameLayoutDetails;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrameLayoutListas = (FrameLayout)findViewById(R.id.container);
        mFrameLayoutDetails = (FrameLayout)findViewById(R.id.container2);
        if(getResources().getBoolean(R.bool.landscape)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ListAplicationsFragment())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container2, new DetailsFragment())
                    .commit();
        }
        initToolbar();
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //setTitle(entry.getName());
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }
    private  void addToolberRowBack()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private  void removeToolberRowBack()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
    @Override public void onResume() {
        super.onResume();
        EntryBus.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        EntryBus.getInstance().unregister(this);
    }
    @Subscribe
    public void onEntryChanged(Entry entry) {
        setTitle(entry.getName());
        mFrameLayoutListas.setVisibility(View.GONE);
        addToolberRowBack();
    }
    @Override
    public void onBackPressed() {
        mFrameLayoutListas.setVisibility(View.VISIBLE);
        removeToolberRowBack();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_adetailsapps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id ==16908332)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
