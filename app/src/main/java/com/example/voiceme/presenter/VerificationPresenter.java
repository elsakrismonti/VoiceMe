package com.example.voiceme.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class VerificationPresenter {

    Presenter v;
    private String phoneNumber;
    private String sentCode;
    private String mVerification;
    public VerificationPresenter(final Presenter v) {
        this.v = v;
        phoneNumber = ((Activity)v).getIntent().getStringExtra("phoneNumber");
        Log.d("TAG", "onCreate: "+phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                (Activity)v,
                mCallBacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            sentCode = phoneAuthCredential.getSmsCode();
            if(sentCode!=null){
                signIn();
            }
            else
                signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

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
                    Helper.nextPage((Activity) v, new HomeActivity());
                }
            }
        });
    }
    public void signIn(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, sentCode);
        signInWithPhoneAuthCredential(credential);
    }

    public interface Presenter{}
}
