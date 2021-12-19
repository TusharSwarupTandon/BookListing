package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book>
{
    Context mContext;
    public BookAdapter(AppCompatActivity context, ArrayList<Book> book)
    {
        super(context, 0 ,book);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView thumbnailView = listItemView.findViewById(R.id.thumbnail);
        Glide.with(mContext).load(currentBook.getThumbnail()).placeholder(R.drawable.ic_book).into(thumbnailView);
//        Picasso picasso = Picasso.with(getContext());
//        picasso.setLoggingEnabled(true);
//        picasso.load(currentBook.getThumbnail()).into(thumbnailView);
//        Log.e(mContext.toString(),currentBook.getThumbnail());
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());

        TextView authorView = listItemView.findViewById(R.id.authors);
        authorView.setText(currentBook.getAuthor());

        return listItemView;
    }
}
