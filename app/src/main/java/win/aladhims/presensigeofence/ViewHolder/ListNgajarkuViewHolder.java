package win.aladhims.presensigeofence.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import win.aladhims.presensigeofence.Model.Ngajar;
import win.aladhims.presensigeofence.R;

/**
 * Created by Aladhims on 03/03/2017.
 */

public class ListNgajarkuViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvNamaMatkul,mTvNumsStar,mTvWaktu,mTvKelas,mTvDurasi;

    public ListNgajarkuViewHolder(View itemView) {
        super(itemView);
        mTvNamaMatkul = (TextView) itemView.findViewById(R.id.tv_nama_matkul_list_ngajarku);
        mTvNumsStar = (TextView) itemView.findViewById(R.id.post_num_stars_list_ngajarku);
        mTvWaktu = (TextView) itemView.findViewById(R.id.tv_jadwal_ngajar_list_ngajarku);
        mTvKelas = (TextView) itemView.findViewById(R.id.tv_kelas_ngajar_list_ngajarku);
        mTvDurasi = (TextView) itemView.findViewById(R.id.tv_durasi_ngajar_list_ngajarku);

    }

}
