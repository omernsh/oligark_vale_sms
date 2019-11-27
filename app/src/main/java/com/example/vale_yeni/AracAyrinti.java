package com.example.vale_yeni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AracAyrinti extends AppCompatActivity {



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    TextView kart_no,plaka,ad_soyad,telefon,park_alanı,tarih,vale_Adi;

    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    String arac_ıd;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arac_ayrinti);




        Bundle extras = getIntent().getExtras();
        arac_ıd = extras.getString("arac_ıd_key");



        kart_no=(TextView) findViewById(R.id.kart_no);
        plaka=(TextView) findViewById(R.id.plaka);
        ad_soyad=(TextView) findViewById(R.id.ad_soyad);
        telefon=(TextView) findViewById(R.id.telefon);
        park_alanı=(TextView) findViewById(R.id.park_alanı);
        tarih=(TextView) findViewById(R.id.tarih);
        vale_Adi=(TextView) findViewById(R.id.vale_adi);






        DatabaseReference oku= FirebaseDatabase.getInstance().getReference().child("ARACLAR").child(arac_ıd);
        ValueEventListener kontrol=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String ad_soyad_str = String.valueOf(dataSnapshot.child("ad_soyad_str").getValue());
                String tarih_str = String.valueOf(dataSnapshot.child("dateTime").getValue());
                String kart_no_str = String.valueOf(dataSnapshot.child("kart_no_str").getValue());
                String park_alanı_str = String.valueOf(dataSnapshot.child("park_alanı_str").getValue());
                String plaka_str = String.valueOf(dataSnapshot.child("plaka_str").getValue());
                String telefon_str = String.valueOf(dataSnapshot.child("telefon_str").getValue());
                String vale_adi_str = String.valueOf(dataSnapshot.child("valeAdı").getValue());


                ad_soyad.setText(ad_soyad_str);
                tarih.setText(tarih_str);
                kart_no.setText(kart_no_str);
                park_alanı.setText(park_alanı_str);
                plaka.setText(plaka_str);
                telefon.setText(telefon_str);
                vale_Adi.setText(vale_adi_str);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        oku.addValueEventListener(kontrol);



        Button button=(Button)findViewById(R.id.button);
       /* araci_teslim_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AracAyrinti.this, KartTara.class);
                intent.putExtra("kart_no_key",kart_no.getText().toString());
                intent.putExtra("arac_ıd_key",arac_ıd);
                startActivity(intent);
            }
        });  */






        mAuth = FirebaseAuth.getInstance();






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                    Intent intent = new Intent(AracAyrinti.this, TeslimatBekleyenler.class);
                intent.putExtra("kart_no_key",kart_no.getText().toString());
                intent.putExtra("arac_ıd_key",arac_ıd);
                intent.putExtra("plaka_key",plaka.getText().toString());
                    startActivity(intent);



                    db = FirebaseDatabase.getInstance();
                    dbRef = db.getReference("TESLİMAT_BEKLEYENLER");


                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                    String dateTime = formatter.format(curDateTime);

                    String kart_no_str=kart_no.getText().toString();
                String plaka_str=plaka.getText().toString();
                String ad_soyad_str=ad_soyad.getText().toString();
                String telefon_str=telefon.getText().toString();
                String park_alanı_str=park_alanı.getText().toString();



                final Veriler veriler = new Veriler(kart_no_str,plaka_str,ad_soyad_str,telefon_str,park_alanı_str,dateTime,"değer");
                    dbRef.push().setValue(veriler);




            }
        });



    }
}





