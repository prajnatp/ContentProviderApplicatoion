package com.example.lenovo.contentproviderapplication;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactContentProviderActivity extends AppCompatActivity {

    public static final String CONTACT_NAME = "CONTACTS";

    private Button btnGetContact;
    private Button btnSearch;
    private EditText etSearch;

    private Button btnAdd;
    private EditText etName;
    private EditText etNumber;

    private Button btnUpdate;
    private EditText etUpdateName;
    private EditText etUpdateNumber;

    private Button btnDelete;
    private EditText etDeleteName;

    private String[] projections = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER};
    private String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?";
    private String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        initialiseElements();
        addClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialiseElements() {
        btnGetContact = (Button) findViewById(R.id.bt_get_all_contact);
        btnSearch = (Button) findViewById(R.id.bt_search_contact);
        etSearch = (EditText) findViewById(R.id.et_search_contact);
        btnAdd = (Button) findViewById(R.id.bt_add_contact);
        etName = (EditText) findViewById(R.id.et_name);
        etNumber = (EditText) findViewById(R.id.et_number);
        btnUpdate = (Button) findViewById(R.id.bt_update_contact);
        etUpdateName = (EditText) findViewById(R.id.et_update_name);
        etUpdateNumber = (EditText) findViewById(R.id.et_update_number);
        btnDelete = (Button) findViewById(R.id.bt_delete_contact);
        etDeleteName = (EditText) findViewById(R.id.et_delete_contact);
    }


    private void addClickListener() {
        btnGetContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllContacts();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchedContacts();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewContact();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });
    }

    private void addNewContact() {

        if (etName.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if(etNumber.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Number", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();



        op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, etName.getText().toString())
                .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, etNumber.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        try{
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
            etName.setText("");
            etNumber.setText("");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateContact(){

        if (etUpdateName.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if(etUpdateNumber.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Number", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();

        op_list.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME
                                + " = ?",
                        new String[] { etUpdateName.getText().toString() })
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, etUpdateName.getText().toString())
                .build());

        op_list.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME
                                + " = ?",
                        new String[] { etUpdateName.getText().toString() })
                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, etUpdateNumber.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        try{
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
            Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
            etUpdateName.setText("");
            etUpdateName.setText("");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void  getAllContacts(){
        try{

            Cursor contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , projections, null,
                    null, sort);
            Intent contactListIntent = new Intent(this, DisplayContactsActivity.class);
            ArrayList<String> listItems = new ArrayList<String>();
            while (contactCursor.moveToNext()) {

//                Log.i(getClass().getName(),"Display name --> "+contactCursor.getString(contactCursor
//                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                listItems.add(contactCursor.getString(contactCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + " "
                        + contactCursor.getString(contactCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

//                contactListIntent.putExtra(CONTACT_NAME, contactCursor.getString(contactCursor
//                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

            }
            contactListIntent.putExtra(CONTACT_NAME, listItems);
            startActivity(contactListIntent);
        }catch (Exception e){
            Log.i(getClass().getName(), "Error :");
            e.printStackTrace();

            // Did  not return any results
        }
    }

    private void getSearchedContacts() {
        try{

            if (etSearch.getText().toString().matches("")) {
                Toast.makeText(this, "You did not enter Search value", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] selectionArgs = { etSearch.getText().toString() + "%" };
            Cursor searchContactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    , projections, selection,
                    selectionArgs, sort);
            Intent contactListIntent = new Intent(this, DisplayContactsActivity.class);
            ArrayList<String> listItems = new ArrayList<String>();
            while (searchContactCursor.moveToNext()) {
                listItems.add(searchContactCursor.getString(searchContactCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + " "
                        + searchContactCursor.getString(searchContactCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            if(listItems.isEmpty()){
                Toast.makeText(this, "No contacts Found", Toast.LENGTH_SHORT).show();
                return;
            }
            contactListIntent.putExtra(CONTACT_NAME, listItems);
            startActivity(contactListIntent);
        }catch (Exception e){

        }
    }

    private void deleteContact(){

        if (etDeleteName.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        final ArrayList arrayList = new ArrayList();
        final ContentResolver contentResolver = getContentResolver();
        arrayList.add(ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " = ?",
                        new String[] { etDeleteName.getText().toString() })
                .build());

        try{
            contentResolver.applyBatch(ContactsContract.AUTHORITY, arrayList);
            arrayList.clear();
            Toast.makeText(this, "Selected Contact deleted", Toast.LENGTH_SHORT).show();
            etDeleteName.setText("");
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
