package win.aladhims.PresenseMe.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.PresenseMe.R;

/**
 * Created by Aladhims on 04/03/2017.
 */

public class ListMahasiswaViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView mCiMahasiswa;
    public TextView mTvNamaMahasiswa,mTvNPMMahasiswa,mTvKelasMahasiswa;

    public ListMahasiswaViewHolder(View itemView) {
        super(itemView);

        mCiMahasiswa = (CircleImageView)itemView.findViewById(R.id.foto_mahasiswa_list);
        mTvNamaMahasiswa = (TextView) itemView.findViewById(R.id.nama_mahasiswa_list);
        mTvNPMMahasiswa = (TextView) itemView.findViewById(R.id.npm_mahasiswa_list);
        mTvKelasMahasiswa = (TextView) itemView.findViewById(R.id.kelas_mahasiswa_list);
    }
}
