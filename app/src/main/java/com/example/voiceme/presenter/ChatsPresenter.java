package com.example.voiceme.presenter;

import android.content.Context;
import android.view.View;

import com.example.voiceme.Firebase;
import com.example.voiceme.adapter.UserRecycleViewAdapter;
import com.example.voiceme.model.UserModel;
import com.example.voiceme.view.fragment.ChatsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.recyclerview.widget.RecyclerView;

public class ChatsPresenter {

    private Context mContext;
    private UserRecycleViewAdapter userAdapter;
    private List<UserModel> mChatUsers;
    private String currentUserId;
    private List<String> friendsList;
    private RecyclerView myRecycleView;

    public ChatsPresenter(final Context mContext, final RecyclerView myRecycleView) {
        this.mContext = mContext;
        this.myRecycleView = myRecycleView;
        friendsList = new ArrayList<>();
        String currentUserId = Firebase.currentUser().getUid();
        Firebase.DataBase.user().document(currentUserId).collection("chat")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        friendsList.clear();
                        for(DocumentSnapshot querySnapshot: queryDocumentSnapshots){
                            String friendId = querySnapshot.getId();
                            friendsList.add(friendId);
                        }
                        Set<String> hashSet = new HashSet<String>(friendsList);
                        friendsList.clear();
                        friendsList.addAll(hashSet);
                        readChats();
                    }
                });
    }

    private void readChats() {
        mChatUsers = new ArrayList<>();
        Firebase.DataBase.user()
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot querySnapshot: queryDocumentSnapshots){
                            UserModel user = new UserModel(querySnapshot.getString("id"), querySnapshot.getString("userName"),
                                    querySnapshot.getString("phoneNumber"));
                            for(String id: friendsList){
                                assert user != null;
                                if(id.equals(user.getId())) mChatUsers.add(user);
                            }
                        }
                        userAdapter = new UserRecycleViewAdapter(mContext, mChatUsers);
                        myRecycleView.setAdapter(userAdapter);
                    }
                });
    }
}
