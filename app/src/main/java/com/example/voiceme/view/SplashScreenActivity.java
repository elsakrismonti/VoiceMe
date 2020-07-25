package com.example.voiceme.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.voiceme.R;
import com.example.voiceme.presenter.SplashScreenPresenter;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenPresenter.Presenter {
    private SplashScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        presenter = new SplashScreenPresenter(this);
    }
}