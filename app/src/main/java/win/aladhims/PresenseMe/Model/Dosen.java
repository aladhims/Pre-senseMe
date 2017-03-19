package win.aladhims.PresenseMe.Model;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Dosen {

    public String photoUrl;
    public String nip;
    public String nama;
    public String email;
    public String nohape;

    public Dosen(){}

    public Dosen(String PhotoUrl,String NIP,String Nama,String Email,String nohape){
        this.photoUrl = PhotoUrl;
        this.nip = NIP;
        this.nama = Nama;
        this.email = Email;
        this.nohape = nohape;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }
    public void setPhotoUrl(String url){
        this.photoUrl = url;
    }

    public String getNIP(){
        return nip;
    }
    public void setNIP(String NIP){
        this.nip = NIP;
    }

    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getNohape() {
        return nohape;
    }

    public void setNohape(String nohape) {
        this.nohape = nohape;
    }

}
