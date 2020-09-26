package com.example.voiceme.presenter;

import android.app.Activity;

import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.activity.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;

public class HomePresenter {

    Presenter view;
    String currentUserID;
    DocumentReference rootRef;


    public HomePresenter(final Presenter view) {
        this.view = view;
    }

//    public void verifyUserExistance() {
//        currentUserID = Firebase.auth().getCurrentUser().getUid();
//        rootRef = Firebase.DataBase.user().document(currentUserID);
//        rootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!snapshot.child("name").exists())            {
//                    Helper.nextPage((Activity)view, new SignUpActivity());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public interface Presenter{}
}
