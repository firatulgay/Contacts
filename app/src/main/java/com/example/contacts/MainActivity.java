package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnAdd;
    ListView contactsList;
    LayoutInflater inflater;
    View dialogView;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Button btnAra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        addListener();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call("05366856546");
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void addListener() {

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final AlertDialog diyalog = new AlertDialog.Builder(MainActivity.this).create();
                diyalog.setTitle("KİŞİ ARAMA");

                final View dialogView = inflater.inflate(R.layout.contact_dialog_detail, null);
                btnAra = dialogView.findViewById(R.id.btnAra);
                TextView txtName = dialogView.findViewById(R.id.txtName);
                final TextView txtNumber = dialogView.findViewById(R.id.txtNumber);

                List<String> nameList = getNameList();
                txtName.setText(nameList.get(0).toString());
                List<Long> numberList = getNumberList();
                txtNumber.setText(String.valueOf(numberList.get(0)));

                diyalog.setView(dialogView);
                diyalog.show();


                btnAra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call(txtNumber.getText().toString());
                    }

                });

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog diyalog = new AlertDialog.Builder(MainActivity.this).create();
                diyalog.setTitle("Yeni Kişi Ekle");

                final View dialogView = inflater.inflate(R.layout.contact_dialog, null);
                final EditText edtName = dialogView.findViewById(R.id.edtName);
                final EditText edtNumber = dialogView.findViewById(R.id.edtNumber);
                Button btnKaydet = dialogView.findViewById(R.id.btnKaydet);

                btnKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String isim = edtName.getText().toString();
                        String numara = edtNumber.getText().toString();
                        kaydet(isim, numara);
                        diyalog.dismiss();
                    }
                });
                diyalog.setView(dialogView);
                diyalog.show();
                diyalog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        List<String> nameList = getNameList();

                        ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, nameList);
                        contactsList.setAdapter(veriAdaptoru);
                    }
                });

            }
        });
    }

    private void call(String txtNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + txtNumber));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
        startActivity(callIntent);
    }

    private List<String> getNameList() {
        List<String> nameList = new ArrayList<>();

        String name = preferences.getString("name", "Kayıt yok");
        nameList.add(name);

        return nameList;

    }

    private List<Long> getNumberList() {
        List<Long> numberList = new ArrayList<>();

        long number = preferences.getLong("numara", 0);
        numberList.add(number);

        return numberList;

    }

    private void kaydet(String isim, String numara) {
        editor = preferences.edit();
        editor.putString("name", isim); // name değeri "key" dir.Daha sonradan bu değerle
        editor.putLong("numara", Long.valueOf(numara));
        editor.commit();
        Toast.makeText(this, "Kayıt yapıldı.", Toast.LENGTH_LONG).show();
    }

    private void initialize() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        inflater = this.getLayoutInflater();
        btnAdd = findViewById(R.id.btnEkle);
        contactsList = findViewById(R.id.listViewContacts);
        dialogView = inflater.inflate(R.layout.contact_dialog, null);
        btnAra = findViewById(R.id.btnAra);


    }
}
