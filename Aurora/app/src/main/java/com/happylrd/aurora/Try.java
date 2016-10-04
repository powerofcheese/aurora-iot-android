package com.happylrd.aurora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Try extends AppCompatActivity {

    MusicEnergy mMusicEnergy = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMusicEnergy = new MusicEnergy(this);

        setContentView(mMusicEnergy);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMusicEnergy.onClose();
    }
}
