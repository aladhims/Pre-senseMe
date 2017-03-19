package win.aladhims.PresenseMe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.PresenseMe.Model.Dosen;
import win.aladhims.PresenseMe.Model.Mahasiswa;
import win.aladhims.PresenseMe.fragment.ListDosenFragment;
import win.aladhims.PresenseMe.fragment.ListMahasiswaFragment;

public class DetailProfil extends BaseActivity {

    public static final String USER_VIEWING_EXTRA = "THEVIEWER";
    public static final String USER_VIEWED_EXTRA = "THEVIEWED";
    public static final String TIPE_USER_VIEWED = "DOSEN";
    public static final boolean ISDOSENDEFAULT = false;

    private String uid;
    private boolean isDosen;

    private DatabaseReference rootRef,idRef;

    private CircleImageView mCiFotoProfil;
    private TextView mTvId, mTvNama,mTvKelas,mTvNomor,mTvEmail;
    private Button mBtnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profil);

        rootRef = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCiFotoProfil = (CircleImageView) findViewById(R.id.ci_detail_profil);
        mTvId = (TextView) findViewById(R.id.tv_id_detail_profil);
        mTvNama = (TextView) findViewById(R.id.tv_nama_detail_profil);
        mTvKelas = (TextView) findViewById(R.id.tv_kelas_detail_profil);
        mTvNomor = (TextView) findViewById(R.id.tv_nomor_detail_profil);
        mTvEmail = (TextView) findViewById(R.id.tv_email_detail_profil);
        mBtnChat = (Button) findViewById(R.id.btn_chat_detail_profil);



        Intent i = getIntent();
        if(i.hasExtra(ListDosenFragment.EXTRA_DOSEN_PROFIL)){
            isDosen = true;
            uid = i.getStringExtra(ListDosenFragment.EXTRA_DOSEN_PROFIL);
            idRef = rootRef.child("users").child("dosen").child(uid);
            idRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Dosen dosen = dataSnapshot.getValue(Dosen.class);
                    mTvKelas.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(dosen.getPhotoUrl())
                            .into(mCiFotoProfil);

                    mTvId.setText(dosen.getNIP());
                    mTvNama.setText(dosen.getNama());
                    mTvNomor.setText(dosen.getNohape());
                    mTvEmail.setText(dosen.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(i.hasExtra(ListMahasiswaFragment.EXTRA_MAHASISWA_PROFIL)){
            isDosen = false;
            uid = i.getStringExtra(ListMahasiswaFragment.EXTRA_MAHASISWA_PROFIL);
            idRef = rootRef.child("users").child("mahasiswa").child(uid);
            idRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                    mTvNomor.setVisibility(View.GONE);
                    mTvEmail.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(mahasiswa.getPhotoUrl())
                            .into(mCiFotoProfil);
                    mTvId.setText(mahasiswa.getNPM());
                    mTvKelas.setText(mahasiswa.getKelas());
                    mTvNama.setText(mahasiswa.getNama());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        mBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChat(getUid(),uid);
            }
        });
    }

    public void toChat(String viewer,String viewed){
        Intent i = new Intent(this,ChatActivity.class);
        i.putExtra(USER_VIEWING_EXTRA,viewer);
        i.putExtra(USER_VIEWED_EXTRA,viewed);
        i.putExtra(TIPE_USER_VIEWED,isDosen);
        startActivity(i);
    }

    private void getBackToParent(){
        rootRef.child("users").child("mahasiswa").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                if( mahasiswa != null){
                    startActivity(new Intent(getApplicationContext(),MahasiswaDrawerActivity.class));
                    finish();
                }else {
                    rootRef.child("users").child("dosen").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Dosen dosen = dataSnapshot.getValue(Dosen.class);
                            if(dosen != null){
                                startActivity(new Intent(getApplicationContext(),DosenDrawerActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getBackToParent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
