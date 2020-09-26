package com.example.voiceme.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.voiceme.R;
import com.example.voiceme.presenter.SplashScreenPresenter;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenPresenter.Presenter{

    private SplashScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
    }

    private void initialize() {
        presenter = new SplashScreenPresenter(this);
    }
}