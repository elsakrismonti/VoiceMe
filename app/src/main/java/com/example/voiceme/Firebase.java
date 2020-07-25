package com.example.voiceme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    public static FirebaseAuth auth(){return FirebaseAuth.getInstance();}
    public static FirebaseUser currenntUser(){return auth().getCurrentUser();}

}
