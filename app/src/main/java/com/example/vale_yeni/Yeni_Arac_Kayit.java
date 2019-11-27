package com.example.vale_yeni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Yeni_Arac_Kayit extends AppCompatActivity {

    Button b_1,b_2,b_3,b_4,b_5,b_6,b_7,b_8,b_9,b_10,b_11,kaydet_button,kart_no_button;
    Context context=this;
    private int int_1,int_2,int_3,int_4,int_5,int_6,int_7,int_8,int_9,int_10,int_11=0;
    private TextView tarih_textview;
    private EditText kart_no_edittext,ad_soyad_edittext,telefon_edittext;

    Spinner park_alını_edittext;
    String kart_no;
    AutoCompleteTextView  plaka_edittext;


    private DatabaseReference dbRef,dbRef1;
    private FirebaseDatabase db;


    List<String> gecmis_plaka,ad_soyad_listesi,telefon_listesi;
    SharedPreferences sharedPref;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeni_arac_kayit);

        Bundle extras = getIntent().getExtras();


        sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        final String vale_isim_str = sharedPref.getString("isim_key","Kayıt Yok");

        TextView vale_adı=(TextView)findViewById(R.id.vale_adi);
        vale_adı.setText(vale_isim_str);



        b_1=(Button)findViewById(R.id.b_1);
        b_2=(Button)findViewById(R.id.b_2);
        b_3=(Button)findViewById(R.id.b_3);
        b_4=(Button)findViewById(R.id.b_4);
        b_5=(Button)findViewById(R.id.b_5);
        b_6=(Button)findViewById(R.id.b_6);
        b_7=(Button)findViewById(R.id.b_7);
        b_8=(Button)findViewById(R.id.b_8);
        b_9=(Button)findViewById(R.id.b_9);
        b_10=(Button)findViewById(R.id.b_10);
        b_11=(Button)findViewById(R.id.b_11);

        kaydet_button=(Button)findViewById(R.id.kaydet_button);

        tarih_textview=(TextView)findViewById(R.id.tarih);
        kart_no_button=(Button)findViewById(R.id.kart_no_button);
        plaka_edittext=(AutoCompleteTextView) findViewById(R.id.plaka_edittext);
        ad_soyad_edittext=(EditText)findViewById(R.id.ad_soyad_edittext);
        telefon_edittext=(EditText)findViewById(R.id.telefon_edittext);
        TextView tarih_text_view=(TextView)findViewById(R.id.tarih);
        final Spinner park_alanı_edittext = (Spinner) findViewById(R.id.park_alanı_edittext);



        if (extras!=null){

            kart_no = extras.getString("kart_no_key");
            kart_no_button.setText("Kart no: "+kart_no);


        }




        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y HH:mm");
        String dateTime = formatter.format(curDateTime);
        tarih_text_view.setText(dateTime);





        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build() );
        }


        park_alanı_edittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("GEÇMİŞ KAYITLAR");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                gecmis_plaka = new ArrayList<>();
                ad_soyad_listesi = new ArrayList<>();
                telefon_listesi = new ArrayList<>();


                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){


                        String value1 = String.valueOf(dataSnapshot2.child("plaka_str").getValue());
                        gecmis_plaka.add(value1);



                        String value2 = String.valueOf(dataSnapshot2.child("ad_soyad_str").getValue());
                        ad_soyad_listesi.add(value2);



                        String value3 = String.valueOf(dataSnapshot2.child("telefon_str").getValue());
                        telefon_listesi.add(value3);


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, gecmis_plaka);

                        plaka_edittext.setAdapter(adapter);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        plaka_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String seçilen_plaka=plaka_edittext.getText().toString();
                int int_1=gecmis_plaka.indexOf(seçilen_plaka);

                Toast.makeText(getApplicationContext(), "Otomatik doldurulan bilgileri kotrol et!", Toast.LENGTH_LONG).show();

                String seçilen_ad_soyad= ad_soyad_listesi.get(int_1);

                String seçilen_tel_no= telefon_listesi.get(int_1);

                ad_soyad_edittext.setText(seçilen_ad_soyad);

                telefon_edittext.setText(seçilen_tel_no);

            }
        });







        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("ARACLAR");

        kaydet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long msTime = System.currentTimeMillis();
                Date curDateTime = new Date(msTime);
                SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                String dateTime = formatter.format(curDateTime);
                String kart_no_str= kart_no_button.getText().toString();
                String plaka_str= plaka_edittext.getText().toString();
                String ad_soyad_str= ad_soyad_edittext.getText().toString();
                String telefon_str= telefon_edittext.getText().toString();

                String park_alanı_str = String.valueOf(park_alanı_edittext.getSelectedItem());

               final String str_1= String.valueOf(int_1);
                final String str_2= String.valueOf(int_2);
                final String str_3= String.valueOf(int_3);
                final String str_4= String.valueOf(int_4);
                final String str_5=  String.valueOf(int_5);
                final String str_6= String.valueOf(int_6);
                final String str_7= String.valueOf(int_7);
                final String str_8= String.valueOf(int_8);
                final String str_9=  String.valueOf(int_9);
                final String str_10=String.valueOf(int_10);
                final String str_11= String.valueOf(int_11);

                dbRef1 = db.getReference("HasarDurumu").child(plaka_str);
                dbRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("1").setValue(str_1);
                        dataSnapshot.getRef().child("2").setValue(str_2);
                        dataSnapshot.getRef().child("3").setValue(str_3);
                        dataSnapshot.getRef().child("4").setValue(str_4);
                        dataSnapshot.getRef().child("5").setValue(str_5);
                        dataSnapshot.getRef().child("6").setValue(str_6);
                        dataSnapshot.getRef().child("7").setValue(str_7);
                        dataSnapshot.getRef().child("8").setValue(str_8);
                        dataSnapshot.getRef().child("9").setValue(str_9);
                        dataSnapshot.getRef().child("10").setValue(str_10);
                        dataSnapshot.getRef().child("11").setValue(str_11);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });





                final Veriler veriler = new Veriler(String.valueOf(kart_no),plaka_str,ad_soyad_str,telefon_str,park_alanı_str,dateTime,vale_isim_str);

                if (kart_no!=null&&plaka_str!=null&&ad_soyad_str!=null&&telefon_str!=null&&park_alanı_str!=null&&dateTime!=null){

                    dbRef.push().setValue(veriler);

                    Toast.makeText(getApplicationContext(), "KAYIT ALINDI...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Yeni_Arac_Kayit.this, AnaSayfa.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Lütfen tüm bilgileri doldurunuz!",Toast.LENGTH_LONG).show();
                }



            }

        });

        kart_no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Yeni_Arac_Kayit.this, KartTara_2.class);
                intent.putExtra("yeni_kayıt_key",1);
                startActivity(intent);

            }
        });



        b_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_1==0){

                    b_1.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_1=1;
                }
                else {

                    b_1.setBackgroundResource(R.drawable.saglam_check_button);
                    int_1=0;

                }
            }
        });



        b_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_2==0){

                    b_2.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_2=1;
                }
                else {

                    b_2.setBackgroundResource(R.drawable.saglam_check_button);
                    int_2=0;

                }
            }
        });





        b_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_3==0){

                    b_3.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_3=1;
                }
                else {

                    b_3.setBackgroundResource(R.drawable.saglam_check_button);
                    int_3=0;

                }
            }
        });



        b_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_4==0){

                    b_4.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_4=1;
                }
                else {

                    b_4.setBackgroundResource(R.drawable.saglam_check_button);
                    int_4=0;

                }
            }
        });



        b_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_5==0){

                    b_5.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_5=1;
                }
                else {

                    b_5.setBackgroundResource(R.drawable.saglam_check_button);
                    int_5=0;

                }
            }
        });


        b_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_6==0){

                    b_6.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_6=1;
                }
                else {

                    b_6.setBackgroundResource(R.drawable.saglam_check_button);
                    int_6=0;

                }
            }
        });


        b_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_7==0){

                    b_7.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_7=1;
                }
                else {

                    b_7.setBackgroundResource(R.drawable.saglam_check_button);
                    int_7=0;

                }
            }
        });


        b_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_8==0){

                    b_8.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_8=1;
                }
                else {

                    b_8.setBackgroundResource(R.drawable.saglam_check_button);
                    int_8=0;

                }
            }
        });


        b_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_9==0){

                    b_9.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_9=1;
                }
                else {

                    b_9.setBackgroundResource(R.drawable.saglam_check_button);
                    int_9=0;

                }
            }
        });


        b_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_10==0){

                    b_10.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_10=1;
                }
                else {

                    b_10.setBackgroundResource(R.drawable.saglam_check_button);
                    int_10=0;

                }
            }
        });


        b_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_11==0){

                    b_11.setBackgroundResource(R.drawable.hasarli_check_button);
                    int_11=1;
                }
                else {

                    b_11.setBackgroundResource(R.drawable.saglam_check_button);
                    int_11=0;

                }
            }
        });

    }







}
