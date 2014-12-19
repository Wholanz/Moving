package com.example.Moving;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by tiny on 12/19/14.
 */
public class AboutUs extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_us);
    }
}