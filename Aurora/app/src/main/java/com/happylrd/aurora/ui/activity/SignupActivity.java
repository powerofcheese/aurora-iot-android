package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.happylrd.aurora.R;
import com.happylrd.aurora.model.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignupActivity extends AppCompatActivity {

    private Button btn_signup;
    private TextInputLayout til_user_name;
    private TextInputLayout til_password;
    private Button btn_goto_login;
    private MyUser myUser;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
        initListener();
    }

    private void initView() {
        til_user_name = (TextInputLayout) findViewById(R.id.til_user_name);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_goto_login = (Button) findViewById(R.id.btn_goto_login);
    }

    private void initListener() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = til_user_name.getEditText().getText().toString();
                String password = til_password.getEditText().getText().toString();

                doSignUp(username, password);
            }
        });

        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLogin();
            }
        });
    }

    private void doSignUp(String username, String password) {
        myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setMobilePhoneNumber(username);
        myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    GoToLogin();
                } else {
                    Toast.makeText(SignupActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GoToLogin() {
        Intent intent = LoginActivity.newIntent(SignupActivity.this);
        startActivity(intent);
    }
}
