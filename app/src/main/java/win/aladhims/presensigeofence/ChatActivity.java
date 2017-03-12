package win.aladhims.presensigeofence;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import win.aladhims.presensigeofence.Model.Chat;
import win.aladhims.presensigeofence.Model.Dosen;
import win.aladhims.presensigeofence.Model.Mahasiswa;
import win.aladhims.presensigeofence.ViewHolder.ChatViewHolder;

public class ChatActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<Chat,ChatViewHolder> mChatAdapter;
    private DatabaseReference rootRef,mChatRef;

    private String viewerUid, viewedUid, viewedName;
    private boolean isDosen;

    private EditText mEtPesan;
    private Button mBtnKirimPesan;
    private ImageView mIvAmbilGambar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        viewerUid = i.getStringExtra(DetailProfil.USER_VIEWING_EXTRA);
        viewedUid = i.getStringExtra(DetailProfil.USER_VIEWED_EXTRA);
        isDosen = i.getExtras().getBoolean(DetailProfil.TIPE_USER_VIEWED);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mChatRef = rootRef.child("chat").child(viewerUid).child(viewedUid);

        setBarTitle(isDosen);

        mEtPesan = (EditText) findViewById(R.id.et_chat_message);
        mBtnKirimPesan = (Button) findViewById(R.id.btn_chat_send);
        mIvAmbilGambar = (ImageView) findViewById(R.id.iv_chat_pick_photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat);

        mChatAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(Chat.class,R.layout.chat_item,ChatViewHolder.class,mChatRef) {
            @Override
            protected void populateViewHolder(final ChatViewHolder viewHolder, Chat model, int position) {
                if(model.getFromUid().equals(viewerUid)){
                    viewHolder.mTvNamaUserChat.setVisibility(View.GONE);
                    viewHolder.mCiFotoUser.setVisibility(View.GONE);
                    viewHolder.mBodyPesan.setGravity(Gravity.END);
                    viewHolder.mTvPesanUserChat.setText(model.getPesan());
                }else if(model.getFromUid().equals(viewedUid)){
                    if(isDosen){
                        rootRef.child("users").child("dosen").child(viewedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Dosen dosen = dataSnapshot.getValue(Dosen.class);
                                Glide.with(getApplicationContext())
                                        .load(dosen.getPhotoUrl())
                                        .into(viewHolder.mCiFotoUser);

                                viewHolder.mTvNamaUserChat.setText(dosen.getNama());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        viewHolder.mTvPesanUserChat.setText(model.getPesan());
                    }else {
                        rootRef.child("users").child("mahasiswa").child(viewedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                                Glide.with(getApplicationContext())
                                        .load(mahasiswa.getPhotoUrl())
                                        .into(viewHolder.mCiFotoUser);

                                viewHolder.mTvNamaUserChat.setText(mahasiswa.getNama());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        viewHolder.mTvPesanUserChat.setText(model.getPesan());
                    }
                }
            }
        };

        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        lm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mChatAdapter);

        mBtnKirimPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chatKey = mChatRef.push().getKey();
                Chat chat = new Chat(viewerUid,viewedUid,mEtPesan.getText().toString());
                mChatRef.child(chatKey).setValue(chat);
                rootRef.child("chat").child(viewedUid).child(viewerUid).child(chatKey).setValue(chat);
                mEtPesan.setText("");
            }
        });
    }

    private void setBarTitle(boolean dosen){
        if(dosen){
            rootRef.child("users").child("dosen").child(viewedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Dosen dosen = dataSnapshot.getValue(Dosen.class);
                    viewedName = dosen.getNama();
                    getSupportActionBar().setTitle(viewedName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            rootRef.child("users").child("mahasiswa").child(viewedUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Mahasiswa mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                    getSupportActionBar().setTitle(mahasiswa.getNama());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
