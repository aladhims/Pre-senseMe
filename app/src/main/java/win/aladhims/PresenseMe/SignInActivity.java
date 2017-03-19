package win.aladhims.PresenseMe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import win.aladhims.PresenseMe.Model.Dosen;
import win.aladhims.PresenseMe.Model.Mahasiswa;

public class SignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    public static final String FROM_SIGNIN = "SKIP";
    private static final int RC_SIGN_IN_GOOGLE = 1111;
    private static final int MAHASISWA_TYPE = 1122;
    private static final int DOSEN_TYPE = 1133;
    private static final String TAG = SignInActivity.class.getSimpleName();

    private int userType;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private LinearLayout mLayoutRole,mlayoutLogin,mLayoutHorizontal;
    private RelativeLayout mLayoutBawah;

    private SignInButton mBtnGoogleSignIn;
    private Button mBtnRoleMahasiswa,mBtnRoleDosen,mBtnSignIn,mBtnPilihUlang,mBtnBelumPunyaAkun;
    private TextInputLayout mTilEmail,mTilPassword;
    private EditText mEtEmail,mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mLayoutRole = (LinearLayout) findViewById(R.id.layout_pilih_role);
        mlayoutLogin = (LinearLayout) findViewById(R.id.layout_login);
        mLayoutBawah = (RelativeLayout) findViewById(R.id.layout_bottom_belum_punya_akun);
        mLayoutHorizontal = (LinearLayout) findViewById(R.id.horizontal_line);
        mBtnBelumPunyaAkun = (Button) findViewById(R.id.btn_belum_punya_akun);
        mBtnBelumPunyaAkun.setText(Html.fromHtml("<font color=#9E9E9E>Sudah punya akun? </font> <font color=#2196F3>masuk disni!</font>"));
        mBtnGoogleSignIn = (SignInButton) findViewById(R.id.google_sign_in_mahasiswa);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnRoleDosen = (Button) findViewById(R.id.btn_pilih_dosen);
        mBtnRoleMahasiswa = (Button) findViewById(R.id.btn_pilih_mahasiswa);
        mBtnPilihUlang = (Button) findViewById(R.id.btn_pilih_ulang_role);
        mTilEmail = (TextInputLayout) findViewById(R.id.til_login_email);
        mTilPassword = (TextInputLayout) findViewById(R.id.til_login_password);
        mEtEmail = (EditText) findViewById(R.id.et_login_email);
        mEtPassword = (EditText) findViewById(R.id.et_login_password);

        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mEtEmail.addTextChangedListener(new TextWatcherSignIn(mEtEmail));
        mEtPassword.addTextChangedListener(new TextWatcherSignIn(mEtPassword));

        mBtnGoogleSignIn.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mBtnRoleDosen.setOnClickListener(this);
        mBtnRoleMahasiswa.setOnClickListener(this);
        mBtnPilihUlang.setOnClickListener(this);

    }



    private void signInMahasiswaWithGoogle(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_SIGN_IN_GOOGLE);
    }

    //user memilih sebagai dosen, ubah tipe user
    private void sebagaiDosen(){
        userType = DOSEN_TYPE;
        setLayout(userType);

    }

    //user memilih sebagai mahasiswa, ubah tipe user
    private void sebagaiMahasiswa(){
        userType = MAHASISWA_TYPE;
        setLayout(userType);
    }

    //ubah layout login sesuai dengan pilihan user
    private void setLayout(int UserType){
        switch (UserType){
            case DOSEN_TYPE:
                showLoginDosenLayout(true);
                break;
            case MAHASISWA_TYPE:
                showLoginMahasiswaLayout(true);
                break;
        }
    }

    //menampilkan layout login untuk dosen
    private void showLoginDosenLayout(boolean e){
        mBtnPilihUlang.setText("Bukan Dosen? Tekan disini!");
        if(e) {
            mBtnPilihUlang.setVisibility(View.VISIBLE);
            mLayoutRole.setVisibility(View.GONE);
            mlayoutLogin.setVisibility(View.VISIBLE);
            mLayoutHorizontal.setVisibility(View.GONE);
            mBtnGoogleSignIn.setVisibility(View.GONE);
        }else{
            mBtnPilihUlang.setVisibility(View.GONE);
            mLayoutRole.setVisibility(View.VISIBLE);
            mlayoutLogin.setVisibility(View.GONE);
            mLayoutHorizontal.setVisibility(View.VISIBLE);
            mBtnGoogleSignIn.setVisibility(View.VISIBLE);
        }
    }

    //menampilkan layout login untuk mahasiswa
    private void showLoginMahasiswaLayout(boolean e){
        mBtnPilihUlang.setText("Bukan Mahasiswa? Tekan disini!");
        if(e) {
            mBtnPilihUlang.setVisibility(View.VISIBLE);
            mLayoutRole.setVisibility(View.GONE);
            mlayoutLogin.setVisibility(View.VISIBLE);
            mLayoutBawah.setVisibility(View.VISIBLE);
        }else{
            mBtnPilihUlang.setVisibility(View.GONE);
            mLayoutRole.setVisibility(View.VISIBLE);
            mlayoutLogin.setVisibility(View.GONE);
            mLayoutBawah.setVisibility(View.GONE);
        }
    }

    //sign in email sesuai dengan tipe user
    private void signIn(){
        showProgressDialog();
        switch (userType){
            case DOSEN_TYPE:
                signInWithEmail(DOSEN_TYPE);
                break;
            case MAHASISWA_TYPE:
                signInWithEmail(MAHASISWA_TYPE);
                break;
        }
    }

    //sign in email plus checking data user di database untuk validasi dan workflow selanjutnya
    private void signInWithEmail(final int type){
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            switch (type) {
                                case DOSEN_TYPE:
                                    mDatabaseRef.child("users").child("dosen").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Dosen dosen = dataSnapshot.getValue(Dosen.class);
                                            if(dosen != null){
                                                startActivity(new Intent(SignInActivity.this,DosenDrawerActivity.class));
                                                finish();
                                            }else{
                                                Intent i = new Intent(SignInActivity.this, EditProfilDosen.class);
                                                i.putExtra(BaseActivity.EXTRA_FROM, FROM_SIGNIN);
                                                startActivity(i);
                                                finish();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    break;
                                case MAHASISWA_TYPE:
                                    mDatabaseRef.child("users").child("mahasiswa").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Mahasiswa m = dataSnapshot.getValue(Mahasiswa.class);
                                            if(m != null){
                                                startActivity(new Intent(SignInActivity.this,MahasiswaDrawerActivity.class));
                                                finish();
                                            }else {
                                                Intent a = new Intent(SignInActivity.this, EditProfilMahasiswa.class);
                                                a.putExtra(BaseActivity.EXTRA_FROM, FROM_SIGNIN);
                                                startActivity(a);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    break;
                            }
                        }else{
                            Log.e("Error with sign in : ",task.getException().toString());
                        }
                    }
                });
    }

    //sign in google plus khusus user tipe mahasiswa dan checking data user di database
    private void AuthGooglePlus(final GoogleSignInAccount acc){
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.e(TAG,"error dengan credential");
                        } else {
                            mDatabaseRef.child("users").child("mahasiswa").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Mahasiswa m = dataSnapshot.getValue(Mahasiswa.class);
                                    if(m != null){
                                        startActivity(new Intent(SignInActivity.this,MahasiswaDrawerActivity.class));
                                        finish();
                                    }else {
                                        Intent a = new Intent(SignInActivity.this, EditProfilMahasiswa.class);
                                        a.putExtra(BaseActivity.EXTRA_FROM, FROM_SIGNIN);
                                        startActivity(a);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    public void requestFocus(View v){
        if(v.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validasiEmail(){
        if(mEtEmail.getText().toString().trim().isEmpty()){
            mTilPassword.setError("Email tidak boleh kosong!");
            requestFocus(mEtEmail);
            return false;
        }else{
            mTilEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validasiPassword(){
        if(mEtPassword.getText().toString().trim().isEmpty()){
            mTilPassword.setError("Password tidak boleh kosong!");
            requestFocus(mEtPassword);
            return false;
        }else{
            mTilPassword.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateAll(){
        if(validasiEmail()&&validasiPassword()){
            return true;
        }
        return false;
    }


    private void pilihUlangRole(){
        switch (userType){
            case DOSEN_TYPE:
                showLoginDosenLayout(false);
                break;
            case MAHASISWA_TYPE:
                showLoginMahasiswaLayout(false);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseAuth.getCurrentUser() != null){
            showProgressDialog();
            mDatabaseRef.child("users").child("dosen").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(getUid())){
                        hideProgressDialog();
                        startActivity(new Intent(SignInActivity.this,DosenDrawerActivity.class));
                        finish();
                    }else{
                        mDatabaseRef.child("users").child("mahasiswa").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(getUid())){
                                    hideProgressDialog();
                                    startActivity(new Intent(SignInActivity.this,MahasiswaDrawerActivity.class));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount acc = result.getSignInAccount();
                AuthGooglePlus(acc);
            } else {
                Log.e(TAG,"ada error di sign in pake google");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_sign_in_mahasiswa:
                signInMahasiswaWithGoogle();
                break;
            case R.id.btn_pilih_dosen:
                sebagaiDosen();
                break;
            case R.id.btn_pilih_mahasiswa:
                sebagaiMahasiswa();
                break;
            case R.id.btn_sign_in:
                if(validateAll()) {
                    signIn();
                }
                break;
            case R.id.btn_pilih_ulang_role:
                pilihUlangRole();
                break;
        }
    }

    class TextWatcherSignIn implements TextWatcher{

        private View view;

        public TextWatcherSignIn(View v){
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.et_login_email:
                    validasiEmail();
                    break;
                case R.id.et_login_password:
                    validasiPassword();
                    break;
            }
        }
    }
}
