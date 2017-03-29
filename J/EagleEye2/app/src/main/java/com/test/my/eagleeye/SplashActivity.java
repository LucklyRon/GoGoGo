package com.test.my.eagleeye;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private TextView versionNumber;
	private LinearLayout mLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);
		mLinearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout01);
		versionNumber = (TextView) this.findViewById(R.id.versionNumber);
		versionNumber.setText(getVersion());
		
		// �жϵ�ǰ����״̬�Ƿ����
		if(isNetWorkConnected()){
			//splash ��һ������,����������
			AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
			aa.setDuration(2000);
			mLinearLayout.setAnimation(aa);
			mLinearLayout.startAnimation(aa);
			//ͨ��handler ��ʱ2�� ִ��r���� 
			new Handler().postDelayed(new LoadMainTabTask(), 2000);
		}else{
			showSetNetworkDialog();
		}
	}
	
	private class LoadMainTabTask implements Runnable{

		public void run() {
			Intent intent = new Intent(SplashActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
			
		}
		
	}
	private void showSetNetworkDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("Set Network");
		builder.setMessage("Please check network status");
		builder.setPositiveButton("Set Network", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				//����һ��Ҫ�������� 
				intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
				
				startActivity(intent);
				finish();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();
		
	}


	private String getVersion(){
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			return "Version " +info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "Version";
		}
	}
	
	/**
	 * �ж�����״̬
	 */
	private boolean isNetWorkConnected(){
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		
//		WifiManager  wifimanager =  (WifiManager) getSystemService(WIFI_SERVICE);
//		wifimanager.isWifiEnabled();
//		wifimanager.getWifiState();
	
		return (info!=null&&info.isConnected());
	}

}
