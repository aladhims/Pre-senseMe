package win.aladhims.presensigeofence.ViewHolder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import win.aladhims.presensigeofence.R;

/**
 * Created by Aladhims on 08/03/2017.
 */

public class ListNgajarViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvNamaDosen,mTvEmailDosen,mTvKontakDosen,mTvJumlahBintang,mTvNamaMatkul,mTvWaktuMulai,mTvKelas,mTvDurasi;
    public Button mBtnIkut;
    public ImageView mIvFotoDosen,mIvBintang;

    public ListNgajarViewHolder(View itemView) {
        super(itemView);

        mTvNamaDosen = (TextView) itemView.findViewById(R.id.tv_nama_pengajar);
        mTvEmailDosen = (TextView) itemView.findViewById(R.id.tv_email_dosen);
        mTvKontakDosen = (TextView) itemView.findViewById(R.id.tv_kontak_dosen);
        mTvJumlahBintang = (TextView) itemView.findViewById(R.id.num_stars_list_ngajar);
        mTvNamaMatkul = (TextView) itemView.findViewById(R.id.tv_nama_matkul);
        mTvWaktuMulai = (TextView) itemView.findViewById(R.id.tv_jadwal_ngajar);
        mTvKelas = (TextView) itemView.findViewById(R.id.tv_kelas);
        mTvDurasi = (TextView) itemView.findViewById(R.id.tv_durasi_ngajar);

        mBtnIkut = (Button) itemView.findViewById(R.id.btn_ikuti_ngajar);
        mIvFotoDosen = (ImageView) itemView.findViewById(R.id.foto_pengajar);
        mIvBintang = (ImageView) itemView.findViewById(R.id.star_list_ngajar);
    }
}
