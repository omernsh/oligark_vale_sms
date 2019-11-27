package com.example.vale_yeni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class TeslimSecenek extends AppCompatActivity {


    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser fUser;
    private ArrayList<Veriler> chatLists = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ListView listView;


    private  String kart_no,arac_ıd,plaka,telefon;

    private Button kart_okut_button_1,kayıp_kart_button,kodukontrol_et_button;
    private EditText kod_edittext;
    private String mVerificationId;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teslim_secenek);

        mAuth = FirebaseAuth.getInstance();



        kart_okut_button_1=(Button)findViewById(R.id.kart_okut_button_1);
        kayıp_kart_button=(Button)findViewById(R.id.kayıp_kart_button);
        kodukontrol_et_button=(Button)findViewById(R.id.kodu_elle_gir);

        kod_edittext=(EditText)findViewById(R.id.kod_edit_text);



        Bundle extras = getIntent().getExtras();
        kart_no = extras.getString("kart_no_key");
        arac_ıd = extras.getString("arac_ıd_key");
        plaka = extras.getString("plaka_key");
        telefon = extras.getString("telefon_key");


        Toast.makeText(getApplicationContext(), plaka, Toast.LENGTH_SHORT).show();



        kart_okut_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(TeslimSecenek.this, KartTara.class);
                intent.putExtra("kart_no_key",kart_no);
                intent.putExtra("plaka_key",plaka);
                intent.putExtra("arac_ıd_key",arac_ıd);

                startActivity(intent);


            }
        });





        kayıp_kart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = getIntent();
                String mobile = intent.getStringExtra("mobile");
                sendVerificationCode(mobile);

                kod_edittext.setVisibility(View.VISIBLE);
                kodukontrol_et_button.setVisibility(View.VISIBLE);

                String code = kod_edittext.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    kod_edittext.setError("Onay kodunu gir...");
                    kod_edittext.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);

            }
        });


        kodukontrol_et_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = kod_edittext.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    kod_edittext.setError("Onay kodunu gir...");
                    kod_edittext.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);

            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+90" + telefon,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);


    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                kod_edittext.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(TeslimSecenek.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(TeslimSecenek.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(TeslimSecenek.this,TeslimatıTamamla.class);
                            intent.putExtra("key",1);
                            intent.putExtra("kart_no_key",kart_no);
                            intent.putExtra("arac_ıd_key",arac_ıd);
                            intent.putExtra("plaka_key",plaka);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Kimlik doğrulandı...", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(getApplicationContext(), "Kimlik doğrulanamadı, tekrar dene...", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }






}
