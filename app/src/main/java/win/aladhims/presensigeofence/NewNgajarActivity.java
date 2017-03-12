package win.aladhims.presensigeofence;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.fragment.ListNgajarKuFragment;

public class NewNgajarActivity extends BaseActivity implements View.OnClickListener,RadialTimePickerDialogFragment.OnTimeSetListener,AdapterView.OnItemSelectedListener {

    private static final String TAG = NewNgajarActivity.class.getSimpleName();
    private static final String RADIAL_PICKER_TAG = "radialtimepicker";
    private static final String REQUIERED = "Harus diisi";
    private static final String DOSEN_CHILD = "dosen";

    private String mHari;
    private int mDurasi,mJamMulai,mMenitMulai,mJumlahSKS;

    private DatabaseReference rootRef;
    private String keyBawaan;
    private String[] hariArray;

    private TextView mTvJamMulai;
    private EditText mEtNamaMatkul,mEtKelas;
    private Spinner mSpinnerHari, mSpinnerDurasi,mSpinnerSKS;
    private FloatingActionButton mTambahNgajar;
    private Button mBtnTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ngajar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootRef = FirebaseDatabase.getInstance().getReference();
        hariArray = getResources().getStringArray(R.array.hari_array);
        mTvJamMulai = (TextView) findViewById(R.id.tv_waktu_picked);
        mEtNamaMatkul = (EditText) findViewById(R.id.et_nama_matkul_new_ngajar);
        mEtKelas = (EditText) findViewById(R.id.et_kelas_new_ngajar);
        mSpinnerSKS = (Spinner) findViewById(R.id.spinner_jumlah_sks_new_ngajar);
        mSpinnerDurasi = (Spinner) findViewById(R.id.spinner_durasi_new_ngajar);
        mSpinnerHari = (Spinner) findViewById(R.id.spinner_hari_new_ngajar);
        mTambahNgajar = (FloatingActionButton) findViewById(R.id.fab_konfirm_tambah_ngajar);
        mBtnTimePicker = (Button) findViewById(R.id.btn_waktu_picker);

        final ArrayAdapter<CharSequence> hariAdapter = ArrayAdapter.createFromResource(this,R.array.hari_array,android.R.layout.simple_spinner_item);
        hariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<Integer> durasiAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new Integer[]{1,2,3});
        durasiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<Integer> sksAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new Integer[]{1,2,3});
        sksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerHari.setAdapter(hariAdapter);
        mSpinnerHari.setOnItemSelectedListener(this);
        mSpinnerDurasi.setAdapter(durasiAdapter);
        mSpinnerDurasi.setOnItemSelectedListener(this);
        mSpinnerSKS.setAdapter(sksAdapter);
        mSpinnerSKS.setOnItemSelectedListener(this);
        mTambahNgajar.setOnClickListener(this);
        mBtnTimePicker.setOnClickListener(this);

        if(getIntent().hasExtra(ListNgajarKuFragment.EXTRA_KEY_LIST_NGAJARKU)){
            keyBawaan = getIntent().getStringExtra(ListNgajarKuFragment.EXTRA_KEY_LIST_NGAJARKU);
            rootRef.child("dosen-ngajar").child(getUid()).child(keyBawaan).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Ngajar ngajar = dataSnapshot.getValue(Ngajar.class);
                    mEtNamaMatkul.setText(ngajar.getNamaMatkul());
                    mEtKelas.setText(ngajar.getKelasDiajar());
                    mJamMulai = ngajar.getJam();
                    mMenitMulai = ngajar.getMenit();
                    String jamToDisplay = String.valueOf(mJamMulai);
                    String menitToDisplay = String.valueOf(mMenitMulai);
                    if(mJamMulai < 10) jamToDisplay = "0" + mJamMulai;
                    if(mMenitMulai < 10) menitToDisplay = "0" + mMenitMulai;
                    mTvJamMulai.setText(jamToDisplay+":"+menitToDisplay);
                    mSpinnerHari.setSelection(checkPosisiHari(ngajar.getHari()));
                    mSpinnerDurasi.setSelection(durasiAdapter.getPosition(ngajar.getDurasiNgajar()));
                    mSpinnerSKS.setSelection(sksAdapter.getPosition(ngajar.getJumlahSKS()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public int checkPosisiHari(String hari){

        for(int i=0; i<hariArray.length;i++){
            if (hari.equals(hariArray[i])){
                return i;
            }
        }
        return 0;
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
                if(keyBawaan != null){
                    key = keyBawaan;
                }


                Ngajar n = new Ngajar(getUid(),dosen.getPhotoUrl(),dosen.getNama(),dosen.getEmail(),dosen.getNIP(),dosen.getNohape(),namaMatkul,mJumlahSKS,mHari,mJamMulai,mMenitMulai,kelas,mDurasi);
                Map<String, Object> ngajar = n.toMap();
                Map<String, Object> updateNgajar = new HashMap<>();
                updateNgajar.put("/dosen-ngajar/" + getUid() + "/" + key, ngajar);
                updateNgajar.put("/ngajar/" + key, ngajar);
                rootRef.updateChildren(updateNgajar);
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
                if(keyBawaan != null){
                    radialTime.setStartTime(mJamMulai,mMenitMulai);
                }
                radialTime.show(getSupportFragmentManager(), RADIAL_PICKER_TAG);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        mJamMulai = hourOfDay;
        mMenitMulai = minute;
        String jamToDisplay = String.valueOf(hourOfDay);
        String menitToDisplay = String.valueOf(minute);
        if(hourOfDay < 10) jamToDisplay = "0" + hourOfDay;
        if(minute < 10) menitToDisplay = "0" + minute;

        mTvJamMulai.setText(jamToDisplay + ":" + menitToDisplay);
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
                    mHari = hariArray[0];
                    break;
                case 1:
                    mHari = hariArray[1];
                    break;
                case 2:
                    mHari = hariArray[2];
                    break;
                case 3:
                    mHari = hariArray[3];
                    break;
                case 4:
                    mHari = hariArray[4];
                    break;
                case 5:
                    mHari = hariArray[5];
                    break;
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
        }else if(parent.getId() == R.id.spinner_jumlah_sks_new_ngajar){
            switch (position){
                case 0:
                    mJumlahSKS = 1;
                    break;
                case 1:
                    mJumlahSKS = 2;
                    break;
                case 2:
                    mJumlahSKS = 3;
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,DosenDrawerActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
