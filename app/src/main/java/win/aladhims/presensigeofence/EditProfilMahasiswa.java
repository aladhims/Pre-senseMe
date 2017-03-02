package win.aladhims.presensigeofence;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.Model.Mahasiswa;

public class EditProfilMahasiswa extends BaseActivity implements View.OnClickListener{

    private static final String TAG = EditProfilMahasiswa.class.getSimpleName();

    private static final int SELECT_PICT = 12;
    private static final String REQUIERED = "harus diisi";

    private Uri mImageData;

    private CircleImageView mCircleImage;
    private Button mUbahFotoBtn,mSimpanBtn;
    private EditText mEtNPMMahasiswa, mEtKelasMahasiswa,mEtNamaMahasiswa;

    private String mPhotoURL, mNPM, mNama, mKelas;

    private FirebaseUser mUser;
    private DatabaseReference baseRef;
    private DatabaseReference mahasiswaRootRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_mahasiswa);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        baseRef = FirebaseDatabase.getInstance().getReference();
        mahasiswaRootRef = baseRef.child("users").child("mahasiswa");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child("mahasiswa").child("profpict");
        mCircleImage = (CircleImageView) findViewById(R.id.ci_foto_profil_mahasiswa);
        mUbahFotoBtn = (Button) findViewById(R.id.btn_ganti_foto_mahasiswa);
        mSimpanBtn = (Button) findViewById(R.id.btn_simpan_edit_mahasiswa);
        mEtNPMMahasiswa = (EditText) findViewById(R.id.et_edit_npm_mahasiswa);
        mEtNamaMahasiswa = (EditText) findViewById(R.id.et_edit_nama_mahasiswa);
        mEtKelasMahasiswa = (EditText) findViewById(R.id.et_edit_kelas_mahasiswa);

        showProgressDialog();
        mahasiswaRootRef.child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                if(mahasiswa != null) {
                    Log.d(TAG,"object mahasiswa ada!");
                    mPhotoURL = mahasiswa.getPhotoUrl();
                    mNPM = mahasiswa.getNPM();
                    mNama = mahasiswa.getNama();
                    mKelas = mahasiswa.getKelas();
                }else{
                    if(mUser.getPhotoUrl()!=null) {
                        mPhotoURL = mUser.getPhotoUrl().toString();
                    }
                    mNPM = "";
                    if(mUser.getDisplayName()!= null){
                        mNama = mUser.getDisplayName();
                    }else{
                        mNama = "";
                    }
                    mKelas = "";
                }
                setTextForProfil(mPhotoURL,mNPM,mNama,mKelas);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSimpanBtn.setOnClickListener(this);
        mUbahFotoBtn.setOnClickListener(this);



    }


    private void setTextForProfil(String urlPhoto, String NPM, String Nama, String Kelas){

        Glide.with(this)
                .load(urlPhoto)
                .into(mCircleImage);
        mEtNPMMahasiswa.setText(NPM);
        mEtNamaMahasiswa.setText(Nama);
        mEtKelasMahasiswa.setText(Kelas);
    }


    private void changeProfPic(){
        Intent changeProf = new Intent(Intent.ACTION_GET_CONTENT);
        changeProf.setType("image/*");
        startActivityForResult(Intent.createChooser(changeProf,"Pilih Foto Profil Pengganti"),SELECT_PICT);
    }

    private void setEnabled(boolean b){
        mUbahFotoBtn.setEnabled(b);
        mSimpanBtn.setEnabled(b);
        mEtNamaMahasiswa.setEnabled(b);
        mEtNPMMahasiswa.setEnabled(b);
        mEtKelasMahasiswa.setEnabled(b);
    }

    private void updateKeDatabase(){
        final String NPM = mEtNPMMahasiswa.getText().toString().trim();
        final String Nama = mEtNamaMahasiswa.getText().toString();
        final String Kelas = mEtKelasMahasiswa.getText().toString().trim();
        final String defPhotoURL = mUser.getPhotoUrl().toString();

        if(TextUtils.isEmpty(NPM)){
            mEtNPMMahasiswa.setError(REQUIERED);
            return;
        }
        if(TextUtils.isEmpty(Nama)){
            mEtNamaMahasiswa.setError(REQUIERED);
            return;
        }
        if(TextUtils.isEmpty(Kelas)){
            mEtKelasMahasiswa.setError(REQUIERED);
            return;
        }

        setEnabled(false);
        showProgressDialog();
        if(mImageData != null){
            mStorageRef.child(getUid()).putFile(mImageData)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri urlFoto = taskSnapshot.getDownloadUrl();
                            updateProfilMahasiswa(getUid(),urlFoto.toString(),NPM,Nama,Kelas);

                        }
                    });
        } else {
            updateProfilMahasiswa(getUid(),defPhotoURL,NPM,Nama,Kelas);
        }
        setEnabled(true);
    }

    public void updateProfilMahasiswa(String uid,String URLFoto, String NPM, String Nama, String Kelas){

        Mahasiswa mahasiswa = new Mahasiswa(URLFoto,NPM,Nama,Kelas);
        Map<String,Object> update = new HashMap<>();
        update.put("/users/mahasiswa/" + uid,mahasiswa);

        baseRef.updateChildren(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(),"Update profil berhasil!",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ganti_foto_mahasiswa:
                changeProfPic();
                break;
            case R.id.btn_simpan_edit_mahasiswa:
                updateKeDatabase();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICT){
            if (resultCode == RESULT_OK){
                try {
                    mImageData = data.getData();
                    InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(mImageData);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mCircleImage.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e){
                    Toast.makeText(getApplicationContext(),"Ada kesalahan dalam memasukan gambar",Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"batal mengubah foto",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
