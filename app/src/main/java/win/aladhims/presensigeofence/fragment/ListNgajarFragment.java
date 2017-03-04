package win.aladhims.presensigeofence.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.MulaiNgajarActivity;
import win.aladhims.presensigeofence.NewNgajarActivity;
import win.aladhims.presensigeofence.R;
import win.aladhims.presensigeofence.ViewHolder.ListNgajarkuViewHolder;

import static android.support.v7.recyclerview.R.attr.layoutManager;


public class ListNgajarFragment extends Fragment {


    public ListNgajarFragment() {
        // Required empty public constructor
    }
    private static final String TAG = ListNgajarFragment.class.getSimpleName();

    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Ngajar,ListNgajarkuViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabNewNgajar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_ngajarku,container,false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_ngajarku);
        mFabNewNgajar = (FloatingActionButton) rootView.findViewById(R.id.fab_tambah_ngajar_baru);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("dosen-ngajar").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
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

                        Intent i = new Intent(getActivity(),MulaiNgajarActivity.class);
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                lm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mFabNewNgajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NewNgajarActivity.class));
            }
        });

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
