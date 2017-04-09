package com.dreamingfly.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamingfly.R;
import com.dreamingfly.adapter.LazyAdapter;
import com.dreamingfly.entry.Information;
import com.dreamingfly.utils.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {
    // 所有的静态变量
    static final String URL = "http://192.168.1.122:8080/Dreamingfly/list.xml";// xml目的地址

    private ListView list;
    private LazyAdapter adapter;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            adapter = new LazyAdapter(MainActivity.this,
                    (ArrayList<Information>) msg.obj);
            list.setAdapter(adapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        final XMLParser parser = new XMLParser();
        new Thread(new Runnable() {
            public void run() {
                // clientHelper = new ClientHelper();
                ArrayList<Information> informations = new ArrayList<Information>();
                String xml = parser.getXmlFromUrl(URL); // 从网络获取xml
                Document doc = parser.getDomElement(xml); // 获取 DOM 节点
                NodeList nl = doc.getElementsByTagName("infomation");
                for (int i = 0; i < nl.getLength(); i++) {
                    Information info = new Information();
                    Element e = (Element) nl.item(i);
                    info.setName(parser.getValue(e, "name"));
                    info.setAge(parser.getValue(e, "age"));
                    info.setSex(parser.getValue(e, "sex"));
                    info.setDay(parser.getValue(e, "day"));
                    info.setImage(parser.getValue(e, "image"));
                    info.setDescription(parser.getValue(e, "description"));
                    informations.add(info);
                }
                handler.sendMessage(handler.obtainMessage(0, informations));
            }
        }).start();

        list = (ListView) findViewById(R.id.list);

        Button btnPulish = (Button) this.findViewById(R.id.btnPulish);
        btnPulish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(MainActivity.this,
                        PublishActivity.class));
                MainActivity.this.finish();
            }
        });

        // 为单一列表行添加单击事件

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this,
                        InfoActivity.class);

                Information information = new Information();
                ImageView imageView = (ImageView) view
                        .findViewById(R.id.list_image);
                information.setImage((String) imageView.getTag());
                TextView textName = (TextView) view.findViewById(R.id.name);
                information.setName(textName.getText().toString());
                TextView textAge = (TextView) view.findViewById(R.id.age);
                information.setAge(textAge.getText().toString());
                TextView textSex = (TextView) view.findViewById(R.id.sex);
                information.setSex(textSex.getText().toString());
                TextView textDay = (TextView) view.findViewById(R.id.day);
                information.setDay(textDay.getText().toString());
                TextView textDescription = (TextView) view
                        .findViewById(R.id.description);
                information
                        .setDescription(textDescription.getText().toString());

                // 用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", information);
                intent.putExtras(bundle);

                startActivity(intent);
                MainActivity.this.finish();
            }
        });
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

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return "/data/data/com.example.demo/cache";
        }
        return sdDir.toString();
    }
}