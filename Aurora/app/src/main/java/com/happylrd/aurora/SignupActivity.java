package com.happylrd.aurora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.happylrd.aurora.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignupActivity extends AppCompatActivity {

    private Button btn_signup;
    private EditText til_user_name;
    private EditText til_password;
    private MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        til_user_name = (EditText) findViewById(R.id.et_user_name_signup);
        til_password = (EditText) findViewById(R.id.et_password_signup);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aaa",""+ 0);
                String username_ = til_user_name.getText().toString();
                String password_ = til_password.getText().toString();;
                Log.i("aaa",""+ 0.1);
                doSignUp(username_, password_);
            }
        });
    }

    private void doSignUp(String username, String password) {
        Log.i("aaa",""+ 1);
        myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setMobilePhoneNumber(username);
        Log.i("aaa", "" + 2);
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

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        return intent;
    }
}
