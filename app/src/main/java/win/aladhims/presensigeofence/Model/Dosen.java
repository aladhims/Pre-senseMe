package win.aladhims.presensigeofence.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Dosen {

    public String photoUrl,nip,nama,email;

    public Dosen(){}

    public Dosen(String PhotoUrl,String NIP,String Nama,String Email){
        this.photoUrl = PhotoUrl;
        this.nip = NIP;
        this.nama = Nama;
        this.email = Email;
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
}
