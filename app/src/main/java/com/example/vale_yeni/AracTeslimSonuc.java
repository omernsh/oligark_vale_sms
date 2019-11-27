package com.example.vale_yeni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AracTeslimSonuc extends AppCompatActivity {


    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser fUser;
    private ArrayList<Veriler> chatLists = new ArrayList<>();
    private CustomAdapter customAdapter;
    private String subject,arac_ıd;
    private ListView listView;

    TextView kart_no,plaka,ad_soyad,telefon,park_alanı,tarih;

    private  String ad_soyad_str,tarih_str,kart_no_str,park_alanı_str,plaka_str,telefon_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arac_teslim_sonuc);


        Button button=(Button)findViewById(R.id.button);
        TextView text_view=(TextView)findViewById(R.id.text_view);
        ImageView image=(ImageView)findViewById(R.id.image);



        Bundle extras = getIntent().getExtras();
        final int değer = extras.getInt("key");
        final String kart_no_str = extras.getString("kart_no_key");
        arac_ıd = extras.getString("arac_ıd_key");




        if (değer==1){


            text_view.setText("Kart eşleşiyor, araç teslim edilebilir!");
            image.setBackgroundResource(R.drawable.saglam_check_button);
            button.setText("Teslim ediliyor olarak işaretle");


            DatabaseReference oku= FirebaseDatabase.getInstance().getReference().child("ARACLAR").child(arac_ıd);


            ValueEventListener kontrol=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                     ad_soyad_str = String.valueOf(dataSnapshot.child("ad_soyad_str").getValue());
                     tarih_str = String.valueOf(dataSnapshot.child("dateTime").getValue());

                     park_alanı_str = String.valueOf(dataSnapshot.child("park_alanı_str").getValue());
                     plaka_str = String.valueOf(dataSnapshot.child("plaka_str").getValue());
                     telefon_str = String.valueOf(dataSnapshot.child("telefon_str").getValue());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            oku.addValueEventListener(kontrol);

        }

        else {

            text_view.setText("Kart eşleşmiyor, tekrar dene!");
            image.setBackgroundResource(R.drawable.hasarli_check_button);
            button.setText("Tekrar dene");



        }















        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (değer==1){


                    Toast.makeText(getApplicationContext(), "Teslim ediliyor olarak işaretlendi...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AracTeslimSonuc.this, TeslimatBekleyenler.class);
                    startActivity(intent);



                    db = FirebaseDatabase.getInstance();
                    dbRef = db.getReference("TESLİMAT_BEKLEYENLER");


                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                    String dateTime = formatter.format(curDateTime);


                    final Veriler veriler = new Veriler(kart_no_str,plaka_str,ad_soyad_str,telefon_str,park_alanı_str,dateTime,"değer");
                    dbRef.push().setValue(veriler);


                }

                else {


                    Intent intent = new Intent(AracTeslimSonuc.this, KartTara.class);
                    intent.putExtra("kart_no_key",kart_no_str);
                    startActivity(intent);


                }




            }
        });

    }



}
