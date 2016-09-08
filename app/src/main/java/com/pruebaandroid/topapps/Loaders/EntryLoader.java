package com.pruebaandroid.topapps.Loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.Webservices.QueryiTunes;

import java.util.List;

/**
 * Created by Andres on 05/09/2016.
 */
/**
 * Loads a list of aplication itunes api by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EntryLoader  extends AsyncTaskLoader<List<Entry>> {

        /** Tag for log messages */
        private static final String LOG_TAG = EntryLoader.class.getName();

        /** Query URL */
        private String mUrl;

        /**
         * Constructs a new {@link EntryLoader}.
         *
         * @param context of the activity
         * @param url to load data from
         */
        public EntryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

        @Override
        protected void onStartLoading() {
            Log.d(LOG_TAG, "onStartLoading");
            forceLoad();
    }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Entry> loadInBackground() {
            Log.d(LOG_TAG, "loadInBackground");
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Entry> entrys = QueryiTunes.getData(mUrl,getContext());
        return entrys;
    }
}
