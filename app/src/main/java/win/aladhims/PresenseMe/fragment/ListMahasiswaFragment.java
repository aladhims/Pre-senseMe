package win.aladhims.PresenseMe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import win.aladhims.PresenseMe.DetailProfil;
import win.aladhims.PresenseMe.Model.Mahasiswa;
import win.aladhims.PresenseMe.R;
import win.aladhims.PresenseMe.ViewHolder.ListMahasiswaViewHolder;


public class ListMahasiswaFragment extends Fragment {

    public static final String EXTRA_MAHASISWA_PROFIL = "MAHASISWA";

    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Mahasiswa,ListMahasiswaViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    public ListMahasiswaFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_mahasiswa,container,false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("mahasiswa");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_mahasiswa);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pg_list_mahasiswa);


        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);

        mAdapter = new FirebaseRecyclerAdapter<Mahasiswa, ListMahasiswaViewHolder>
                (Mahasiswa.class,R.layout.list_mahasiswa_item,ListMahasiswaViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(ListMahasiswaViewHolder viewHolder, Mahasiswa model, int position) {
                final String key = getRef(position).getKey();
                if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(key)) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), DetailProfil.class);
                            i.putExtra(EXTRA_MAHASISWA_PROFIL, key);
                            startActivity(i);
                        }
                    });
                }
                mProgressBar.setVisibility(View.GONE);
                Glide.with(getActivity())
                        .load(model.getPhotoUrl())
                        .into(viewHolder.mCiMahasiswa);
                viewHolder.mTvNamaMahasiswa.setText(model.getNama());
                viewHolder.mTvNPMMahasiswa.setText(model.getNPM());
                viewHolder.mTvKelasMahasiswa.setText(model.getKelas());
            }
        };
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                lm.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
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

}
