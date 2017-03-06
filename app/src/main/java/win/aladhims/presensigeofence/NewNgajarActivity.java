package win.aladhims.presensigeofence;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
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

public class NewNgajarActivity extends BaseActivity implements View.OnClickListener,RadialTimePickerDialogFragment.OnTimeSetListener,AdapterView.OnItemSelectedListener {

    private static final String TAG = NewNgajarActivity.class.getSimpleName();
    private static final String RADIAL_PICKER_TAG = "radialtimepicker";
    private static final String REQUIERED = "Harus diisi";
    private static final String DOSEN_CHILD = "dosen";

    private String mHari;
    private int mDurasi,mJamMulai,mMenitMulai;

    private DatabaseReference rootRef;

    private TextView mTvJamMulai;
    private EditText mEtNamaMatkul,mEtKelas;
    private Spinner mSpinnerHari, mSpinnerDurasi;
    private FloatingActionButton mTambahNgajar;
    private Button mBtnTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ngajar);

        rootRef = FirebaseDatabase.getInstance().getReference();

        mTvJamMulai = (TextView) findViewById(R.id.tv_waktu_picked);
        mEtNamaMatkul = (EditText) findViewById(R.id.et_nama_matkul_new_ngajar);
        mEtKelas = (EditText) findViewById(R.id.et_kelas_new_ngajar);
        mSpinnerDurasi = (Spinner) findViewById(R.id.spinner_durasi_new_ngajar);
        mSpinnerHari = (Spinner) findViewById(R.id.spinner_hari_new_ngajar);
        mTambahNgajar = (FloatingActionButton) findViewById(R.id.fab_konfirm_tambah_ngajar);
        mBtnTimePicker = (Button) findViewById(R.id.btn_waktu_picker);

        ArrayAdapter<CharSequence> hariAdapter = ArrayAdapter.createFromResource(this,R.array.hari_array,android.R.layout.simple_spinner_item);
        hariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> durasiAdapter = ArrayAdapter.createFromResource(this,R.array.durasi_array,android.R.layout.simple_spinner_item);
        durasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerHari.setAdapter(hariAdapter);
        mSpinnerHari.setOnItemSelectedListener(this);
        mSpinnerDurasi.setAdapter(durasiAdapter);
        mSpinnerDurasi.setOnItemSelectedListener(this);
        mTambahNgajar.setOnClickListener(this);
        mBtnTimePicker.setOnClickListener(this);
    }

    public void tambahNgajar(){
        final String namaMatkul = mEtNamaMatkul.getText().toString().trim();
        final String kelas = mEtKelas.getText().toString().trim();
        showProgressDialog();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef.child("users").child(DOSEN_CHILD).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);

                String key = rootRef.child("dosen-ngajar").child(getUid()).push().getKey();
                Ngajar n = new Ngajar(dosen.getPhotoUrl(),dosen.getNama(),dosen.getEmail(),dosen.getNohape(),0,namaMatkul,mHari,mJamMulai,mMenitMulai,kelas,mDurasi);
                rootRef.child("dosen-ngajar").child(getUid()).child(key).setValue(n);
                rootRef.child("ngajar").child(key).setValue(n);
                hideProgressDialog();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_konfirm_tambah_ngajar:
                tambahNgajar();
                break;
            case R.id.btn_waktu_picker:
                RadialTimePickerDialogFragment radialTime = new RadialTimePickerDialogFragment();
                radialTime.setOnTimeSetListener(this)
                        .setDoneText("Pilih")
                        .setCancelText("Batal")
                        .setForced24hFormat()
                        .setThemeLight()
                        .setTitleText("Pilih Waktu Mulai");
                radialTime.show(getSupportFragmentManager(), RADIAL_PICKER_TAG);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        mJamMulai = hourOfDay;
        mMenitMulai = minute;
        mTvJamMulai.setText(hourOfDay + ":" + minute);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RadialTimePickerDialogFragment rtp = (RadialTimePickerDialogFragment) getSupportFragmentManager().findFragmentByTag(RADIAL_PICKER_TAG);
        if(rtp != null){
            rtp.setOnTimeSetListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner_hari_new_ngajar){
            Log.d(TAG,"Spinner hari :"+position);
            switch (position){
                case 0:
                    mHari = "Senin";
                    break;
                case 1:
                    mHari = "Selasa";
                    break;
                case 2:
                    mHari = "Rabu";
                    break;
                case 3:
                    mHari = "Kamis";
                    break;
                case 4:
                    mHari = "Jum'at";
                    break;
                case 5:
                    mHari = "Sabtu";
                    break;
                default:
                    mHari = "";
            }
        }else if(parent.getId() == R.id.spinner_durasi_new_ngajar){
            switch (position){
                case 0:
                    mDurasi = 1;
                    break;
                case 1:
                    mDurasi = 2;
                    break;
                case 2:
                    mDurasi = 3;
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
