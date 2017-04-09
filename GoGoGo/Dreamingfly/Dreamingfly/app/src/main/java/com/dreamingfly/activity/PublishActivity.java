package com.dreamingfly.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamingfly.R;
import com.dreamingfly.entry.Information;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class PublishActivity extends Activity implements OnClickListener {
    private Information info;
    private TextView txtName;
    private TextView txtAge;
    private TextView txtSex;
    private TextView txtDay;
    private TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.publish_activity);
        Button btnBack = (Button) this.findViewById(R.id.back);
        Button btnPublish = (Button) this.findViewById(R.id.ok);
        btnBack.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
        txtAge = (TextView) findViewById(R.id.age);
        txtDay = (TextView) findViewById(R.id.day);
        txtSex = (TextView) findViewById(R.id.sex);
        txtDescription = (TextView) findViewById(R.id.description);
        txtName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.back:
                startActivity(new Intent(PublishActivity.this, MainActivity.class));
                this.finish();
                break;
            case R.id.ok:
                String Id = UUID.randomUUID().toString();
                info = GetInfo(Id);
                PublishInfo(info, Id);

                break;

            default:
                break;
        }
    }

    private Information GetInfo(String id) {
        Information information = new Information();
        information.setAge(txtAge.getText().toString());
        information.setDay(txtDay.getText().toString());
        information.setId(id);
        information.setName(txtName.getText().toString());
        information.setSex(txtSex.getText().toString());
        information.setDescription(txtDescription.getText().toString());
        return information;
    }

    private void PublishInfo(Information information,String id) {
        PublishData(information);
        Resources r = this.getApplicationContext().getResources();
       Bitmap bitmap = BitmapFactory.decodeResource(r, R.drawable.ic_launcher);
       //使用Xutils上传图片
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("img", saveFile(bitmap, id + ".jpeg"));
        httpUtils.send(HttpMethod.POST, "http://192.168.1.122:8080/Dreamingfly/UploadFileServlet", params, new RequestCallBack<String>() {
            ProgressDialog waitingDialog = new ProgressDialog(
                    PublishActivity.this);
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //上传成功，这里面的返回值，就是服务器返回的数据
                Log.e("返回值", responseInfo.result);
                waitingDialog.dismiss();
                Log.e("Test", "dialog");
                startActivity(new Intent(PublishActivity.this,
                        MainActivity.class));
                PublishActivity.this.finish();
                }
            @Override
            public void onStart() {
                waitingDialog.setTitle("Wait for upload");
                waitingDialog.setMessage("upload information...");
                waitingDialog.setIndeterminate(true);
                waitingDialog.setCancelable(false);
                waitingDialog.show();
            }
                    @Override
            public void onFailure(HttpException e, String s) {
                waitingDialog.dismiss();
                Log.e("失败:", e.getMessage());
                }
            });
    }

    private void PublishData(Information information) {
        try {
            RequestParams params = new RequestParams();
            // 封装一个JSON对象
            JSONObject param = new JSONObject();
            param.put("Id", information.getId());
            param.put("name", information.getName());
            param.put("age", information.getAge());
            param.put("sex", information.getSex());
            param.put("day", information.getDay());
            param.put("description", information.getDescription());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
            Log.e("Test", param.toString());
            HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1000 * 10);
            http.send(HttpMethod.POST,
                    "http://192.168.1.122:8080/Dreamingfly/AddDataServerlet", params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {

                        }
                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(getApplicationContext(),
                                    "upload failed", Toast.LENGTH_LONG).show();
                            Log.e("error", msg);
                            Log.e("error", error.toString());
                        }
                    });

        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public File saveFile(Bitmap bm, String fileName) {
        String path = getSDPath() + "/revoeye/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = null;
        try {
            myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return "/data/data/com.dreamingfly/cache";
        }
        return sdDir.toString();
    }
}
