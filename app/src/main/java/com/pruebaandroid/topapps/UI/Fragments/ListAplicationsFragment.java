package com.pruebaandroid.topapps.UI.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pruebaandroid.topapps.Adapters.AplicatiosAdapter;
import com.pruebaandroid.topapps.Adapters.CategorysAdapter;
import com.pruebaandroid.topapps.DataBase.DBHelperOrm;
import com.pruebaandroid.topapps.DataObjects.Category;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.EventBus.EntryBus;
import com.pruebaandroid.topapps.Loaders.EntryLoader;
import com.pruebaandroid.topapps.R;
import com.pruebaandroid.topapps.UI.widgets.HorizontalListView;
import com.pruebaandroid.topapps.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ListAplicationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Entry>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = ListAplicationsFragment.class.getName();
    /** URL for itunes api data */
    private static final String ITUNES_REQUEST_URL =
            "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private static final int ITUNES_LOADER_ID = 2;

    private AbsListView  mListResults;
    private HorizontalListView mListCategorys;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AplicatiosAdapter mAplicatiosAdapter;
    private CategorysAdapter mCategorysAdapter;
    private ProgressBar mprogressBar;
    private LinearLayout mmessage_network;
    private ImageView mimage_status_network;
    private TextView mtext_status_network;
    private static DBHelperOrm dbHelperOrm;
    private static RuntimeExceptionDao<Entry, Integer> daoEntry;
    private static RuntimeExceptionDao<Category, Integer> daoCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dbHelperOrm = OpenHelperManager.getHelper(getContext(), DBHelperOrm.class);
        daoEntry =dbHelperOrm.getRuntimeExceptionDao(Entry.class);
        daoCategory =dbHelperOrm.getRuntimeExceptionDao(Category.class);
        View rootView = inflater.inflate(R.layout.fragment_list_aplications, container, false);
        mmessage_network = (LinearLayout)rootView.findViewById(R.id.mesagge_network);
        mimage_status_network = (ImageView)rootView.findViewById(R.id.image_status_network);
        mtext_status_network = (TextView)rootView.findViewById(R.id.text_status_network);
        mprogressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        mListCategorys = (HorizontalListView) rootView.findViewById(R.id.listviewcategorys);
        mListCategorys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                findByCatagory(mCategorysAdapter.getData().get(position).getName());
            }
        });
        mListResults = (AbsListView)rootView.findViewById(R.id.listapps);
        mAplicatiosAdapter = new AplicatiosAdapter(getContext(),new ArrayList<Entry>());
        mListResults.setAdapter(mAplicatiosAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        EntryBus.getInstance().register(this);
        mListResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EntryBus.getInstance().post(mAplicatiosAdapter.getItem(position));
            }
        });

        return rootView;
    }

    private  void findByCatagory(String categoryname)
    {
        List<Entry> result=null;
        if(categoryname.equals(getString(R.string.text_all_category))) {
            result =new ArrayList<>(daoEntry.queryForAll());
        }
        else{
            result= new ArrayList<>(daoEntry.queryForEq("category", categoryname));
        }

        mAplicatiosAdapter.clear();
        mAplicatiosAdapter.addAll(result);
    }
    private void showdata()
    {
        if (Utils.isConnected(getContext())) {
            getLoaderManager().initLoader(ITUNES_LOADER_ID, null, this);
            Log.d(LOG_TAG, "internet Conection done");
            showMessageNetWork(true);
        } else
        {
            Log.e(LOG_TAG, "No internet Conection");
            List<Entry>listEntry= new ArrayList<>(daoEntry.queryForAll());
            if (listEntry != null && !listEntry.isEmpty()) {
                Log.d(LOG_TAG, "showing db data");
                mAplicatiosAdapter.addAll(listEntry);
                mCategorysAdapter = new CategorysAdapter(getContext(),new ArrayList<>(daoCategory.queryForAll()));
                mListCategorys.setAdapter(mCategorysAdapter);
                mprogressBar.setVisibility(View.GONE);
                showMessageNetWork(false);
            }
            else
            {
                Fragment someFragment = new NoDataNoConectionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, someFragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        showdata();

    }

    private void showMessageNetWork(boolean conectect)
    {
        if(conectect){
            mmessage_network.setBackgroundColor(getResources().getColor(R.color.onlinecolor));
            mtext_status_network.setText(R.string.status_on_line);
            mimage_status_network.setBackgroundResource(R.drawable.ic_file_cloud_done);
            AnimationSet set = new AnimationSet(true);
            Animation animationItems = null;
            animationItems = new TranslateAnimation(0, 0, 0, 100);
            animationItems.setDuration(5000);
            set.addAnimation(animationItems);
            mmessage_network.startAnimation(animationItems);

            animationItems.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation arg0) {
                    mmessage_network.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationRepeat(Animation arg0) {
                }
                @Override
                public void onAnimationEnd(Animation arg0) {
                    mmessage_network.setVisibility(View.GONE);
                }
            });
        }
        else {
            mmessage_network.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mimage_status_network.setBackgroundResource(R.drawable.ic_file_cloud_off_line);
            mtext_status_network.setText(R.string.status_off_line);
            mmessage_network.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            getLoaderManager().initLoader(ITUNES_LOADER_ID, null, this);
            Log.d(LOG_TAG, "internet Conection done");
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Log.e(LOG_TAG, "No internet Conection");
        }
    }

    @Override
    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
        return new EntryLoader(getContext(), ITUNES_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> entry) {
        Log.d(LOG_TAG, "onLoadFinished");
        mAplicatiosAdapter.clear();
        if (entry != null && !entry.isEmpty()) {
            mCategorysAdapter =new CategorysAdapter(getContext(),new ArrayList<>(daoCategory.queryForAll()));
            mListCategorys.setAdapter(mCategorysAdapter);
            mAplicatiosAdapter.addAll(entry);

        }
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if(mprogressBar!=null) {
            mprogressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mAplicatiosAdapter.clear();
    }

}
