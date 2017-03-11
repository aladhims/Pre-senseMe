package win.aladhims.presensigeofence.ViewHolder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.R;

/**
 * Created by Aladhims on 05/03/2017.
 */

public class ListDosenViewHolder extends RecyclerView.ViewHolder {

    public ImageView mIvFotoDosen;
    public TextView mTvNamaDosen,mTvNIPDosen;
    public LinearLayout item;

    public ListDosenViewHolder(View itemView) {
        super(itemView);

        item = (LinearLayout) itemView.findViewById(R.id.linear_per_item_dosen);
        mIvFotoDosen = (ImageView) itemView.findViewById(R.id.iv_dosen_item);
        mTvNamaDosen = (TextView) itemView.findViewById(R.id.tv_nama_dosen_item);
        mTvNIPDosen = (TextView) itemView.findViewById(R.id.tv_nip_dosen_item);
    }
}
