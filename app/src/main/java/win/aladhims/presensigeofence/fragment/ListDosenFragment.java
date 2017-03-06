package win.aladhims.presensigeofence.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Mahasiswa;
import win.aladhims.presensigeofence.R;
import win.aladhims.presensigeofence.ViewHolder.ListDosenViewHolder;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaViewHolder;

/**
 * Created by Aladhims on 05/03/2017.
 */

public class ListDosenFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Dosen,ListDosenViewHolder> mAdapter;
    private RecyclerView mRecyclerView;

    public ListDosenFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_dosen,container,false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("dosen");
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_dosen);



        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(lm);

        mAdapter = new FirebaseRecyclerAdapter<Dosen, ListDosenViewHolder>
                (Dosen.class,R.layout.list_dosen_item,ListDosenViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(ListDosenViewHolder viewHolder, Dosen dosen, int position) {

                Glide.with(getActivity())
                        .load(dosen.getPhotoUrl())
                        .into(viewHolder.mCiFotoDosen);
                viewHolder.mTvNamaDosen.setText(dosen.getNama());
                viewHolder.mTvNIPDosen.setText(dosen.getNIP());
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
