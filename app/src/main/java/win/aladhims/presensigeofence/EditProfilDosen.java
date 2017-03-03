package win.aladhims.presensigeofence;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Mahasiswa;

public class EditProfilDosen extends BaseActivity implements View.OnClickListener {

    private static final String TAG = EditProfilDosen.class.getSimpleName();
    private static final String REQUIERED = "harus diisi";
    private static final int RC_PICK_FOTO = 112;

    private String photoUrl,nip,nama,email;
    private Uri mImageData;

    private FirebaseUser mUser;
    private DatabaseReference rootRef;
    private DatabaseReference dosenRef;
    private StorageReference mStorageRef;

    private CircleImageView mCiFotoProfil;
    private Button mBtnGantiFoto, mSkipOrCancelBtn;
    private EditText mEtNIP, mEtNama, mEtEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_dosen);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        dosenRef = rootRef.child("users").child("dosen");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("users").child("dosen").child("profpict");
        mCiFotoProfil = (CircleImageView) findViewById(R.id.ci_foto_profil_dosen);
        mBtnGantiFoto = (Button) findViewById(R.id.btn_ganti_foto_dosen);
        mSkipOrCancelBtn = (Button) findViewById(R.id.btn_skip_or_cancel_dosen);
        Intent i = getIntent();
        String txt = i.getStringExtra(BaseActivity.EXTRA_FROM);
        mSkipOrCancelBtn.setText(txt);
        mEtNIP = (EditText) findViewById(R.id.et_edit_nip_dosen);
        mEtNama = (EditText) findViewById(R.id.et_edit_nama_dosen);
        mEtEmail = (EditText) findViewById(R.id.et_edit_email_dosen);

        showProgressDialog();
        dosenRef.child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dosen dosen = dataSnapshot.getValue(Dosen.class);
                if(dosen != null){
                    photoUrl = dosen.getPhotoUrl();
                    nip = dosen.getNIP();
                    nama = dosen.getNama();
                    email = dosen.getEmail();
                }else {
                    if(mUser.getPhotoUrl() != null){
                        photoUrl = mUser.getPhotoUrl().toString();
                    }else{
                        photoUrl = "";
                    }
                    if(mUser.getDisplayName() != null){
                        nama = mUser.getDisplayName();
                    }else{
                        nama = "";
                    }if(mUser.getEmail() != null){
                        email = mUser.getEmail();
                    }else{
                        email = "";
                    }
                    nip = "";
                }
                setTextFor(photoUrl,nip,nama,email);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBtnGantiFoto.setOnClickListener(this);
        mSkipOrCancelBtn.setOnClickListener(this);
    }

    private void setTextFor(String fotoURL,String nip,String nama, String email){
        Glide.with(getApplicationContext())
                .load(fotoURL)
                .into(mCiFotoProfil);
        mEtNIP.setText(nip);
        mEtNama.setText(nama);
        mEtEmail.setText(email);
    }

    private void changeProfPict(){
        Intent changeProf = new Intent(Intent.ACTION_GET_CONTENT);
        changeProf.setType("image/*");
        startActivityForResult(Intent.createChooser(changeProf,"Pilih Foto Profil Pengganti"),RC_PICK_FOTO);
    }

    private void updateKeDatabase(){
        final String mNIP = mEtNIP.getText().toString().trim();
        final String mNama = mEtNama.getText().toString();
        final String mEmail = mEtEmail.getText().toString().trim();

        if(TextUtils.isEmpty(mNIP)){
            mEtNIP.setError(REQUIERED);
            return;
        }
        if(TextUtils.isEmpty(mNama)){
            mEtNama.setError(REQUIERED);
            return;
        }
        if(TextUtils.isEmpty(mEmail)){
            mEtEmail.setError(REQUIERED);
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
                            updateProfilDosen(getUid(),urlFoto.toString(),mNIP,mNama,mEmail);

                        }
                    });
        } else {
            updateProfilDosen(getUid(),null,mNIP,mNama,mEmail);
        }
        setEnabled(true);
    }

    public void updateProfilDosen(String uid, String URLFoto, String NIP, String Nama, String Email){

        Dosen dosen = new Dosen(URLFoto,NIP,Nama,Email);
        Map<String,Object> update = new HashMap<>();
        update.put("/users/dosen/" + uid,dosen);

        rootRef.updateChildren(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(),"Update profil berhasil!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEnabled(boolean b){
        mBtnGantiFoto.setEnabled(b);
        mSkipOrCancelBtn.setEnabled(b);
        mEtNIP.setEnabled(b);
        mEtNama.setEnabled(b);
        mEtEmail.setEnabled(b);
    }

    private void skipOrCancel(){
        startActivity(new Intent(this,ListNgajarkuActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PICK_FOTO){
            if (resultCode == RESULT_OK){
                try {
                    mImageData = data.getData();
                    InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(mImageData);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mCiFotoProfil.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e){
                    Toast.makeText(getApplicationContext(),"Ada kesalahan dalam memasukan gambar",Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"batal mengubah foto",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ganti_foto_dosen:
                changeProfPict();
                break;
            case R.id.btn_skip_or_cancel_dosen:
                skipOrCancel();
                break;
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
        }
        return super.onOptionsItemSelected(item);
    }
}
