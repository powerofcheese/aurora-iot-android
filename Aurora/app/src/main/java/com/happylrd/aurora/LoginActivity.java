package com.happylrd.aurora;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private static final String TEST_USER_NAME_STR = "123456";
    private static final String TEST_PASSWORD_STR = "666666";

    private Button btn_login;
    private Button btn_forget_password;
    private Button btn_goto_signup;
    private TextInputLayout til_user_name;
    private TextInputLayout til_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        til_user_name = (TextInputLayout) findViewById(R.id.til_user_name);
        til_password = (TextInputLayout) findViewById(R.id.til_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = til_user_name.getEditText().getText().toString();
                String password = til_password.getEditText().getText().toString();

                if (!validateUsername(username)) {
                    til_user_name.setError("用户名错误");
                    til_password.setErrorEnabled(false);
                } else if (!validatePassword(password)) {
                    til_password.setError("密码错误");
                    til_user_name.setErrorEnabled(false);
                } else {
                    til_user_name.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                    doLogin();
                }
            }
        });

        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);

        btn_goto_signup = (Button) findViewById(R.id.btn_goto_signup);
        btn_goto_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignup();
            }
        });
    }

    private boolean validateUsername(String username) {
        if (username == null) {
            return false;
        }

        if (username.equals(TEST_USER_NAME_STR)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }

        if (password.equals(TEST_PASSWORD_STR)) {
            return true;
        } else {
            return false;
        }
    }

    private void doLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    private void doSignup(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
