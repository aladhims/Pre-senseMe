package win.aladhims.presensigeofence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.Model.Mahasiswa;

public class EditProfilMahasiswa extends BaseActivity implements View.OnClickListener{

    private CircleImageView mCircleImage;
    private Button mUbahFotoBtn,mSimpanBtn;
    private EditText mEtNPMMahasiswa, mEtKelasMahasiswa,mEtNamaMahasiswa;
    private FirebaseUser mUser;
    private DatabaseReference mahasiswaRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_mahasiswa);

        mCircleImage = (CircleImageView) findViewById(R.id.ci_foto_profil_mahasiswa);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUbahFotoBtn = (Button) findViewById(R.id.btn_ganti_foto_mahasiswa);
        mSimpanBtn = (Button) findViewById(R.id.btn_simpan_edit_mahasiswa);
        mEtNPMMahasiswa = (EditText) findViewById(R.id.et_edit_npm_mahasiswa);
        mEtNamaMahasiswa = (EditText) findViewById(R.id.et_edit_nama_mahasiswa);
        mEtKelasMahasiswa = (EditText) findViewById(R.id.et_edit_kelas_mahasiswa);

        mahasiswaRootRef = FirebaseDatabase.getInstance().getReference().child("users").child("mahasiswa");
        mahasiswaRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(getUid())){
                    Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                    if(mahasiswa.getPhotoUrl() != null) {
                        Glide.with(getApplicationContext())
                                .load(mahasiswa.getPhotoUrl())
                                .into(mCircleImage);
                    }
                    mEtNPMMahasiswa.setText(mahasiswa.getNPM());
                    mEtNamaMahasiswa.setText(mahasiswa.getNama());
                    mEtKelasMahasiswa.setText(mahasiswa.getKelas());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSimpanBtn.setOnClickListener(this);
        mUbahFotoBtn.setOnClickListener(this);
    }



    private void setEnabled(boolean b){
        mUbahFotoBtn.setEnabled(b);
        mSimpanBtn.setEnabled(b);
        mEtNamaMahasiswa.setEnabled(b);
        mEtNPMMahasiswa.setEnabled(b);
        mEtKelasMahasiswa.setEnabled(b);
    }

    @Override
    public void onClick(View v) {

    }
}
