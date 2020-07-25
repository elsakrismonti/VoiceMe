package com.example.voiceme.presenter;

import android.app.Activity;
import android.os.Handler;

import com.example.voiceme.Firebase;
import com.example.voiceme.Helper;
import com.example.voiceme.view.HomeActivity;
import com.example.voiceme.view.LoginActivity;


public class SplashScreenPresenter {
    private Presenter v;
    public SplashScreenPresenter(final Presenter v) {
        this.v = v;
        //Is user logged in
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Firebase.currenntUser()==null){
                    Helper.nextPage((Activity)v, new LoginActivity());
                }
                else{
                    Helper.nextPage((Activity)v, new HomeActivity());
                }
            }

        }, 3000);
    }
    public interface Presenter{}
}
