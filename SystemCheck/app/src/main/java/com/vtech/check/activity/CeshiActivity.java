package com.vtech.check.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.vtech.check.R;
import com.vtech.check.serfragment.ecg.EcgFragment;
import com.vtech.check.view.DrawLineView;
import com.vtech.check.view.MultiTouchView;

public class CeshiActivity extends Activity {

//    MultiTouchView multiTouchView;
    DrawLineView dwView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceshi);

/*        multiTouchView = (MultiTouchView) findViewById(R.id.multiTouchView);
        multiTouchView.setOnMultiTouchListener(new MultiTouchView.OnMultiTouchListener());*/
//        dwView=(DrawLineView)findViewById(R.id.dwView);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//
//        ft.add(R.id.item_fragment1, (Fragment)new EcgFragment()).commit();


    }



}
