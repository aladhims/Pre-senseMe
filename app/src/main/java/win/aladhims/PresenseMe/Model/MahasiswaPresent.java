package win.aladhims.PresenseMe.Model;

/**
 * Created by Aladhims on 07/03/2017.
 */

public class MahasiswaPresent {


    private String mahasiswaUid;
    private boolean valid = false;

    public MahasiswaPresent(){}

    public MahasiswaPresent(String Uid){
        this.mahasiswaUid = Uid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMahasiswaUid() {
        return mahasiswaUid;
    }

    public void setMahasiswaUid(String mahasiswaUid) {
        this.mahasiswaUid = mahasiswaUid;
    }
}
