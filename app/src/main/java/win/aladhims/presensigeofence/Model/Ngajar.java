package win.aladhims.presensigeofence.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aladhims on 03/03/2017.
 */

public class Ngajar {

    public String getNipDosen() {
        return nipDosen;
    }

    public void setNipDosen(String nipDosen) {
        this.nipDosen = nipDosen;
    }

    String nipDosen;
    String photoURLDosen;
    String namaDosen;
    String emailDosen;
    String kontakDosen;
    String namaMatkul;
    String hari;
    String kelasDiajar;
    int durasiNgajar;
    public int jumlahStar = 0;
    int jam;
    int menit;
    int jumlahSKS;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String uid;
    public Map<String, Boolean> stars = new HashMap<>();

    public String getHari() {
        return hari;
    }

    public int getJam() {
        return jam;
    }

    public int getMenit() {
        return menit;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setJam(int jam) {
        this.jam = jam;
    }

    public void setMenit(int menit) {
        this.menit = menit;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public String getPhotoURLDosen() {
        return photoURLDosen;
    }

    public void setPhotoURLDosen(String photoURLDosen) {
        this.photoURLDosen = photoURLDosen;
    }

    public int getJumlahStar() {
        return jumlahStar;
    }

    public void setJumlahStar(int jumlahStar) {
        this.jumlahStar = jumlahStar;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

    public void setEmailDosen(String emailDosen) {
        this.emailDosen = emailDosen;
    }

    public void setKontakDosen(String kontakDosen) {
        this.kontakDosen = kontakDosen;
    }

    public void setNamaMatkul(String namaMatkul) {
        this.namaMatkul = namaMatkul;
    }

    public void setKelasDiajar(String kelasDiajar) {
        this.kelasDiajar = kelasDiajar;
    }

    public void setDurasiNgajar(int durasiNgajar) {
        this.durasiNgajar = durasiNgajar;
    }



    public String getEmailDosen() {
        return emailDosen;
    }

    public String getKontakDosen() {
        return kontakDosen;
    }

    public String getNamaMatkul() {
        return namaMatkul;
    }

    public String getKelasDiajar() {
        return kelasDiajar;
    }

    public int getDurasiNgajar() {
        return durasiNgajar;
    }
    public int getJumlahSKS() {
        return jumlahSKS;
    }

    public void setJumlahSKS(int jumlahSKS) {
        this.jumlahSKS = jumlahSKS;
    }

    public boolean containUid(String uid){
        if(this.stars.containsKey(uid)){
            return true;
        }else{
            return false;
        }
    }

    public void putUid(String uid){
        this.stars.put(uid,true);
    }

    public void removeUid(String uid){
        this.stars.remove(uid);
    }

    public Ngajar(){}

    public Ngajar(String uid,String photoURLDosen, String namaDosen, String emailDosen, String nip, String kontakDosen, String namaMatkul, int jumlahsks, String hari, int jam, int menit, String kelasDiajar, int durasiNgajar){
        this.namaDosen = namaDosen;
        this.emailDosen = emailDosen;
        this.kontakDosen = kontakDosen;
        this.namaMatkul = namaMatkul;
        this.jumlahSKS = jumlahsks;
        this.hari = hari;
        this.nipDosen = nip;
        this.jam = jam;
        this.menit = menit;
        this.kelasDiajar = kelasDiajar;
        this.durasiNgajar = durasiNgajar;
        this.photoURLDosen = photoURLDosen;
        this.uid = uid;
    }

    public String makeJamNgajar(){
        return this.getJam()+":"+this.getMenit();
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("photoURLDosen",photoURLDosen);
        map.put("namaDosen",namaDosen);
        map.put("emailDosen",emailDosen);
        map.put("nipDosen",nipDosen);
        map.put("kontakDosen",kontakDosen);
        map.put("jumlahStar",jumlahStar);
        map.put("stars",stars);
        map.put("namaMatkul",namaMatkul);
        map.put("jumlahSKS",jumlahSKS);
        map.put("hari",hari);
        map.put("jam",jam);
        map.put("menit",menit);
        map.put("kelasDiajar",kelasDiajar);
        map.put("durasiNgajar",durasiNgajar);

        return map;
    }

}
