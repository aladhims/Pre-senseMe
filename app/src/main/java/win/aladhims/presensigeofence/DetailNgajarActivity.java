package win.aladhims.presensigeofence;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.Model.Mahasiswa;
import win.aladhims.presensigeofence.Model.MahasiswaPresent;
import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaPresentViewHolder;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaViewHolder;
import win.aladhims.presensigeofence.fragment.ListNgajarKuFragment;

public class DetailNgajarActivity extends BaseActivity {

    private static final String TAG = DetailNgajarActivity.class.getSimpleName();

    private int Pertemuan;

    private String key;

    private DatabaseReference mRootRef,mahasiswaIkutREf;
    private FirebaseRecyclerAdapter<MahasiswaPresent,ListMahasiswaPresentViewHolder> mahasiswaAdapter;

    private TextView mTvNamaMatkul,mTvKelas,mTvWaktu,mTvNamaDosenPengajar,mTvNIPDosenPengajar,mTvDurasi;
    private Spinner mSpinnerPertemuan;
    private CircleImageView mCiFotoDosen;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ngajar);

        key = getIntent().getStringExtra(ListNgajarKuFragment.EXTRA_KEY_LIST_NGAJARKU);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mahasiswaIkutREf = mRootRef.child("ikutngajar-mahasiswa").child(key);

        MahasiswaPresent mp = new MahasiswaPresent();
        mahasiswaIkutREf.child("DtPSigO7bGgju86aECl2aY30f8q1").setValue(mp);

        mTvNamaMatkul = (TextView) findViewById(R.id.tv_nama_matkul_detail_ngajar);
        mTvKelas = (TextView) findViewById(R.id.tv_kelas_detail_ngajar);
        mTvWaktu = (TextView) findViewById(R.id.tv_waktu_detail_ngajar);
        mCiFotoDosen = (CircleImageView) findViewById(R.id.foto_dosen_list);
        mTvNamaDosenPengajar = (TextView) findViewById(R.id.nama_dosen_list);
        mTvNIPDosenPengajar = (TextView) findViewById(R.id.nip_dosen_list);
        mTvDurasi = (TextView) findViewById(R.id.tv_durasi_detail_ngajar);
        mSpinnerPertemuan = (Spinner) findViewById(R.id.spinner_pertemuan_detail_ngajar);
        ArrayAdapter<Integer> pertemuanAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new Integer[]{1,2,3});
        pertemuanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPertemuan.setAdapter(pertemuanAdapter);
        mSpinnerPertemuan.setSelection(0);
        mSpinnerPertemuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setAdapterWithPertemuan(1);
                        break;
                    case 1:
                        setAdapterWithPertemuan(2);
                        break;
                    case 2:
                        setAdapterWithPertemuan(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_presen_detail_ngajar);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                lm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);
        DatabaseReference pertRef = mahasiswaIkutREf.child("satu");
        mahasiswaAdapter = new FirebaseRecyclerAdapter<MahasiswaPresent, ListMahasiswaPresentViewHolder>(MahasiswaPresent.class,R.layout.mahasiswa_presen_list_item,ListMahasiswaPresentViewHolder.class,pertRef) {
            @Override
            protected void populateViewHolder(final ListMahasiswaPresentViewHolder viewHolder, MahasiswaPresent model, int position) {

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
                viewHolder.mTvTotalHadir.setText(String.valueOf(model.getTotalAbsen()));
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
                    mTvDurasi.setText(String.valueOf(ngajar.getDurasiNgajar()));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAdapterWithPertemuan(int per){

    }
}
