package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.NormalState;
import com.happylrd.aurora.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

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

                    initDefaultMode(myUser);

                    ToastUtil.showSignUpSuccessToast(SignupActivity.this);
                    GoToLogin();
                } else {
                    ToastUtil.showSignUpFailToast(SignupActivity.this);
                }
            }
        });
    }

    /**
     * init 7 mode for new sign-up user
     *
     * @param myUser
     */
    private void initDefaultMode(MyUser myUser) {
        List<Integer> colors1 = new ArrayList<>();
        colors1.add(getResources().getColor(R.color.md_red_500));
        colors1.add(getResources().getColor(R.color.md_green_500));
        colors1.add(getResources().getColor(R.color.md_pink_500));
        colors1.add(getResources().getColor(R.color.md_purple_500));
        colors1.add(getResources().getColor(R.color.md_cyan_500));
        colors1.add(getResources().getColor(R.color.md_amber_500));

        List<Integer> colors2 = new ArrayList<>();
        colors2.add(getResources().getColor(R.color.md_pink_500));
        colors2.add(getResources().getColor(R.color.md_lime_500));
        colors2.add(getResources().getColor(R.color.md_cyan_500));
        colors2.add(getResources().getColor(R.color.md_light_green_500));
        colors2.add(getResources().getColor(R.color.md_pink_A100));

        List<Integer> colors3 = new ArrayList<>();
        colors3.add(getResources().getColor(R.color.md_red_500));
        colors3.add(getResources().getColor(R.color.md_teal_A200));
        colors3.add(getResources().getColor(R.color.md_yellow_500));
        colors3.add(getResources().getColor(R.color.md_light_green_A200));

        List<Integer> colors4 = new ArrayList<>();
        colors4.add(getResources().getColor(R.color.md_red_A400));
        colors4.add(getResources().getColor(R.color.md_cyan_A200));
        colors4.add(getResources().getColor(R.color.md_purple_A200));
        colors4.add(getResources().getColor(R.color.md_yellow_500));

        List<Integer> colors5 = new ArrayList<>();
        colors5.add(getResources().getColor(R.color.md_red_A400));
        colors5.add(getResources().getColor(R.color.md_white_1000));
        colors5.add(getResources().getColor(R.color.md_yellow_500));
        colors5.add(getResources().getColor(R.color.md_cyan_A200));
        colors5.add(getResources().getColor(R.color.md_purple_A200));

        List<Integer> colors6 = new ArrayList<>();
        colors6.add(getResources().getColor(R.color.md_lime_A200));
        colors6.add(getResources().getColor(R.color.md_red_600));
        colors6.add(getResources().getColor(R.color.md_cyan_500));

        List<Integer> colors7 = new ArrayList<>();
        colors7.add(getResources().getColor(R.color.md_white_1000));
        colors7.add(getResources().getColor(R.color.md_yellow_500));
        colors7.add(getResources().getColor(R.color.md_black_1000));

        saveMode(myUser, getString(R.string.default_mode_1_en), colors1);
        saveMode(myUser, getString(R.string.default_mode_2_en), colors2);
        saveMode(myUser, getString(R.string.default_mode_3_en), colors3);
        saveMode(myUser, getString(R.string.default_mode_4_en), colors4);
        saveMode(myUser, getString(R.string.default_mode_5_en), colors5);
        saveMode(myUser, getString(R.string.default_mode_6_en), colors6);
        saveMode(myUser, getString(R.string.default_mode_7_en), colors7);
    }

    private void saveMode(MyUser myUser, String modeName, final List<Integer> defaultColors) {
        Mode mode = new Mode();
        mode.setModeName(modeName);
        mode.setAuthor(myUser);

        mode.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    findModeById(objectId, defaultColors);
                } else {
                    Log.d(TAG, "save mode failed");
                }
            }
        });
    }

    private void findModeById(String objectId, final List<Integer> defaultColors) {
        BmobQuery<Mode> query = new BmobQuery<Mode>();
        query.getObject(objectId, new QueryListener<Mode>() {
            @Override
            public void done(Mode mode, BmobException e) {
                if (e == null) {
                    // just save normal state
                    saveNormalState(mode, defaultColors);
                } else {
                    Log.d(TAG, "find mode failed");
                }
            }
        });
    }

    private void saveNormalState(Mode mode, final List<Integer> defaultColors) {
        NormalState mNormalState = new NormalState();
        mNormalState.setMode(mode);
        mNormalState.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    findNormalStateById(objectId, defaultColors);
                } else {
                    Log.d(TAG, "save normalState failed");
                }
            }
        });
    }

    private void findNormalStateById(String objectId, final List<Integer> defaultColors) {
        BmobQuery<NormalState> queryNormalState = new BmobQuery<NormalState>();
        queryNormalState.getObject(objectId, new QueryListener<NormalState>() {
            @Override
            public void done(NormalState normalState, BmobException e) {
                if (e == null) {
                    saveMotionByNormalState(normalState, defaultColors);
                } else {
                    Log.d(TAG, "find normalState failed");
                }
            }
        });
    }

    private void saveMotionByNormalState(NormalState normalState, List<Integer> defaultColors) {
        Motion mMotion = new Motion();

        mMotion.setIntColorList(defaultColors);
        mMotion.setPatternName(getString(R.string.pattern_thin_stripe));

        mMotion.setAnimationName(getString(R.string.anim_ramp));
        mMotion.setRotationName(getString(R.string.rotation_float_left));
        mMotion.setActionName(getString(R.string.action_fade_in_out));

        mMotion.setNormalState(normalState);
        mMotion.setGestureState(null);

        mMotion.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    // save motion success
                } else {
                    Log.d(TAG, "save motion failed");
                }
            }
        });
    }

    private void GoToLogin() {
        Intent intent = LoginActivity.newIntent(SignupActivity.this);
        startActivity(intent);
    }
}
