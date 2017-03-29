package com.test.my.eagleeye;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Created by 哲 on 2017/3/29.
 */

public class PublishActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Publish");
        setContentView(tv);
    }

}
