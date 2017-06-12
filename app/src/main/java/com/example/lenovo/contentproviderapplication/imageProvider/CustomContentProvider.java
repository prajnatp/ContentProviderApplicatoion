package com.example.lenovo.contentproviderapplication.imageProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by lenovo on 10/6/17.
 */

public class CustomContentProvider extends ContentProvider {

    private static int identifier = 1;
    private static final String PROVIDER_NAME = "com.example.lenovo.contentproviderapplication.provider.customcontent";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/customcontent");
    private static final int ALL_CONTENT = 1;
    private static final int ITEM = 2;

    String[] columnNames = {"_id","name","phone"};
    MatrixCursor matrixCursor;


    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "customcontent", ALL_CONTENT);
        uriMatcher.addURI(PROVIDER_NAME, "customcontent/#", ITEM);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        matrixCursor = new MatrixCursor(columnNames);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_CONTENT:
                return "vnd.android.cursor.dir/vnd.com.example.lenovo.contentproviderapplication.provider.customcontent";
            case ITEM:
                return "vnd.android.cursor.item/vnd.com.example.lenovo.contentproviderapplication.provider.customcontent";

        }
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String[] values = {""+(identifier++),contentValues.getAsString("name"),contentValues.getAsString("phone")};
        matrixCursor.addRow(values);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
