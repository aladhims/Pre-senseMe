package win.aladhims.presensigeofence.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.presensigeofence.R;

/**
 * Created by Aladhims on 07/03/2017.
 */

public class ListMahasiswaPresentViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView mCiMahasiswa;
    public TextView mTvNamaMahasiswa,mTvNPMMahasiswa,mTvTotalHadir;
    public CheckBox mCbValidasi;

    public ListMahasiswaPresentViewHolder(View itemView) {
        super(itemView);

        mCiMahasiswa = (CircleImageView)itemView.findViewById(R.id.foto_mahasiswa_presen);
        mTvNamaMahasiswa = (TextView) itemView.findViewById(R.id.nama_mahasiswa_presen);
        mTvNPMMahasiswa = (TextView) itemView.findViewById(R.id.npm_mahasiswa_presen);
        mTvTotalHadir = (TextView) itemView.findViewById(R.id.jumlah_presensi_mahasiswa_presen);
        mCbValidasi = (CheckBox) itemView.findViewById(R.id.cb_validasi_presen);
    }
}
