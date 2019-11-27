package com.example.vale_yeni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by alper on 16/07/2017.
 */

public class CustomAdapter extends ArrayAdapter<Veriler> {

    private FirebaseUser firebaseUser;

    public CustomAdapter(Context context, ArrayList<Veriler> chatList, FirebaseUser firebaseUser) {
        super(context, 0, chatList);
        this.firebaseUser = firebaseUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Veriler veriler = getItem(position);


            convertView = LayoutInflater.from(getContext()).inflate(R.layout.arac_items, parent, false);

            TextView plaka = (TextView) convertView.findViewById(R.id.plaka);
            TextView ad_soyad = (TextView) convertView.findViewById(R.id.ad_soyad);
            TextView park_yeri = (TextView) convertView.findViewById(R.id.park_yeri);
        TextView kartno_text = (TextView) convertView.findViewById(R.id.kart_no);

            plaka.setText(veriler.getPlaka_str());
            ad_soyad.setText(veriler.getAd_soyad_str());
           park_yeri.setText(veriler.getPark_alanı_str());
            kartno_text.setText(veriler.getKart_no_str());



        return convertView;
    }
}
