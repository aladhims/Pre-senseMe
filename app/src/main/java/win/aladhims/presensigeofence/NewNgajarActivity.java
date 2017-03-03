package win.aladhims.presensigeofence;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Ngajar;

public class NewNgajarActivity extends BaseActivity {

    private static final String TAG = NewNgajarActivity.class.getSimpleName();
    private static final String REQUIERED = "Harus diisi";
    private static final String DOSEN_CHILD = "dosen";

    private static final String dummyKontak = "085726494433";

    private DatabaseReference rootRef;

    private FloatingActionButton mTambahNgajar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ngajar);

        rootRef = FirebaseDatabase.getInstance().getReference();

        mTambahNgajar = (FloatingActionButton) findViewById(R.id.fab_konfirm_tambah_ngajar);

        mTambahNgajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahNgajar();
            }
        });
    }

    public void tambahNgajar(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef.child("users").child(DOSEN_CHILD).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);

                Ngajar n = new Ngajar(dosen.getPhotoUrl(),dosen.getNama(),dosen.getEmail(),dummyKontak,0,"Algoritma 1","08.30 AM","3IA09","2");
                rootRef.child("dosen-ngajar").child(getUid()).push().setValue(n);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buatNgajarBaru(String uid,String NIP, String namaDosen,String photoURL, String emailDosen){

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

        }
    }
}
