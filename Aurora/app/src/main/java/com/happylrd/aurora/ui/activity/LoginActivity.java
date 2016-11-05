package com.happylrd.aurora.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.util.ToastUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    // need to be replaced with your application id
    private static final String BMOB_APPLICATION_ID = "8b128823a40bc1c41cb2d9e5540c9172";

    private Button btn_login;
    private Button btn_forget_password;
    private Button btn_goto_signup;
    private TextInputLayout til_user_name;
    private TextInputLayout til_password;

    private ProgressDialog progressDialog;
    private MyUser myUser;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this, BMOB_APPLICATION_ID);
        initView();
        initListener();
    }

    private void initView() {
        til_user_name = (TextInputLayout) findViewById(R.id.til_user_name);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_goto_signup = (Button) findViewById(R.id.btn_goto_signup);
    }

    private void initListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = til_user_name.getEditText().getText().toString();
                String password = til_password.getEditText().getText().toString();

                doLogin(username, password);
            }
        });

        btn_goto_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToSignUp();
            }
        });
    }

    private void doLogin(String username, String password) {
        myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        startProgressDialog();
        myUser.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    til_user_name.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                    loginSuccessInfo();

                    GoToMainActivity();
                } else {
                    til_user_name.setError("用户名或密码错误");
                    til_password.setError("用户名或密码错误");
                    loginFailInfo();
                }
            }
        });
    }

    private void startProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void loginSuccessInfo() {
        ToastUtil.showLoginSuccessToast(LoginActivity.this);
        progressDialog.dismiss();
    }

    public void loginFailInfo() {
        ToastUtil.showLoginFailToast(LoginActivity.this);
        progressDialog.dismiss();
    }

    private void GoToSignUp() {
        Intent intent = SignupActivity.newIntent(LoginActivity.this);
        startActivity(intent);
    }

    private void GoToMainActivity() {
        Intent intent = MainActivity.newIntent(LoginActivity.this);
        startActivity(intent);
    }
}
