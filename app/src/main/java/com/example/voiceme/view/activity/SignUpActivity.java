package com.example.voiceme.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.voiceme.R;
import com.example.voiceme.presenter.SignupPresenter;

public class SignUpActivity extends AppCompatActivity implements SignupPresenter.Presenter {

    private SignupPresenter presenter;
    private EditText eTUserName;
    Toolbar signUpToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
        settingActionBar();
    }

    private void initialize(){
        eTUserName = findViewById(R.id.eTUserName);
        signUpToolBar = findViewById(R.id.signup_toolbar);
        presenter = new SignupPresenter(this);
    }

    private void settingActionBar() {
        setSupportActionBar(signUpToolBar);
        getSupportActionBar().setTitle("Register");
    }

    public void signUp(View view) {
        presenter.setUserName(eTUserName.getText().toString());
        presenter.restoreUsername();
    }
}