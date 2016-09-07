package com.happylrd.aurora;

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
import android.widget.ImageView;
import android.widget.TextView;

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

        public ImageView itemAvatar;
        public TextView itemTitle;
        public TextView itemDescription;

        public ColorSchemeHolder(View itemView) {
            super(itemView);

            itemAvatar = (ImageView) itemView.findViewById(R.id.list_avatar);
            itemTitle = (TextView) itemView.findViewById(R.id.list_title);
            itemDescription = (TextView) itemView.findViewById(R.id.list_desc);
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
