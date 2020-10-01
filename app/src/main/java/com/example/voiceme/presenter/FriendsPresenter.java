package com.example.voiceme.presenter;

import android.content.Context;

import com.example.voiceme.Firebase;
import com.example.voiceme.adapter.UserRecycleViewAdapter;
import com.example.voiceme.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsPresenter {

    private List<UserModel> listFriends;
    Context mContext;

    public FriendsPresenter(final Context mContext, final RecyclerView myRecycleView) {
        this.mContext = mContext;
        listFriends = new ArrayList<>();
        final String currentUserId = Firebase.currentUser().getUid();
        CollectionReference reference = Firebase.DataBase.user();
        reference.orderBy("userName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listFriends.clear();
                        for(DocumentSnapshot querySnapshot: task.getResult()){
                            UserModel user = new UserModel(querySnapshot.getString("id"), querySnapshot.getString("userName"),
                                    querySnapshot.getString("phoneNumber"));
                            if(!user.getId().equals(currentUserId)){
                                listFriends.add(user);
                            }
                        }
                        UserRecycleViewAdapter recycleViewAdapter = new UserRecycleViewAdapter(mContext,listFriends);
                        myRecycleView.setAdapter(recycleViewAdapter);
                    }
                });
    }
}
