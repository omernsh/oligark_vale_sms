package com.example.vale_yeni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnaSayfa extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser fUser;
    private ArrayList<Veriler> chatLists = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ListView kayıtlar_listesi;
    List<String> ıd_listesi,teslimat_bekleyen_listesi;
    int tes_bek_say_int;
    private TextView teslimat_bekleyen_sayisi_tv;
    private  String vale_isim_str;
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ana_sayfa);

        teslimat_bekleyen_sayisi_tv=(TextView)findViewById(R.id.teslimat_bek_ar_sayisi);
        ImageView yeni_arac_kayit_button=(ImageView) findViewById(R.id.yeni_arac_kayit_button);
        final ImageView teslimat_bekleyen=(ImageView) findViewById(R.id.teslimat_bekleyen);
        final ImageView istekler=(ImageView) findViewById(R.id.istekler);

        kayıtlar_listesi = (ListView)findViewById(R.id.kayıtlar_listesi);

        TextView vale_isim=(TextView)findViewById(R.id.vale_isim);




        sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        String vale_isim_str = sharedPref.getString("isim_key","Kayıt Yok");
        vale_isim.setText("Vale: "+vale_isim_str);





        db = FirebaseDatabase.getInstance();
        customAdapter = new CustomAdapter(getApplicationContext(),chatLists,fUser);
        kayıtlar_listesi.setAdapter(customAdapter);

        dbRef = db.getReference("ARACLAR");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatLists.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Veriler veriler = ds.getValue(Veriler.class);
                    chatLists.add(veriler);

                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ARACLAR");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ıd_listesi = new ArrayList<>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                    String value1 = String.valueOf(dataSnapshot1.getKey());
                    ıd_listesi.add(value1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });





        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("TESLİMAT_BEKLEYENLER");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teslimat_bekleyen_listesi = new ArrayList<>();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                    String value1 = String.valueOf(dataSnapshot1.getKey());
                    teslimat_bekleyen_listesi.add(value1);

                    tes_bek_say_int = teslimat_bekleyen_listesi.size();

                    if (tes_bek_say_int!=0){
                        teslimat_bekleyen_sayisi_tv.setVisibility(View.VISIBLE);

                        teslimat_bekleyen_sayisi_tv.setText(String.valueOf(tes_bek_say_int));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        yeni_arac_kayit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaSayfa.this,Yeni_Arac_Kayit.class);
                startActivity(intent);
            }
        });


        teslimat_bekleyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AnaSayfa.this,TeslimatBekleyenler.class);
                intent.putExtra("key","1");
                startActivity(intent);
            }
        });




        kayıtlar_listesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String arac_ıd=ıd_listesi.get(position);
                Intent i = new Intent(getApplicationContext(), AracAyrinti.class);
                i.putExtra("arac_ıd_key",arac_ıd);
                startActivity(i);

            }
        });



        istekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AnaSayfa.this,Kronometre.class);
                startActivity(intent);
            }
        });



    }






}
