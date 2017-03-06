package win.aladhims.presensigeofence;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class DetailNgajarActivity extends BaseActivity {

    private TextView mNamaMatkul,mNamaDosenPengajar,mNIPDosenPengajar,mTvDurasi;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ngajar);
    }
}
