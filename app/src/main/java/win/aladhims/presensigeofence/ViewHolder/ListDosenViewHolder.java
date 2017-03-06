package win.aladhims.presensigeofence.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.R;

/**
 * Created by Aladhims on 05/03/2017.
 */

public class ListDosenViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView mCiFotoDosen;
    public TextView mTvNamaDosen,mTvNIPDosen;

    public ListDosenViewHolder(View itemView) {
        super(itemView);

        mCiFotoDosen = (CircleImageView) itemView.findViewById(R.id.foto_dosen_list);
        mTvNamaDosen = (TextView) itemView.findViewById(R.id.nama_dosen_list);
        mTvNIPDosen = (TextView) itemView.findViewById(R.id.nip_dosen_list);
    }
}
