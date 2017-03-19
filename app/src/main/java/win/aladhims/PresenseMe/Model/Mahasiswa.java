package win.aladhims.PresenseMe.Model;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Mahasiswa {

    public String photoUrl,npm, nama,kelas;

    public Mahasiswa(){}

    public Mahasiswa(String PhotoUrl,String NPM, String Nama, String Kelas){
        this.photoUrl = PhotoUrl;
        this.npm = NPM;
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
        return npm;
    }

    public void setNPM(String NPM){
        this.npm = NPM;
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public String getKelas(){
        return kelas;
    }

    public void setKelas(String Kelas){
        this.kelas = Kelas;
    }
}
