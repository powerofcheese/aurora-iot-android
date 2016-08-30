package com.happylrd.aurora;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonalInfoActivity extends AppCompatActivity {

    private String[] sex_array = new String[]{"男", "女", "未知"};
    private int sex_array_selected_index = 2;

    private Toolbar toolbar;
    private RelativeLayout rl_sex;
    private TextView tv_personal_sex;

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

        tv_personal_sex = (TextView) findViewById(R.id.tv_personal_sex);

        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
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
                                        tv_personal_sex.setText(sex_array[sex_array_selected_index]);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .create();
                dialog.show();
            }
        });
    }
}
