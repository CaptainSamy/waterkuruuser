package jp.co.wssj.iungo.screens.chatrealtime.conversations;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.firebaseChat.MessagesFirebase;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.chatrealtime.adapter.ConversationsAdapter;
import jp.co.wssj.iungo.screens.chatrealtime.chatdeatail.ChatRealTimeFragment;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.widget.swipyrefreshlayout.SwipyRefreshLayout;
import jp.co.wssj.iungo.widget.swipyrefreshlayout.SwipyRefreshLayoutDirection;

/**
 * Created by thang on 1/30/2018.
 */

public class ConversationsFragment extends BaseFragment<IConversationsView, ConversationsPresenter> implements IConversationsView {
    private static String TAG = "ConversationsFragment";
    // private SwipyRefreshLayout mRefreshListChat;
    private ListView mListViewChat;
    private EditText mInputChat;
    private ImageView mButtonSend;
    private ProgressBar mProgressSendChat;
    public static final String KEY_STORE_ID = "store_id";
    public static final String KEY_STORE_NAME = "store_name";
    public static final String KEY_IMAGE_STORE = "image_store";
    public static final String KEY_USER_ID = "user_id";
    private String idStore;
    private String imageStore;
    private String nameStore;
    private long idUser;
    private DatabaseReference myDatabaseReference;
    private ConversationsAdapter conversationsAdapter;
    private List<MessagesFirebase> listMessagesFirebase;
//    private String dateText;
//    private String time;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CONVERSATION;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_conversations;
    }

    public static ConversationsFragment newInstance(Bundle b) {
        ConversationsFragment fragment = new ConversationsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected ConversationsPresenter onCreatePresenter(IConversationsView view) {
        return new ConversationsPresenter(view);
    }

    @Override
    protected IConversationsView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        String storeName = getString(R.string.title_screen_chat);
        if (getArguments() != null) {
            storeName = getArguments().getString(KEY_STORE_NAME);
        }
        return storeName;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
//        mRefreshListChat = (SwipyRefreshLayout) rootView.findViewById(R.id.refreshHistoryChat);
//        mRefreshListChat.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mListViewChat = (ListView) rootView.findViewById(R.id.lvStoreFollow);
        mInputChat = (EditText) rootView.findViewById(R.id.etChat);
        mButtonSend = (ImageView) rootView.findViewById(R.id.tvSendChat);
        mProgressSendChat = (ProgressBar) rootView.findViewById(R.id.progressSendChat);
    }

    @Override
    protected void initData() {
        super.initData();
        myDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (getArguments() != null) {
            idStore = getArguments().getString(KEY_STORE_ID);
            nameStore = getArguments().getString(KEY_STORE_NAME);
            imageStore = getArguments().getString(KEY_IMAGE_STORE);
            idUser = getArguments().getLong(KEY_USER_ID);
            Logger.d("thangss", idStore + " " + nameStore + " " + imageStore + " " + idUser);
        }

        myDatabaseReference.child("Chat").child("Groups").child("group_user_" + idUser + "_" + idStore).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.d("datasnap", dataSnapshot.toString());
                listMessagesFirebase = new ArrayList<MessagesFirebase>();
                MessagesFirebase messagesFirebase;
                for (DataSnapshot item : dataSnapshot.getChildren()) {

                    String content = (String) item.child("content").getValue();
                    long created = (long) item.child("created").getValue();
                    boolean deleted = (boolean) item.child("deleted").getValue();
                    String from = item.child("from").getValue().toString();
                    String type = item.child("type").getValue().toString();
                    boolean is_store = (boolean) item.child("is_store").getValue();

                    if (!TextUtils.isEmpty(content) && created != 0) {
                        Date date = new Date(created);
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                        String dateText = df2.format(date);
                        String time = date.getHours() + ":" + date.getMinutes();

                        messagesFirebase = new MessagesFirebase(content, dateText, time);
                        listMessagesFirebase.add(messagesFirebase);
                        conversationsAdapter = new ConversationsAdapter(getActivityContext(), R.layout.item_chat_user, listMessagesFirebase);
                        mListViewChat.setAdapter(conversationsAdapter);
                        conversationsAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void initAction() {
        super.initAction();
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idUser != 0 && !TextUtils.isEmpty(idStore)) {
                    DatabaseReference query = myDatabaseReference.child("Chat").child("Groups").child("group_user_" + idUser + "_" + idStore).child("messages").push();
                    query.child("content").setValue(mInputChat.getText().toString());
                    query.child("created").setValue(System.currentTimeMillis());
                    query.child("deleted").setValue(false);
                    String[] a = imageStore.split("_");
                    query.child("from").setValue(a[1]);
                    query.child("is_store").setValue(true);
                    query.child("type").setValue(0);

                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                    String dateText = df2.format(date);
                    String time = date.getHours() + ":" + date.getMinutes();

                    MessagesFirebase messagesFirebase = new MessagesFirebase(mInputChat.getText().toString(), dateText, time);
                    listMessagesFirebase.add(messagesFirebase);
                    conversationsAdapter.notifyDataSetChanged();

                    mInputChat.setText("");
                }
            }
        });

    }
}
