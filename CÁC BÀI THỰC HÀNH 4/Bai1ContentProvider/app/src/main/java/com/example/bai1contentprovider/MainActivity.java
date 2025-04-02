package com.example.bai1contentprovider;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listViewContacts;
    private ArrayAdapter <String> adapter;

    private ArrayList <String> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhxa();

        if(ContextCompat.checkSelfPermission(this,  Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        } else{
            loadContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loadContacts();
            } else{
                Toast.makeText(this, "Bạn chưa cấp quyền truy cập", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void loadContacts(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        while(cursor.moveToNext()){
            String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            contacts.add(contactName);
        }
        adapter.notifyDataSetChanged();
        listViewContacts.setAdapter(adapter);
        cursor.close();


    }
    private void anhxa() {
        listViewContacts = findViewById(R.id.listViewContacts);
        contacts = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);

    }
}