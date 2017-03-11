package win.aladhims.presensigeofence.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import win.aladhims.presensigeofence.DetailProfil;
import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Mahasiswa;
import win.aladhims.presensigeofence.R;
import win.aladhims.presensigeofence.ViewHolder.ListDosenViewHolder;
import win.aladhims.presensigeofence.ViewHolder.ListMahasiswaViewHolder;

/**
 * Created by Aladhims on 05/03/2017.
 */

public class ListDosenFragment extends Fragment {

    public static final String EXTRA_DOSEN_PROFIL = "DOSEN";

    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Dosen,ListDosenViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


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
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pg_list_dosen);


        GridLayoutManager lm = new GridLayoutManager(getActivity(),2);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(5),true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(lm);

        mAdapter = new FirebaseRecyclerAdapter<Dosen, ListDosenViewHolder>
                (Dosen.class,R.layout.list_dosen_item,ListDosenViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(ListDosenViewHolder viewHolder, Dosen dosen, int position) {
                final String id = getRef(position).getKey();
                if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(id)) {
                    viewHolder.mIvFotoDosen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), DetailProfil.class);
                            i.putExtra(EXTRA_DOSEN_PROFIL, id);
                            startActivity(i);
                        }
                    });
                }
                mProgressBar.setVisibility(View.GONE);
                Glide.with(getActivity())
                        .load(dosen.getPhotoUrl())
                        .into(viewHolder.mIvFotoDosen);
                viewHolder.mTvNamaDosen.setText(dosen.getNama());
                viewHolder.mTvNIPDosen.setText(dosen.getNIP());
            }
        };
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

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
