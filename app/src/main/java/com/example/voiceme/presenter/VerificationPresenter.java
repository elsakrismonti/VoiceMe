package com.example.voiceme.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.activity.HomeActivity;
import com.example.voiceme.view.activity.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class VerificationPresenter {

    Presenter v;
    private String phoneNumber;
    private String phoneNumberDb;
    private String sentCode;
    private String mVerification;

    String currentUserID;
    CollectionReference rootRef;

    public VerificationPresenter(final Presenter v) {
        this.v = v;
        phoneNumber = ((Activity)v).getIntent().getStringExtra("phoneNumber");
        phoneNumberDb = phoneNumber;
        phoneNumber = "+62" + phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,           // Phone number to verify
                60,                  // Timeout duration
                TimeUnit.SECONDS,      // Unit of timeout
                (Activity)v,           // Activity (for callback binding)
                mCallBacks             // OnVerificationStateChangedCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // Getting the OTP
            sentCode = phoneAuthCredential.getSmsCode();
            Log.d("TAG", "onCreate: "+sentCode);

            if(sentCode!=null){
                signIn();
            }
            else
                signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG", "onCreate: "+e.toString());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerification = s;
        }
    };

    public void setSentCode(String sentCode){
        this.sentCode = sentCode;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        final ProgressDialog dialog = ProgressDialog.show((Context) v, "", "Sedang Memuat", true);
        Firebase.auth().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    checkAccount();
                }  else {
                    // Sign in failed
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Log.e("TAG", "Invalid Code");
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    public void signIn(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, sentCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void checkAccount(){
        rootRef = Firebase.DataBase.user();
        rootRef.whereEqualTo("phoneNumber",phoneNumberDb).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        if(phoneNumber.equals(phoneNumberDb)){
                            Helper.nextPage((Activity) v, new HomeActivity());
                        }
                    }
                }
                if(task.getResult().size() == 0){
                    signUp();
                }
            }
        });
    }

    public void signUp() {
        currentUserID = Firebase.auth().getCurrentUser().getUid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", currentUserID);
        hashMap.put("phoneNumber", phoneNumberDb);

        rootRef.document(currentUserID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Helper.nextPage((Activity) v, new SignUpActivity());
            }
        });
    }

    public interface Presenter{}
}
