package win.aladhims.presensigeofence;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Aladhims on 01/03/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_FROM = "FROM";
    public GeoFire baseGeoFire;

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Tunggu Sebentar");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setGeoRef(DatabaseReference ref){
        baseGeoFire = new GeoFire(ref);
    }

    public GeoFire getBaseGeoFire(){
        return this.baseGeoFire;
    }


}
