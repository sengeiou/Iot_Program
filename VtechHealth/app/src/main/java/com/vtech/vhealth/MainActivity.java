package com.vtech.vhealth;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.vtech.vhealth.function.main.MainHealthActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainHealthActivity.start(this);
        finish();

    }

    @Override
    public void onClick(View view) {


    }


}
