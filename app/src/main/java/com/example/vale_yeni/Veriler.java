package com.example.vale_yeni;



public class Veriler {

    String kart_no_str;
    String plaka_str;
    String ad_soyad_str;
    String telefon_str;
    String park_alanı_str;
    String dateTime;
    String valeAdı;



    public Veriler() {
    }

    public Veriler(String kart_no_str, String plaka_str, String ad_soyad_str, String telefon_str, String park_alanı_str, String dateTime, String valeAdı) {
        this.kart_no_str = kart_no_str;
        this.plaka_str = plaka_str;
        this.ad_soyad_str = ad_soyad_str;
        this.telefon_str = telefon_str;
        this.park_alanı_str = park_alanı_str;
        this.dateTime = dateTime;
        this.valeAdı = valeAdı;

    }

    public String getKart_no_str() {
        return kart_no_str;
    }

    public void setKart_no_str(String kart_no_str) {
        this.kart_no_str = kart_no_str;
    }



    public String getPlaka_str() {
        return plaka_str;
    }

    public void setPlaka_str(String plaka_str) {
        this.plaka_str = plaka_str;
    }




    public String getAd_soyad_str() {
        return ad_soyad_str;
    }

    public void setAd_soyad_str(String ad_soyad_str) {
        this.ad_soyad_str = ad_soyad_str;
    }





    public String getTelefon_str() {
        return telefon_str;
    }

    public void setTelefon_str(String telefon_str) {
        this.telefon_str = telefon_str;
    }




    public String getPark_alanı_str() {
        return park_alanı_str;
    }

    public void setPark_alanı_str(String park_alanı_str) {
        this.park_alanı_str = park_alanı_str;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValeAdı() {
        return valeAdı;
    }

    public void setValeAdı(String valeAdı) {
        this.valeAdı = valeAdı;
    }


}
