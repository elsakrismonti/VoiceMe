package com.example.voiceme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {
    public static FirebaseAuth auth(){return FirebaseAuth.getInstance();}
    public static FirebaseUser currenntUser(){return auth().getCurrentUser();}
    public static class DataBase{
        static FirebaseDatabase db(){return FirebaseDatabase.getInstance();}
        public static DatabaseReference user(){return db().getReference("users");}
        public static DatabaseReference user(String phoneNumber){return user().child(phoneNumber);}
    }

}

