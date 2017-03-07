package win.aladhims.presensigeofence.Model;

/**
 * Created by Aladhims on 07/03/2017.
 */

public class MahasiswaPresent {


    private String mahasiswaUid;
    private boolean valid;
    private int totalAbsen = 0;

    public MahasiswaPresent(){}

    public MahasiswaPresent(String Uid){
        this.mahasiswaUid = Uid;
    }

    public void plusHadir(){
        this.totalAbsen++;
    }

    public void minHadir(){
        this.totalAbsen--;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getTotalAbsen() {
        return totalAbsen;
    }

    public String getMahasiswaUid() {
        return mahasiswaUid;
    }

    public void setMahasiswaUid(String mahasiswaUid) {
        this.mahasiswaUid = mahasiswaUid;
    }
}
