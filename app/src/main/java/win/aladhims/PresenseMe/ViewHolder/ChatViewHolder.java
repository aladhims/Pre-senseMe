package win.aladhims.PresenseMe.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import win.aladhims.PresenseMe.R;

/**
 * Created by Aladhims on 12/03/2017.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView mCiFotoUser;
    public TextView mTvNamaUserChat,mTvPesanUserChat;
    public LinearLayout mBodyPesan;

    public ChatViewHolder(View itemView) {
        super(itemView);

        mCiFotoUser = (CircleImageView) itemView.findViewById(R.id.ci_photo_user_chat);
        mTvNamaUserChat = (TextView) itemView.findViewById(R.id.tv_pengirim_chat);
        mTvPesanUserChat = (TextView) itemView.findViewById(R.id.tv_pesan_chat);
        mBodyPesan = (LinearLayout) itemView.findViewById(R.id.ll_body_pesan);
    }
}
