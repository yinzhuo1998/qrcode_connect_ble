package com.manong.putSystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.manong.putSystem.R;
import com.manong.putSystem.basic.TitleBar;
import com.manong.putSystem.bean.DevicePropertyInfo;
import com.manong.putSystem.util.MToast;
import com.manong.putSystem.util.QrCodeUtil;

public class MainActivity extends AppCompatActivity {

    private TitleBar titleBar;
    private Button main_ble_conn_button;
    private Button main_sacn_button;



    private String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setListener();

    }


    void findView(){
        titleBar = new TitleBar(findViewById(R.id.title_bar));
        main_ble_conn_button = findViewById(R.id.main_ble_conn_button);
        main_sacn_button = findViewById(R.id.main_sacn_button);
    }

    void setListener(){
        titleBar.setTitleText("主页");
        titleBar.showLeftImageView(false);

        main_ble_conn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BleConnActivity.class);
                startActivity(intent);
            }
        });

        main_sacn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 动态权限申请
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    //扫码
                    goScan();

                }

            }
        });
    }




    private Handler myHandler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what) {

                case 1:

                    System.out.println("=================================扫码数据111: " + qrcode);

                    break;

            }
        }
    };










    /**
     * 跳转到扫码界面扫码
     */
    private void goScan(){
//        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_SCAN);

        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(QrcodeCaptrueActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt(""); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        IntentIntegrator.REQUEST_CODE = 10;
        integrator.initiateScan();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {

            if (data != null) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (scanResult != null && scanResult.getContents() != null) {
                    //扫码回调
                    qrcode = scanResult.getContents();
                } else {
                    //相册/蓝牙回调
                    qrcode = data.getStringExtra("qrcode");
                }

                if (qrcode != null) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("qrcode", qrcode);
                    msg.setData(bundle);
                    msg.what = 1;
                    myHandler.sendMessage(msg);

                    return;

                }
            }

            MToast.showToast(this, "请重新扫描", Toast.LENGTH_LONG);

        }

    }

}
