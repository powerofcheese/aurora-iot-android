package com.happylrd.aurora;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.happylrd.aurora.entity.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    // need to be replaced with your application id
    private static final String BMOB_APPLICATION_ID = "51343ffcee8d4b3363acc1503b77ce09";

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

        til_user_name = (TextInputLayout) findViewById(R.id.til_user_name);
        til_password = (TextInputLayout) findViewById(R.id.til_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = til_user_name.getEditText().getText().toString();
                String password = til_password.getEditText().getText().toString();
                doLogin(username, password);
            }
        });

        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);

        btn_goto_signup = (Button) findViewById(R.id.btn_goto_signup);
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
                    Log.i("BMOB",e.getMessage());
                    til_user_name.setError("用户名或密码错误");
                    til_password.setError("用户名或密码错误");
                    loginFailInfo();
                }
            }
        });
    }

    private void startProgressDialog() {
        progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog.dismiss();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }, 2000);
    }

    public void loginSuccessInfo(){
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    public void loginFailInfo(){
        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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
