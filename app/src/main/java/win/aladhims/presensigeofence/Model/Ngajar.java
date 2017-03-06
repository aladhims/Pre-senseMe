package win.aladhims.presensigeofence.Model;

/**
 * Created by Aladhims on 03/03/2017.
 */

public class Ngajar {

    String photoURLDosen;
    String namaDosen;
    String emailDosen;
    String kontakDosen;
    String namaMatkul;
    String hari;
    String kelasDiajar;
    int durasiNgajar;
    int jumlahStar,jam,menit;

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



    public Ngajar(){}

    public Ngajar(String photoURLDosen,String namaDosen,String emailDosen,String kontakDosen,int jumlahStar,String namaMatkul,String hari,int jam,int menit,String kelasDiajar,int durasiNgajar){
        this.namaDosen = namaDosen;
        this.emailDosen = emailDosen;
        this.kontakDosen = kontakDosen;
        this.namaMatkul = namaMatkul;
        this.jumlahStar = jumlahStar;
        this.hari = hari;
        this.jam = jam;
        this.menit = menit;
        this.kelasDiajar = kelasDiajar;
        this.durasiNgajar = durasiNgajar;
        this.photoURLDosen = photoURLDosen;
    }

}
