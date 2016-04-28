package com.bryanching.hotslogsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HotsLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                HotsLogFragment.newInstance())
                .commit();
        HotsLogApplication.getHotsLogComponent().inject(this);
    }
}
