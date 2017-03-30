package com.test.my.eagleeye;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends TabActivity {
    private TabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_tab);
        mTabHost = getTabHost();
        mTabHost.addTab(publishLostInformationTab());
        mTabHost.addTab(getLostInformationTab());

    }

    private TabHost.TabSpec publishLostInformationTab(){
        TabHost.TabSpec spec = mTabHost.newTabSpec("lostInformation");
        Intent intent = new Intent(this,PublishActivity.class);
        spec.setContent(intent);
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
        spec.setIndicator("Publish",icon);
        return spec;
    }

    private TabHost.TabSpec getLostInformationTab(){
        TabHost.TabSpec spec = mTabHost.newTabSpec("lostInformation");
        Intent intent = new Intent(this,LostActivity.class);
        spec.setContent(intent);
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
        spec.setIndicator("Lost",icon);
        return spec;
    }
}
