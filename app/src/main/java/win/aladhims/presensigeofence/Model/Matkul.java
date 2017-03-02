package win.aladhims.presensigeofence.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aladhims on 27/02/2017.
 */

public class Matkul {

    public String kodeMatkul,namaMatkul;
    public int SKS;

    public Matkul(){}

    public Matkul(String KodeMatkul, String Nama, int SKS){
        this.kodeMatkul = KodeMatkul;
        this.namaMatkul = Nama;
        this.SKS = SKS;
    }
}
