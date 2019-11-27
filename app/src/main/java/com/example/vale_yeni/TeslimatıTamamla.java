package com.example.vale_yeni;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeslimatıTamamla extends AppCompatActivity {


    private String[] para_birimleri={"TL","Dolar","Euro"};

    private Spinner para_birimleri_spinner,ekstra_edit_text;
    private ArrayAdapter<String> para_birimleri_adapter;

    private int ücretli_ücretsiz,layout_kontrol=0;

    final Context context = this;

    private String vale_ücreti_str,ücretsiz_str,ekstra_str,ekstra_ücreti_str,not_str;

    private DatabaseReference dbRef;
    private FirebaseDatabase db;

    String plaka_str;



    private int vale_ücreti_int;

    String plaka;
    Button b_1,b_2,b_3,b_4,b_5,b_6,b_7,b_8,b_9,b_10,b_11,kaydet_button,kart_no_button;

    Switch switch_button;
    TextView ücretli_textview,ücretsiz_text_view;

    SharedPreferences sharedPref;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teslimati_tamamla);



        TextView vale_isim=(TextView)findViewById(R.id.vale_isim);

        sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        final String vale_isim_str = sharedPref.getString("isim_key","Kayıt Yok");
        vale_isim.setText("Vale: "+vale_isim_str);

        Bundle extras = getIntent().getExtras();
        plaka = extras.getString("plaka_key");


        Toast.makeText(getApplicationContext(), plaka, Toast.LENGTH_SHORT).show();





        final Button teslimat_tamamla_button=(Button)findViewById(R.id.teslimat_tamamla_button);
        Button hasar_durumu_button=(Button)findViewById(R.id.hasar_durumu_button);
        Button hasar_layout_kapat_button=(Button)findViewById(R.id.hasar_layout_kapat_button);
        switch_button=(Switch)findViewById(R.id.switch_button);


        final LinearLayout hasar_layout=(LinearLayout)findViewById(R.id.hasar_layout);

        final ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);



        LinearLayout ekstra_gir_button=(LinearLayout)findViewById(R.id.ekstra_gir_button);
        LinearLayout not_gir_button=(LinearLayout)findViewById(R.id.not_gir_button);
        final LinearLayout toplam_layout=(LinearLayout)findViewById(R.id.toplam_layout);


        final LinearLayout ekstra_layout=(LinearLayout)findViewById(R.id.ekstra_layout);
        final LinearLayout not_layout=(LinearLayout)findViewById(R.id.not_layout);

        final EditText not_edit_text=(EditText)findViewById(R.id.not_edit_text);
        final EditText ekstra_ücret_edittext=(EditText)findViewById(R.id.ekstra_ücret_edittext);
        final Spinner ekstra_edit_text=(Spinner)findViewById(R.id.ekstra_spinner);
        final EditText vale_ücreti=(EditText)findViewById(R.id.vale_ücreti);


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

        vale_ücreti_str=vale_ücreti.getText().toString();
        ücretsiz_str="0";
        ekstra_str=ekstra_edit_text.getSelectedItem().toString();
        ekstra_ücreti_str=ekstra_ücret_edittext.getText().toString();
        not_str=not_edit_text.getText().toString();

        ücretsiz_text_view=(TextView)findViewById(R.id.ücretsiz_textview);
        ücretli_textview=(TextView)findViewById(R.id.ücretli_textview);




        final TextView toplam_ücret_text_view=(TextView)findViewById(R.id.toplam_ücret_text_view);

        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked==true){

                    Toast.makeText(getApplicationContext(), "Ücretsiz", Toast.LENGTH_SHORT).show();
                    ücretsiz_text_view.setTextColor(getResources().getColor(R.color.kırmızı));
                    toplam_ücret_text_view.setText("0 "+ para_birimleri_spinner.getSelectedItem().toString());
                    ücretli_ücretsiz=1;

                }
                else {

                    ücretsiz_text_view.setTextColor(getResources().getColor(R.color.beyaz));
                    ücretli_ücretsiz=0;



                }

            }

        });




        ekstra_gir_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ekstra_layout.setVisibility(View.VISIBLE);
                toplam_layout.setVisibility(View.VISIBLE);

            }
        });


        not_gir_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                not_layout.setVisibility(View.VISIBLE);

            }
        });


        hasar_durumu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    hasar_layout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                toplam_layout.setVisibility(View.INVISIBLE);



            }
        });



        hasar_layout_kapat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                    hasar_layout.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                toplam_layout.setVisibility(View.VISIBLE);





            }
        });





        vale_ücreti.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                String value = s.toString();

                if (value.matches("")){

                    toplam_ücret_text_view.setText("0 "+para_birimleri_spinner.getSelectedItem().toString());



                }
                else {

                    vale_ücreti_int=Integer.parseInt(value);
                    toplam_ücret_text_view.setText(value+" "+para_birimleri_spinner.getSelectedItem().toString());



                }



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count){

            }

        });




        ekstra_ücret_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                String value = s.toString();
                vale_ücreti_str=vale_ücreti.getText().toString();


                if (value.matches("")){

                   toplam_ücret_text_view.setText(vale_ücreti_str+" "+para_birimleri_spinner.getSelectedItem().toString());



                }
                else {


                    if (ekstra_ücreti_str!=null){

                        int ekstra_ücreti_int=Integer.parseInt(value);
                        int toplma_ücret=ekstra_ücreti_int+vale_ücreti_int;
                        toplam_ücret_text_view.setText(String.valueOf(toplma_ücret)+" "+para_birimleri_spinner.getSelectedItem().toString());

                    }


                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count){

            }

        });











        final TextView ad_soyad_text_view=(TextView)findViewById(R.id.ad_soyad);
        final TextView plaka_text_view=(TextView)findViewById(R.id.plaka);
        final TextView park_yeri_text_view=(TextView)findViewById(R.id.park_yeri);
        final TextView tarih_text_view=(TextView)findViewById(R.id.tarih);
        final TextView tel_no_text_view=(TextView)findViewById(R.id.tel_no);
        final TextView kart_no_text_view=(TextView)findViewById(R.id.kart_no);




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("TESLİMAT_BEKLEYENLER").orderByChild("plaka_str").equalTo(plaka);


        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String kart_no_str = String.valueOf(dataSnapshot1.child("kart_no_str").getValue());
                     plaka_str = String.valueOf(dataSnapshot1.child("plaka_str").getValue());
                    String ad_soyad_str = String.valueOf(dataSnapshot1.child("ad_soyad_str").getValue());
                    String park_alanı_str = String.valueOf(dataSnapshot1.child("park_alanı_str").getValue());
                    String telefon_str = String.valueOf(dataSnapshot1.child("telefon_str").getValue());
                    String dateTime = String.valueOf(dataSnapshot1.child("dateTime").getValue());

                    ad_soyad_text_view.setText(ad_soyad_str);
                    kart_no_text_view.setText(kart_no_str);
                    plaka_text_view.setText(plaka_str);
                    park_yeri_text_view.setText(park_alanı_str);
                    tarih_text_view.setText(dateTime);
                    tel_no_text_view.setText(telefon_str);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("HasarDurumu").child(plaka);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                String bir = String.valueOf(dataSnapshot.child("1").getValue());
                String iki = String.valueOf(dataSnapshot.child("2").getValue());
                String üç = String.valueOf(dataSnapshot.child("3").getValue());
                String dört = String.valueOf(dataSnapshot.child("4").getValue());
                String beş = String.valueOf(dataSnapshot.child("5").getValue());
                String altı = String.valueOf(dataSnapshot.child("6").getValue());
                String yedi = String.valueOf(dataSnapshot.child("7").getValue());
                String sekiz = String.valueOf(dataSnapshot.child("8").getValue());
                String dokuz = String.valueOf(dataSnapshot.child("9").getValue());
                String on = String.valueOf(dataSnapshot.child("10").getValue());
                String on_bir = String.valueOf(dataSnapshot.child("11").getValue());


                Toast.makeText(getApplicationContext(),bir+iki+üç+dört+beş+altı+yedi+sekiz+dokuz+on+on_bir,Toast.LENGTH_LONG).show();




                if (bir.equals("0")){
                    b_1.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_1.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (iki.equals("0")){
                    b_2.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_2.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (üç.equals("0")){
                    b_3.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_3.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (dört.equals("0")){
                    b_4.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_4.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (beş.equals("0")){
                    b_5.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_5.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (altı.equals("0")){
                    b_6.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_6.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (yedi.equals("0")){
                    b_7.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_7.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (sekiz.equals("0")){
                    b_8.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_8.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (dokuz.equals("0")){
                    b_9.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_9.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (on.equals("0")){
                    b_10.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_10.setBackgroundResource(R.drawable.hasarli_check_button);
                }


                if (on_bir.equals("0")){
                    b_11.setBackgroundResource(R.drawable.saglam_check_button);
                }
                else {
                    b_11.setBackgroundResource(R.drawable.hasarli_check_button);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        teslimat_tamamla_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("TESLİMAT_BEKLEYENLER").orderByChild("plaka_str").equalTo(plaka);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            showMyCustomAlertDialog();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                Query applesQuery1 = ref.child("ARACLAR").orderByChild("plaka_str").equalTo(plaka);

                applesQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            showMyCustomAlertDialog();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                vale_ücreti_str=vale_ücreti.getText().toString();
                ücretsiz_str="0";
                ekstra_str=ekstra_edit_text.getSelectedItem().toString();
                ekstra_ücreti_str=ekstra_ücret_edittext.getText().toString();
                not_str=not_edit_text.getText().toString();





                long msTime = System.currentTimeMillis();
                Date curDateTime = new Date(msTime);
                SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd'.'MM'.'y");

                String dateTime = formatter.format(curDateTime);

                String tarih_2 = formatter1.format(curDateTime);




                Date date = new Date();
                String tarih=DateFormat.getDateInstance(DateFormat.LONG).format(date);





                db = FirebaseDatabase.getInstance();
                dbRef = db.getReference("GEÇMİŞ KAYITLAR").child(tarih);






                vale_ücreti_str=vale_ücreti.getText().toString();
                ücretsiz_str="0";
                ekstra_str=ekstra_edit_text.getSelectedItem().toString();
                ekstra_ücreti_str=ekstra_ücret_edittext.getText().toString();
                not_str=not_edit_text.getText().toString();





                final Veriler veriler = new Veriler(vale_ücreti_str,plaka_text_view.getText().toString(),ad_soyad_text_view.getText().toString(),tel_no_text_view.getText().toString(),ücretsiz_str,dateTime,vale_isim_str);
                dbRef.push().setValue(veriler);














            }
        });




















        para_birimleri_spinner = (Spinner) findViewById(R.id.para_birimi);

        para_birimleri_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, para_birimleri);

        para_birimleri_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        para_birimleri_spinner.setAdapter(para_birimleri_adapter);



        para_birimleri_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);

                if (vale_ücreti_str.matches("")&&ekstra_ücreti_str.matches("")){

                    toplam_ücret_text_view.setText(para_birimleri_spinner.getSelectedItem().toString());

                }
                else if (vale_ücreti_str!=null){
                    toplam_ücret_text_view.setText(vale_ücreti.getText().toString()+" "+para_birimleri_spinner.getSelectedItem().toString());
                }
                else if(ekstra_ücreti_str!=null){


                    int ekstra_ücreti_int=Integer.parseInt(ekstra_ücret_edittext.getText().toString());
                    int vale_ücreti_int=Integer.parseInt(vale_ücreti.getText().toString());

                    int toplma_ücret=ekstra_ücreti_int+vale_ücreti_int;
                    toplam_ücret_text_view.setText(String.valueOf(toplma_ücret)+" "+para_birimleri_spinner.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ekstra_edit_text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ((TextView)parentView.getChildAt(0)).setTextColor(Color.WHITE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });












    }


    public void showMyCustomAlertDialog() {

        // dialog nesnesi oluştur ve layout dosyasına bağlan
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog);

        // custom dialog elemanlarını tanımla - text, image ve button
        Button btnKaydet = (Button) dialog.findViewById(R.id.button_kaydet);
        TextView tvBaslik = (TextView) dialog.findViewById(R.id.textview_baslik);
        ImageView ivResim = (ImageView) dialog.findViewById(R.id.imageview_resim);

        // custom dialog elemanlarına değer ataması yap - text, image
        tvBaslik.setText("Teslimat tamamlandı!");

        // tamam butonunun tıklanma olayları
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TeslimatıTamamla.this, AnaSayfa.class);
                startActivity(intent);



            }
        });

        dialog.show();
    }

}
