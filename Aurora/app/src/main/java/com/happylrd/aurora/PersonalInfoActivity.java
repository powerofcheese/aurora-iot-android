package com.happylrd.aurora;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.happylrd.aurora.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalInfoActivity extends AppCompatActivity {

    private String[] sex_array = new String[]{"男", "女", "未知"};
    private int sex_array_selected_index = 2;

    private Toolbar toolbar;

    private RelativeLayout rl_head_portrait;

    private RelativeLayout rl_nick_name;
    private TextView tv_nick_name;

    private RelativeLayout rl_sex;
    private TextView tv_sex;

    private RelativeLayout rl_age;
    private TextView tv_age;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        rl_nick_name = (RelativeLayout) findViewById(R.id.rl_nick_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        rl_age = (RelativeLayout) findViewById(R.id.rl_age);

        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        tv_nick_name.setText(myUser.getNickName());
        tv_sex.setText(myUser.getSex());
        if (!(myUser.getAge() == null)) {
            tv_age.setText(myUser.getAge() + "");
        }

        rl_nick_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                LayoutInflater inflater = LayoutInflater.from(PersonalInfoActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_et_text, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
                editText.setText(tv_nick_name.getText());
                editText.setSelection(editText.getText().length());

                dialog.setTitle("修改用户名")
                        .setView(dialogView)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals("")) {
                                            showInputNotNullToast();
                                        } else {
                                            String nickName = editText.getText().toString();
                                            updateNickName(nickName);
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });

        rl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                dialog.setTitle("性别")
                        .setSingleChoiceItems(sex_array, sex_array_selected_index,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sex_array_selected_index = which;
                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str_sex = sex_array[sex_array_selected_index];
                                        updateSex(str_sex);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });

        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                LayoutInflater inflater = LayoutInflater.from(PersonalInfoActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_et_number, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
                editText.setText(tv_age.getText());
                editText.setSelection(editText.getText().length());

                dialog.setTitle("修改年龄")
                        .setView(dialogView)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals("")) {
                                            showInputNotNullToast();
                                        } else {
                                            Integer integer_age = Integer.parseInt(
                                                    editText.getText().toString());
                                            updateAge(integer_age);
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });
    }

    private void updateNickName(String nickName) {
        MyUser newUser = new MyUser();
        newUser.setNickName(nickName);
        MyUser myUserNickName = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserNickName.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showUpdateSuccessToast();
                        } else {
                            showUpdateFailToast();
                        }
                    }
                });
        tv_nick_name.setText(nickName);
    }

    private void updateSex(String str_sex) {
        MyUser newUser = new MyUser();
        newUser.setSex(str_sex);
        MyUser myUserSex = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserSex.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showUpdateSuccessToast();
                        } else {
                            showUpdateFailToast();
                        }
                    }
                });
        tv_sex.setText(str_sex);
    }

    private void updateAge(Integer integer_age) {
        MyUser newUser = new MyUser();
        newUser.setAge(integer_age);
        MyUser myUserAge = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserAge.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showUpdateSuccessToast();
                        } else {
                            showUpdateFailToast();
                        }
                    }
                });

        tv_age.setText(integer_age + "");
    }

    private void showUpdateSuccessToast() {
        Toast.makeText(PersonalInfoActivity.this, "更新成功", Toast.LENGTH_SHORT)
                .show();
    }

    private void showUpdateFailToast() {
        Toast.makeText(PersonalInfoActivity.this, "更新失败", Toast.LENGTH_SHORT)
                .show();
    }

    private void showInputNotNullToast() {
        Toast.makeText(PersonalInfoActivity.this, "输入不能为空", Toast.LENGTH_SHORT)
                .show();
    }
}
