package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView contactsView;
    ArrayList<String> contactsList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsView = (ListView)findViewById(R.id.listview1);
        contactsList = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            contactsList = getContacts();
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contacts_list_item, R.id.text1,contactsList);
            contactsView.setAdapter(arrayAdapter);
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_CONTACTS}, 1);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_CONTACTS}, 1);

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String per[], int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,"Permission Granted", Toast.LENGTH_LONG).show();
                    contactsList = getContacts();
                    arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contacts_list_item, R.id.text1, contactsList);
                    contactsView.setAdapter(arrayAdapter);

                } else {

                    Toast.makeText(MainActivity.this,"Permission denied", Toast.LENGTH_LONG).show();

                }
                return;
        }
    }
    public ArrayList<String> getContacts(){
        Cursor cursor;
        String name;
        String phone;
        ArrayList<String> contacts = new ArrayList<String>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "1").build();
        cursor = getContentResolver().query(uri, null,null, null, null);
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(name + ": " + phone);
        }
        cursor.close();
        return contacts;
    }

}