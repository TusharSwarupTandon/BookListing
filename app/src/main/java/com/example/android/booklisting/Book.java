package com.example.android.booklisting;

import android.service.quicksettings.Tile;

public class Book
{
    /*Thumbnail of the book*/
    private String mThumbnail;
    /*Title of the book*/
    private String mTitle;
    /*Author of the book*/
    private String mAuthor;
    /*Preview URL of the book*/
    private String mPreview;


    public Book(String Thumbnail, String Title, String Author, String Preview)
    {
        mThumbnail = Thumbnail;
        mTitle = Title;
        mAuthor = Author;
        mPreview = Preview;
    }


    public String getThumbnail()
    {
        return mThumbnail;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public String getPreview()
    {
        return mPreview;
    }
}
