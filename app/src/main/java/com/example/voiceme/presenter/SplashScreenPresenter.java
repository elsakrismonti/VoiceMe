package com.example.voiceme.presenter;

import android.app.Activity;
import android.os.Handler;
import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.activity.HomeActivity;
import com.example.voiceme.view.activity.LoginActivity;


public class SplashScreenPresenter {
    private Presenter v;
    public SplashScreenPresenter(final Presenter v) {
        this.v = v;
        //Is the user logged in?
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Firebase.currentUser() == null){
                    Helper.nextPage((Activity)v, new LoginActivity());

                }
                else{
                    Helper.nextPage((Activity)v, new HomeActivity());
                }
            }
        }, 300);
    }
    public interface Presenter{}
}
