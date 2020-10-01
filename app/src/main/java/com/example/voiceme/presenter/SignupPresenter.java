package com.example.voiceme.presenter;

import android.app.Activity;

import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.activity.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class SignupPresenter {

    private String currentUserID;
    private String userName;
    private DocumentReference rootRef;

    private Presenter v;

    public SignupPresenter(Presenter v) {
        this.v = v;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void restoreUsername() {
        currentUserID = Firebase.auth().getCurrentUser().getUid();
        rootRef = Firebase.DataBase.user().document(currentUserID);
        Map<String, String> data = new HashMap<>();
        data.put("userName", userName);
        rootRef.set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Helper.nextPage((Activity)v, new HomeActivity());
                }
            }
        });
    }

    public interface Presenter{}
}