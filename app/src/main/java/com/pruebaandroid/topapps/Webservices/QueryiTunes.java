package com.pruebaandroid.topapps.Webservices;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pruebaandroid.topapps.DataBase.DBHelperOrm;
import com.pruebaandroid.topapps.DataObjects.Category;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving itunes data from itunes Api.
 */
public final class QueryiTunes {

    private static final String LOG_TAG = QueryiTunes.class.getSimpleName();
    private static DBHelperOrm dbHelperOrm;
    private static RuntimeExceptionDao<Entry, Integer> daoEntry;
    private static RuntimeExceptionDao<Category, Integer> daoCategory;
    private static Context mcontext;

    private QueryiTunes() {
    }

    /**
     * Query the itunes Api dataset and return a list of {@link Entry} objects.
     */
    public static List<Entry> getData(String requestUrl, Context context) {

        mcontext = context;
        dbHelperOrm = OpenHelperManager.getHelper(mcontext, DBHelperOrm.class);
        daoEntry = dbHelperOrm.getRuntimeExceptionDao(Entry.class);
        daoCategory = dbHelperOrm.getRuntimeExceptionDao(Category.class);
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Entry}s
        List<Entry> entries = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Entry}s
        return entries;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {


        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.d(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Entry} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Entry> extractFeatureFromJson(String stringJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Entry> entries = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string an get the root
            JSONObject baseJsonResponse = (new JSONObject(stringJSON)).getJSONObject("feed");

            // Extract the JSONArray associated with the key called "entry",
            JSONArray entryArray = baseJsonResponse.getJSONArray("entry");
            if (entryArray.length() > 0) {
                List<Entry> allEntryRecords = daoEntry.queryForAll();
                for (Entry entry : allEntryRecords) {

                    daoEntry.deleteById(entry.getIdEntry());
                }
                if(needCreateCategory(mcontext.getString(R.string.text_all_category))) {
                    Category allCategory = new Category();
                    allCategory.setName(mcontext.getString(R.string.text_all_category));
                    daoCategory.createIfNotExists(allCategory);
                }
            }
            // For each data in the list entrys object, create an {@link Entry} object
            for (int i = 0; i < entryArray.length(); i++) {
                Entry entryObjet = new Entry();
                JSONObject entrys = entryArray.getJSONObject(i);
                JSONObject name = entrys.getJSONObject("im:name");
                entryObjet.setName(name.get("label").toString());
                JSONObject artist = entrys.getJSONObject("im:artist");
                entryObjet.setArtist(artist.get("label").toString());
                JSONObject summary = entrys.getJSONObject("summary");
                entryObjet.setSummary(summary.get("label").toString());
                JSONArray image = entrys.getJSONArray("im:image");
                entryObjet.setUrlimagen(image.getJSONObject(2).get("label").toString());
                JSONObject category = entrys.getJSONObject("category");
                JSONObject attributes = category.getJSONObject("attributes");
                entryObjet.setCategory(attributes.get("label").toString());
                JSONObject price = entrys.getJSONObject("im:price");
                JSONObject attributesprice = price.getJSONObject("attributes");
                entryObjet.setPriceamount(attributesprice.get("amount").toString());
                entryObjet.setPricecurrency(attributesprice.get("currency").toString());
                JSONObject releaseDate = entrys.getJSONObject("im:releaseDate");
                JSONObject attributesreleaseDate = releaseDate.getJSONObject("attributes");
                entryObjet.setReleaseDate(attributesreleaseDate.get("label").toString());
                entries.add(entryObjet);
                if(needCreateCategory(entryObjet.getCategory()))
                 {
                    Category mcategory = new Category();
                    mcategory.setName(entryObjet.getCategory());
                    daoCategory.createIfNotExists(mcategory);
                }
                daoEntry.createIfNotExists(entryObjet);
            }

        } catch (JSONException e) {

            Log.d("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of top aplication
        return entries;
    }
    public static Boolean needCreateCategory(String category)
    {
        List<Category> listaCategoria = new ArrayList<>(daoCategory.queryForEq("name", category));
       Boolean needCreate =false;
        if (listaCategoria.size() == 0)
        {
            needCreate=true;
        }
        return needCreate;

    }
}
