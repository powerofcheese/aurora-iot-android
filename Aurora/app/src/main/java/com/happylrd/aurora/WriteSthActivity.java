package com.happylrd.aurora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.happylrd.aurora.entity.WriteSthContent;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WriteSthActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_write_sth;
    private ImageView image_submit_sth;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WriteSthActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sth);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("写说说");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_write_sth = (EditText) findViewById(R.id.et_write_sth);

        image_submit_sth = (ImageView) findViewById(R.id.image_submit_sth);
        image_submit_sth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_sth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_publish) {
            String textContent = et_write_sth.getText().toString();

            uploadResToCloud(textContent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadResToCloud(String textContent) {
        WriteSthContent writeSthContent = new WriteSthContent();
        writeSthContent.setTextContent(textContent);
        writeSthContent.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(WriteSthActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WriteSthActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadPicToCloud(String picPath) {

    }
}
