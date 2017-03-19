package win.aladhims.PresenseMe;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.PresenseMe.Model.Mahasiswa;
import win.aladhims.PresenseMe.fragment.ListDosenFragment;
import win.aladhims.PresenseMe.fragment.ListMahasiswaFragment;
import win.aladhims.PresenseMe.fragment.ListNgajarFragment;
import win.aladhims.PresenseMe.fragment.ListNgajarKuFragment;

public class MahasiswaDrawerActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {


    public static final String EXTRA_FROM_MAHASISWADRAWER = "EXTRADRAWER";
    public static final String VALUE_FROM_MAHASISWADRAWER = "VALUEDRAWER";

    private static int navItemIndex = 0;

    private static final String TAG_LISTNGAJAR = "listngajar";
    private static final String TAG_LISTMAHASISWA = "listmahasiswa";
    private static final String TAG_LISTDOSEN = "listdosen";

    public static String CURRENT_TAG = TAG_LISTNGAJAR;

    private DatabaseReference mRootRef;
    private GoogleApiClient mGoogleApiClient;

    private String[] judulActivity;
    private Handler mHandler;

    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private View mNavHeader;
    private CircleImageView mCiUserFoto;
    private TextView mTvUserName,mTvUserID;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_mahasiswa_main_layout);

        toolbar = (Toolbar) findViewById(R.id.mahasiswa_toolbar);
        setSupportActionBar(toolbar);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mHandler = new Handler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_mahasiswa_layout_main);
        mNavView = (NavigationView) findViewById(R.id.nav_mahasiswa_view);

        mNavHeader = mNavView.getHeaderView(0);
        mTvUserName = (TextView) mNavHeader.findViewById(R.id.tv_drawer_header_name);
        mTvUserID = (TextView) mNavHeader.findViewById(R.id.tv_drawer_header_id);
        mCiUserFoto = (CircleImageView) mNavHeader.findViewById(R.id.ci_drawer_header_foto);

        judulActivity = getResources().getStringArray(R.array.nav_item_activity_titles_mahasiswa);

        loadNavHeader();

        setUpNavigationView();

        if(savedInstanceState == null){
            navItemIndex = 0;
            CURRENT_TAG = TAG_LISTNGAJAR;
            loadFragment();
        }
    }

    private void loadNavHeader(){
        mRootRef.child("users").child("mahasiswa").child(getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                        Glide.with(getApplicationContext())
                                .load(mahasiswa.getPhotoUrl())
                                .into(mCiUserFoto);
                        mTvUserName.setText(mahasiswa.getNama());
                        mTvUserID.setText(mahasiswa.getNPM());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void loadFragment(){

        selectNavMenu();

        setJudulToolBar();

        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG)!=null){
            mDrawerLayout.closeDrawers();


            return;

        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.drawer_mahasiswa_content_frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable != null){
            mHandler.post(mPendingRunnable);
        }



        mDrawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getFragment(){
        switch (navItemIndex){
            case 0:
                ListNgajarFragment listNgajar = new ListNgajarFragment();
                return listNgajar;
            case 1:
                ListMahasiswaFragment listMahasiswa = new ListMahasiswaFragment();
                return listMahasiswa;
            case 2:
                ListDosenFragment listDosen = new ListDosenFragment();
                return listDosen;
            case 3:

            case 4:

            default:
                return new ListNgajarKuFragment();
        }
    }

    private void setJudulToolBar(){
        getSupportActionBar().setTitle(judulActivity[navItemIndex]);
    }

    private void selectNavMenu(){
        mNavView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView(){

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_mahasiswa_drawer_list_ngajar:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_LISTNGAJAR;
                        break;
                    case R.id.menu_mahasiswa_drawer_list_mahasiswa:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_LISTMAHASISWA;
                        break;
                    case R.id.menu_mahasiswa_drawer_list_dosen:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_LISTDOSEN;
                        break;
                    default:
                        navItemIndex = 0;
                        break;
                }

                if(item.isChecked()){
                    item.setChecked(false);
                }else {
                    item.setChecked(true);
                }
                item.setChecked(true);
                loadFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.menu_bar_mahasiswa_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sign_out_mahasiswa:
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(new Intent(this,SignInActivity.class));
                finish();
                break;
            case R.id.menu_to_edit_profil_mahasiswa:
                Intent i = new Intent(this,EditProfilMahasiswa.class);
                i.putExtra(EXTRA_FROM_MAHASISWADRAWER,VALUE_FROM_MAHASISWADRAWER);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
