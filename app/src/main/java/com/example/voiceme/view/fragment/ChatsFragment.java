package com.example.voiceme.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.voiceme.Firebase;
import com.example.voiceme.R;
import com.example.voiceme.adapter.UserRecycleViewAdapter;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatsFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private UserRecycleViewAdapter userAdapter;
    private List<UserModel> mChatUsers;
    FirebaseUser currentUser;
    DatabaseReference reference;
    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats, container, false);

        myRecyclerView =view.findViewById(R.id.chats_recycle);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUser =FirebaseAuth.getInstance().getCurrentUser();

        usersList=new ArrayList<>();

//        reference= Firebase.DataBase.chats();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    ChatModel chat=snapshot.getValue(ChatModel.class);
//                    assert chat != null;
//
//                    if(chat.getSender().equals(currentUser.getUid())){
//                        usersList.add(chat.getRecipient());
//                    }
//                    if(chat.getRecipient().equals(currentUser.getUid())){
//                        usersList.add(chat.getSender());
//                    }
//                }
//
//                Set<String> hashSet = new HashSet<String>(usersList);
//                usersList.clear();
//                usersList.addAll(hashSet);
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return view;
    }

//    private void readChats(){
//        mChatUsers = new ArrayList<>();
//
//        reference=Firebase.DataBase.user();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    UserModel user=snapshot.getValue(UserModel.class);
//
//                    for(String id:usersList){
//                        assert user != null;
//                        if(user.getId().equals(id)){
//                            mChatUsers.add(user);
//                        }
//
//                    }
//                }
//
//                userAdapter=new UserRecycleViewAdapter(getContext(), mChatUsers);
//                myRecyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
