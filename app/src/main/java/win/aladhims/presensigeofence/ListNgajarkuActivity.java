package win.aladhims.presensigeofence;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.ViewHolder.ListNgajarkuViewHolder;

public class ListNgajarkuActivity extends BaseActivity {

    private static final String TAG = ListNgajarkuActivity.class.getSimpleName();
    public static final String EXTRA_FROM_LISTNGAJARKU = "LISTNGAJARKU";
    public static final String VALUE_FROM_LISTNGAJARKU = "VALLISTNGAJARRKU";

    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Ngajar,ListNgajarkuViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabNewNgajar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ngajarku);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_ngajarku);
        mFabNewNgajar = (FloatingActionButton) findViewById(R.id.fab_tambah_ngajar_baru);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("dosen-ngajar").child(getUid());

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);


        mAdapter = new FirebaseRecyclerAdapter<Ngajar, ListNgajarkuViewHolder>
                (Ngajar.class,R.layout.list_ngajarku_item,ListNgajarkuViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(ListNgajarkuViewHolder viewHolder, Ngajar model, int position) {
                final DatabaseReference ngajarRef = getRef(position);

                final String key = ngajarRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getApplicationContext(),MulaiNgajarActivity.class);
                        i.putExtra("EXTRA_NGAJAR",key);
                        startActivity(i);
                    }
                });

                viewHolder.mTvNamaMatkul.setText(model.getNamaMatkul());
                viewHolder.mTvNumsStar.setText(String.valueOf(model.getJumlahStar()));
                viewHolder.mTvWaktu.setText(model.getWaktu());
                viewHolder.mTvKelas.setText(model.getKelasDiajar());
                viewHolder.mTvDurasi.setText(model.getDurasiNgajar());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mFabNewNgajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListNgajarkuActivity.this,NewNgajarActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.cleanup();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_ngajarku_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sign_out_dosen_list_ngajarku:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ListNgajarkuActivity.this,SignInActivity.class));
                finish();
                break;
            case R.id.menu_to_edit_profil_dosen_list_ngajarku:
                Intent i = new Intent(ListNgajarkuActivity.this,EditProfilDosen.class);
                i.putExtra(EXTRA_FROM_LISTNGAJARKU,VALUE_FROM_LISTNGAJARKU);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
