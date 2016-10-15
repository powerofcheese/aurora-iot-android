package com.happylrd.aurora.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TryActivity extends AppCompatActivity {
    MusicEnergy mMusicEnergy = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMusicEnergy = new MusicEnergy(this);

        setContentView(mMusicEnergy);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicEnergy.onClose();
    }
}
