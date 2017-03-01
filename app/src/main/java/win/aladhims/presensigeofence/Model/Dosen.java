package win.aladhims.presensigeofence.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Dosen {

    public String photoUrl,NIP,Nama,Email;

    public Dosen(){}

    public Dosen(String PhotoUrl,String NIP,String Nama,String Email){
        this.photoUrl = PhotoUrl;
        this.NIP = NIP;
        this.Nama = Nama;
        this.Email = Email;
    }

    public Map<String, Object> toMap(){

        HashMap<String,Object> map = new HashMap<>();
        map.put("photoURL",photoUrl);
        map.put("NIP",NIP);
        map.put("Nama",Nama);
        map.put("Email",Email);

        return map;
    }
}
