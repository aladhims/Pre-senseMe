package win.aladhims.PresenseMe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import win.aladhims.PresenseMe.Model.Mahasiswa;

public class EditProfilMahasiswa extends BaseActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = EditProfilMahasiswa.class.getSimpleName();

    private static final int SELECT_PICT = 12;
    private static final String REQUIERED = "harus diisi";

    private Uri mImageData;

    private CircleImageView mCircleImage;
    private Button mUbahFotoBtn,mSkipOrCancelBtn;
    private EditText mEtNPMMahasiswa, mEtKelasMahasiswa,mEtNamaMahasiswa;

    private String mPhotoURL, mNPM, mNama, mKelas;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference baseRef;
    private DatabaseReference mahasiswaRootRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_mahasiswa);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        baseRef = FirebaseDatabase.getInstance().getReference();
        mahasiswaRootRef = baseRef.child("users").child("mahasiswa");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child("mahasiswa").child("profpict");
        mCircleImage = (CircleImageView) findViewById(R.id.ci_foto_profil_mahasiswa);
        mUbahFotoBtn = (Button) findViewById(R.id.btn_ganti_foto_mahasiswa);
        mSkipOrCancelBtn = (Button) findViewById(R.id.btn_skip_or_cancel_mahasiswa);
        Intent i = getIntent();
        if(i.getStringExtra(MahasiswaDrawerActivity.EXTRA_FROM_MAHASISWADRAWER) != null){
            mSkipOrCancelBtn.setText("CANCEL");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else if(i.getStringExtra(BaseActivity.EXTRA_FROM) != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mSkipOrCancelBtn.setVisibility(View.GONE);
        }
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

        mSkipOrCancelBtn.setOnClickListener(this);
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
        mSkipOrCancelBtn.setEnabled(b);
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
                        startActivity(new Intent(getApplicationContext(),MahasiswaDrawerActivity.class));
                        finish();
                    }
                });
    }

    private void skipOrCancel(){
        startActivity(new Intent(this,MahasiswaDrawerActivity.class));
        finish();
    }

    private void signOut(){
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this,SignInActivity.class));
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ganti_foto_mahasiswa:
                changeProfPic();
                break;
            case R.id.btn_skip_or_cancel_mahasiswa:
                skipOrCancel();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profil_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_simpan_edit_dosen:
                updateKeDatabase();
                break;
            case R.id.menu_sign_out_dosen_edit_profil:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
