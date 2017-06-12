package com.example.lenovo.contentproviderapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lenovo on 9/6/17.
 */

public class DisplayContactsActivity extends AppCompatActivity {
    private ListView contactList;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);

        contactList=(ListView)findViewById(R.id.lv_contact_list);

//        ArrayList<String> listItems = new ArrayList<String>();
//        listItems.add(name);

            ArrayList<String> listItems = (ArrayList<String>) getIntent().getSerializableExtra(ContactContentProviderActivity.CONTACT_NAME);

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,listItems);
            contactList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    }
}
