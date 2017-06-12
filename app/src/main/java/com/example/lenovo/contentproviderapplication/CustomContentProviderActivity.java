package com.example.lenovo.contentproviderapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lenovo.contentproviderapplication.imageProvider.CustomContentProvider;

/**
 * Created by lenovo on 10/6/17.
 */

public class CustomContentProviderActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etNumber;
    private Button addContactButton;
    private ListView contactListView;
    private android.support.v4.widget.SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_content);

        etName = (EditText) findViewById(R.id.et_contact_name);
        etNumber =(EditText) findViewById(R.id.et_contact);
        addContactButton = (Button) findViewById(R.id.bt_add_new_contact);
        contactListView = (ListView) findViewById(R.id.lv_contacts);

        adapter = new android.support.v4.widget.SimpleCursorAdapter(getBaseContext(),
                R.layout.activity_contact_row,
                null,
                new String[] { "name", "phone"},
                new int[] { R.id.tv_new_name, R.id.tv_new_number }, 0);

        contactListView.setAdapter(adapter);
        refreshValuesFromContentProvider();

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewContact();
            }
        });
    }

    private void refreshValuesFromContentProvider() {
        Cursor c = getContentResolver().query(CustomContentProvider.CONTENT_URI,null,null,null,null);
        c.moveToFirst();

        while (c.moveToNext()){
            String value = c.getString(1) + " " + c.getString(2);
            Log.i(getClass().getName(),"Custom Content -> "+ value);
        }
        adapter.swapCursor(c);
    }


    private void addNewContact() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",etName.getText().toString());
        contentValues.put("phone",etNumber.getText().toString());

        Uri uri = getContentResolver().insert(CustomContentProvider.CONTENT_URI, contentValues);
        refreshValuesFromContentProvider();
    }

}
