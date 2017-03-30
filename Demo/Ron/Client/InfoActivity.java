package com.example.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;
import com.example.entry.Information;
import com.lidroid.xutils.BitmapUtils;

public class InfoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bundle = this.getIntent().getExtras();
		Information information = (Information) bundle.getSerializable("info");
		setContentView(R.layout.activity_info);
		initView(information);
		Button btn = (Button) findViewById(R.id.back);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(InfoActivity.this, MainActivity.class));
				InfoActivity.this.finish();
			}
		});
	}

	private void initView(Information information) {
		ImageView imageView = (ImageView) this.findViewById(R.id.info);
		new BitmapUtils(getApplicationContext()).display(imageView,
				information.getImage());
		TextView name = (TextView) findViewById(R.id.name); // name
		TextView age = (TextView) findViewById(R.id.age); // age
		TextView sex = (TextView) findViewById(R.id.sex); // sex
		TextView day = (TextView) findViewById(R.id.day); // day
		TextView description = (TextView) findViewById(R.id.description); // day

		// 设置ListView的相关值
		name.setText(information.getName());
		age.setText(information.getAge());
		sex.setText(information.getSex());
		day.setText(information.getDay());
		description.setText(information.getDescription());
	}
}
