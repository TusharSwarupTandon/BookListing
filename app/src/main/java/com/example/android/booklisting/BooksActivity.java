package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>
{
    private static final String firstHalfRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    private static String userBookSearch = "";
    private static final String secondHalfRequestUrl = "&maxResults=40";

    private BookAdapter mAdapter;

    private static int BOOK_LOADER_ID = 0;

    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingIndicator;
    private EditText mBookNameTextView;
    private ImageButton mSearchButton;
    private ListView mBookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mBookListView = findViewById(R.id.list);
        mLoadingIndicator = findViewById(R.id.loading_spinner);
        mLoadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        mBookListView.setEmptyView(mEmptyStateTextView);

        mBookNameTextView = findViewById(R.id.book_name_input_view);

        mBookNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    onSearch();
                    return true;
                }
                return false;
            }
        });


        mBookListView = findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        mBookListView.setAdapter(mAdapter);

        mBookListView.setOnItemClickListener((adapterView, view,position,l)->
        {
            Book currentBook = mAdapter.getItem(position);
            Uri bookUri = Uri.parse(currentBook.getPreview());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
            startActivity(websiteIntent);
        });

        mSearchButton = findViewById(R.id.book_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSearch();
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle)
    {
        // Create a new loader for the given URL
        String print = firstHalfRequestUrl+userBookSearch+secondHalfRequestUrl;
        return new BookLoader(this, print);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books)
    {
        mLoadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_book);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkNetworkStatus()
    {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    private void onSearch()
    {
        String bookName = mBookNameTextView.getText().toString().replaceAll("\\s","");
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), "nhi hua",Toast.LENGTH_SHORT).show();
        }
        if(!bookName.equals(""))
        {
            if(checkNetworkStatus())
            {
                mEmptyStateTextView.setVisibility(View.INVISIBLE);
                mBookListView.setVisibility(View.VISIBLE);
                userBookSearch = mBookNameTextView.getText().toString().replaceAll("\\s","-");
                mLoadingIndicator.setVisibility(View.VISIBLE);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(BOOK_LOADER_ID++, null, BooksActivity.this);
            }
            else
            {
                mBookListView.setVisibility(View.INVISIBLE);
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText("No Internet Connection");
                Toast.makeText(BooksActivity.this, "Net not available", Toast.LENGTH_SHORT).show();

                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            Toast.makeText(BooksActivity.this, "Please enter a book name first", Toast.LENGTH_SHORT).show();
        }
    }
}
