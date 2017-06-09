package com.example.jeju;

import android.os.Bundle;

import org.mospi.moml.framework.pub.core.MOMLFragmentActivity;

public class SuccessActivity extends MOMLFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        loadApplication("/moml/applicationInfo_2.xml");
        //MOMLView momlView2 = (MOMLView) findViewById(R.id.momlView2);
        //setMomlView(momlView2);
    }

}
