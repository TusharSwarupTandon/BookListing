package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.Nullable;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>>
{
    /** Query URL */
    private final String mUrl;

    public BookLoader(Context context, String url)
    {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Book> loadInBackground()
    {
        if (mUrl == null)
        {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
//        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return GetResponse.fetchBookData(mUrl);
    }
}
