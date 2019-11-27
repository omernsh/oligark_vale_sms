package com.example.vale_yeni;


import android.app.ProgressDialog;
import android.content.Context;
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

public class GirisYap extends AppCompatActivity {


    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    TextView kart_no;

    private  String telefon_str;

    private EditText tel_no_edittext, kod_edittext, isim_edittext;

    private String mVerificationId;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    SharedPreferences sharedPref;


    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_yap);

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            Intent intent = new Intent(getApplicationContext(), AnaSayfa.class);
            startActivity(intent);
            finish();
        }

        sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);




        tel_no_edittext=(EditText)findViewById(R.id.tel_no_edittext);
        isim_edittext=(EditText)findViewById(R.id.isim_edittext);
        kod_edittext=(EditText)findViewById(R.id.kod_edit_text);

        final Button kodu_elle_gir=(Button)findViewById(R.id.kodu_elle_gir);


        Button giris_yap_button=(Button)findViewById(R.id.giris_yap_button);
        giris_yap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String isim_str=isim_edittext.getText().toString();
                String tel_no_str=tel_no_edittext.getText().toString();





                if ((isim_str.matches(""))||(tel_no_str.matches(""))){



                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurunuz...", Toast.LENGTH_SHORT).show();



                }
                else {


                    progressDialog = new ProgressDialog(GirisYap.this);
                    progressDialog.setMessage("Onay kodu bekleniyor...");
                    progressDialog.show();

                    Intent intent = getIntent();
                    String mobile = intent.getStringExtra("mobile");
                    sendVerificationCode(mobile);

                    kod_edittext.setVisibility(View.VISIBLE);
                    kodu_elle_gir.setVisibility(View.VISIBLE);

                    String code = kod_edittext.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        kod_edittext.setError("Onay kodunu gir...");
                        kod_edittext.requestFocus();
                        return;
                    }

                    //verifying the code entered manually
                    verifyVerificationCode(code);



                    dbRef = db.getReference("VALELER").child(isim_edittext.getText().toString());
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child("Tel no").setValue(tel_no_edittext.getText().toString());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }






            }




        });






        kodu_elle_gir.setOnClickListener(new View.OnClickListener() {
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
                "+90" + tel_no_edittext.getText().toString(),
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
            Toast.makeText(GirisYap.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                .addOnCompleteListener(GirisYap.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Intent intent = new Intent(GirisYap.this, AnaSayfa.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("isim_key",isim_edittext.getText().toString());
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Kimlik doğrulandı...", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPref.edit();
                            String vale_adı=isim_edittext.getText().toString();
                            editor.putString("isim_key",vale_adı);
                            editor.commit();








                        }

                        else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                        }
                    }
                });
    }





    }




