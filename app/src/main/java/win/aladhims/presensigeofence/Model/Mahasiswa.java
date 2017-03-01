package win.aladhims.presensigeofence.Model;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Mahasiswa {

    public String photoUrl,NPM, nama,kelas;

    public Mahasiswa(){}

    public Mahasiswa(String PhotoUrl,String NPM, String Nama, String Kelas){
        this.photoUrl = PhotoUrl;
        this.NPM = NPM;
        this.nama = Nama;
        this.kelas  = Kelas;
    }

    public String getPhotoUrl(){
        return this.photoUrl;
    }

    public void setPhotoUrl(String Url){
        this.photoUrl = Url;
    }

    public String getNPM(){
        return this.NPM;
    }

    public void setNPM(String NPM){
        this.NPM = NPM;
    }

    public String getNama(){
        return this.nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public String getKelas(){
        return this.kelas;
    }

    public void setKelas(String Kelas){
        this.kelas = Kelas;
    }
}
