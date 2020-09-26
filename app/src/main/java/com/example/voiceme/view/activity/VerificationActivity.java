package com.example.voiceme.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.voiceme.R;
import com.example.voiceme.presenter.VerificationPresenter;

public class VerificationActivity extends AppCompatActivity implements VerificationPresenter.Presenter {

    private VerificationPresenter presenter;
    EditText eTVerificationCode;
    Toolbar verificationToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initialize();
        setSupportActionBar(verificationToolBar);
        settingActionBar();
    }

    private void initialize() {
        verificationToolBar = findViewById(R.id.verification_toolbar);
        eTVerificationCode = findViewById(R.id.eTVerificationCode);
        presenter = new VerificationPresenter(this);
    }

    private void settingActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Verification");
    }

    public void verificationAction(View view) {
        presenter.setSentCode(eTVerificationCode.getText().toString());
        presenter.signIn();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}