package win.aladhims.presensigeofence;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;
import win.aladhims.presensigeofence.Model.Mahasiswa;
import win.aladhims.presensigeofence.Model.MahasiswaPresent;
import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaPresentViewHolder;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaViewHolder;
import win.aladhims.presensigeofence.fragment.ListNgajarKuFragment;

public class DetailNgajarActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,EasyPermissions.PermissionCallbacks {

    private static final String TAG = DetailNgajarActivity.class.getSimpleName();
    private static final int RC_PERMS_FINELOC = 15;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

    private int mDurasiSisa;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private String key;

    private DatabaseReference mRootRef,mahasiswaIkutREf,mNgajarRef;
    private FirebaseRecyclerAdapter<MahasiswaPresent,ListMahasiswaPresentViewHolder> mahasiswaAdapter;

    private TextView mTvNamaMatkul,mTvKelas,mTvWaktu,mTvNamaDosenPengajar,mTvNIPDosenPengajar,mTvDurasi;
    private CircleImageView mCiFotoDosen;
    private Button mBtnMulai;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ngajar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        key = getIntent().getStringExtra(ListNgajarKuFragment.EXTRA_KEY_LIST_NGAJARKU);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mNgajarRef = mRootRef.child("ikutngajar-mahasiswa").child(key);
        mahasiswaIkutREf = mNgajarRef.child("mahasiswa");

        mBtnMulai = (Button) findViewById(R.id.btn_mulai_ngajar);
        mTvNamaMatkul = (TextView) findViewById(R.id.tv_nama_matkul_detail_ngajar);
        mTvKelas = (TextView) findViewById(R.id.tv_kelas_detail_ngajar);
        mTvWaktu = (TextView) findViewById(R.id.tv_waktu_detail_ngajar);
        mCiFotoDosen = (CircleImageView) findViewById(R.id.foto_dosen_list);
        mTvNamaDosenPengajar = (TextView) findViewById(R.id.nama_dosen_list);
        mTvNIPDosenPengajar = (TextView) findViewById(R.id.nip_dosen_list);
        mTvDurasi = (TextView) findViewById(R.id.tv_durasi_detail_ngajar);

        mBtnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mulaiNgajar();
            }
        });

        //RecyclerView untuk mahasiswa yang sudah presensi;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_presen_detail_ngajar);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                lm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);
        final DatabaseReference pertRef = mahasiswaIkutREf;
        mahasiswaAdapter = new FirebaseRecyclerAdapter<MahasiswaPresent, ListMahasiswaPresentViewHolder>
                (MahasiswaPresent.class,R.layout.mahasiswa_presen_list_item,ListMahasiswaPresentViewHolder.class,pertRef) {
            @Override
            protected void populateViewHolder(final ListMahasiswaPresentViewHolder viewHolder, final MahasiswaPresent model, int position) {

                mRootRef.child("users").child("mahasiswa").child(model.getMahasiswaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Mahasiswa m = dataSnapshot.getValue(Mahasiswa.class);
                        Glide.with(getApplicationContext())
                                .load(m.getPhotoUrl())
                                .into(viewHolder.mCiMahasiswa);
                        viewHolder.mTvNamaMahasiswa.setText(m.getNama());
                        viewHolder.mTvNPMMahasiswa.setText(m.getNPM());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.mCbValidasi.setChecked(model.isValid());
                viewHolder.mCbValidasi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        model.setValid(isChecked);
                        MahasiswaPresent p = model;
                        pertRef.child(model.getMahasiswaUid()).setValue(p);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mahasiswaAdapter);

        mRootRef.child("ngajar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){
                    Ngajar ngajar = dataSnapshot.child(key).getValue(Ngajar.class);
                    mTvNamaMatkul.setText(ngajar.getNamaMatkul());
                    mTvKelas.setText(ngajar.getKelasDiajar());
                    mTvWaktu.setText(ngajar.getHari()+", "+ngajar.makeJamNgajar());
                    Glide.with(getApplicationContext())
                            .load(ngajar.getPhotoURLDosen())
                            .into(mCiFotoDosen);
                    mTvNamaDosenPengajar.setText(ngajar.getNamaDosen());
                    mTvNIPDosenPengajar.setText(ngajar.getNipDosen());
                    mDurasiSisa = ngajar.getDurasiNgajar()*60;
                    mTvDurasi.setText(String.valueOf(mDurasiSisa));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mulaiNgajar(){
        if (EasyPermissions.hasPermissions(this, perms)) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(statusOfGPS) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    showProgressDialog();

                    Map<String, Object> locHash = new HashMap<>();
                    locHash.put("lat", mLastLocation.getLatitude());
                    locHash.put("long", mLastLocation.getLongitude());
                    mNgajarRef.updateChildren(locHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Berhasil memulai pelajaran!", Toast.LENGTH_LONG).show();
                                mBtnMulai.setEnabled(false);
                                mBtnMulai.setVisibility(View.GONE);
                                Long countLong = TimeUnit.MINUTES.toMillis(mDurasiSisa);
                                CountDownTimer countDownTimer = new CountDownTimer(countLong, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        mTvDurasi.setText(String.format("%d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                    }

                                    @Override
                                    public void onFinish() {
                                        finish();
                                    }
                                };
                                countDownTimer.start();
                            } else {
                                Toast.makeText(getApplicationContext(), "ada kesalahan memasukan data ke database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "gagal mendapatkan lokasi anda", Toast.LENGTH_LONG).show();
                }
            }else {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }else {
            EasyPermissions.requestPermissions(this,"Minta Ijin",RC_PERMS_FINELOC,perms);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mulaiNgajar();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
