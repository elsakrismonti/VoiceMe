package com.example.voiceme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {
    public static FirebaseAuth auth(){return FirebaseAuth.getInstance();}
    public static void out(){FirebaseAuth.getInstance().signOut();}
    public static FirebaseUser currentUser(){return auth().getCurrentUser();}
    public static class DataBase{
        static FirebaseFirestore db(){return FirebaseFirestore.getInstance();}
        public static CollectionReference user(){return db().collection("Users");}
        public static CollectionReference chatRoom() {return db().collection("chatRoom");}

//        static FirebaseDatabase db(){return FirebaseDatabase.getInstance();}
//        public static DatabaseReference user(){return db().getReference("Users");}
//        public static DatabaseReference chats(){return db().getReference("Chats");}
//        public static DatabaseReference user(String phoneNumber){return user().child(phoneNumber);}
    }

}

