package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happylrd.aurora.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorSchemeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ColorSchemeAdapter mColorSchemeAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ColorSchemeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_scheme);

        initView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ColorSchemeActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mColorSchemeAdapter = new ColorSchemeAdapter();
        mRecyclerView.setAdapter(mColorSchemeAdapter);
    }

    private class ColorSchemeHolder extends RecyclerView.ViewHolder {
        private CircleImageView civ_head_portrait;
        private TextView tv_nick_name;
        private TextView tv_message;

        public ColorSchemeHolder(View itemView) {
            super(itemView);

            civ_head_portrait = (CircleImageView) itemView.findViewById(R.id.civ_head_portrait);
            tv_nick_name = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
        }

        public void bindColorScheme() {

        }
    }

    private class ColorSchemeAdapter extends RecyclerView.Adapter<ColorSchemeHolder> {

        private static final int LENGTH = 18;

        @Override
        public ColorSchemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ColorSchemeActivity.this);
            View view =
                    inflater.inflate(R.layout.item_list, parent, false);
            return new ColorSchemeHolder(view);
        }

        @Override
        public void onBindViewHolder(ColorSchemeHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("配色方案");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_color_scheme);
    }
}
