package wssj.co.jp.olioa.screens.chatrealtime.chatdeatail;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.firebaseChat.StoreFirebase;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.chatrealtime.adapter.ChatRealTimeAdapter;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by thang on 1/22/2018.
 */

public class ChatRealTimeFragment extends BaseFragment<IChatRealTimeView, ChatRealTimePresenter> implements IChatRealTimeView {

    private static String TAG = "ChatRealTimeFagment";

    public static final String KEY_STORE_ID = "store_id";

    public static final String KEY_STORE_NAME = "store_name";

    public static final String KEY_IMAGE_STORE = "image_store";

    public static final String KEY_USER_ID = "user_id";

    private DatabaseReference myDatabaseReference;

    private TextView txtNoItem;

    private ListView listView;

    private List<StoreFirebase> listStoreFirebases;

    private ChatRealTimeAdapter chatRealTimeAdapter;

    private long idUser;

    public static ChatRealTimeFragment newInstance(Bundle b) {
        ChatRealTimeFragment fragment = new ChatRealTimeFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHAT_REALTIME;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_chat_realtime;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_chat;
    }

    @Override
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    protected ChatRealTimePresenter onCreatePresenter(IChatRealTimeView view) {
        return new ChatRealTimePresenter(view);
    }

    @Override
    protected IChatRealTimeView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        txtNoItem = (TextView) rootView.findViewById(R.id.textNoItem);
        listView = (ListView) rootView.findViewById(R.id.listViewStore);
    }

    @Override
    public String getAppBarTitle() {
        return "チェット";
    }

    @Override
    protected void initData() {
        super.initData();
        showProgress();
        myDatabaseReference = FirebaseDatabase.getInstance().getReference();
        getPresenter().getUserProfile();
    }

    @Override
    protected void initAction() {
        super.initAction();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (idUser != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_STORE_NAME, listStoreFirebases.get(position).getName());
                    bundle.putString(KEY_STORE_ID, listStoreFirebases.get(position).getId_store());
                    bundle.putString(KEY_IMAGE_STORE, listStoreFirebases.get(position).getLogo());
                    bundle.putLong(KEY_USER_ID, idUser);
                    getActivityCallback().displayScreen(IMainView.FRAGMENT_CONVERSATION, false, true, bundle);
                }
            }
        });
    }

    @Override
    public void getUserIdSucess(long userId) {
        Logger.d("IDDDD", userId + "");

        getStoreFollow(userId);
        hideProgress();
        idUser = userId;
    }

    @Override
    public void getUserIdErro(ErrorMessage errorMessage) {
    }

    private void getStoreFollow(final long id) {
        myDatabaseReference.child("Chat").child("Conversations").child("user_" + id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listStoreFirebases = new ArrayList<StoreFirebase>();
                for (DataSnapshot store : dataSnapshot.getChildren()) {
                    StoreFirebase storeFirebase = new StoreFirebase(store);
                    listStoreFirebases.add(storeFirebase);
                }
                chatRealTimeAdapter = new ChatRealTimeAdapter(getActivityContext(), R.layout.item_store_follow, listStoreFirebases);
                listView.setAdapter(chatRealTimeAdapter);
                chatRealTimeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
