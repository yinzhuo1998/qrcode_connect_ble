package com.manong.putSystem.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.manong.putSystem.R;
import com.manong.putSystem.adapter.MainListDialogAdapter;
import com.manong.putSystem.basic.TitleBar;
import com.manong.putSystem.bean.DevicePropertyInfo;
import com.manong.putSystem.util.BluetoothUtils;
import com.manong.putSystem.util.CRC16;
import com.manong.putSystem.util.Constants;
import com.manong.putSystem.util.DataUtil;
import com.manong.putSystem.util.MToast;
import com.manong.putSystem.util.QrCodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BleConnActivity extends AppCompatActivity {

    private Button main_sacnning_ble;
    private ListView deviceListView;
    private TitleBar titleBar;
    private TextView ble_conn_tip;

    //ble适配器
    private BluetoothAdapter mBluetoothAdapter;

    //蓝牙方面
    public static Map<String, String> deviceCheck;
    public static List<BluetoothDevice> devices;
    //蓝牙设备
    public static BluetoothDevice device;
    public static BluetoothUtils bluetoothUtils;
    //实际蓝牙适配
    private static MainListDialogAdapter mainListDialogAdapter;

    //所有服务
    public static List<BluetoothGattService> services;
    //所有特征
    private static List<BluetoothGattCharacteristic> characteristics;
    //写入所需的服务UUID
    private String serviceUUID;
    //写入所需的特征UUID
    private String characterUUID;

    //权限相关
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_ENABLE_LOC = 2;
    private boolean ALL_AUTH_IS_OPEN = false;

    private static final int REQUEST_CODE_SCAN = 3;

    private String qrcode;


    //扫描等待
    private ProgressDialog sacnDialog;

    //连接等待
    private ProgressDialog connDialog;

    public static Handler myHandler;

    // 接收数据builder
    StringBuilder builder;
    // 数据总包数
    int count = 0;
    // 当前总包数
    int currCount = 0;

    Timer sacnTimer;
    Timer connTimer;
    int connCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_conn);

        findView();
        setListener();

        init();

        sacnBle(3000);

    }

    void init(){
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
//        BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        devices = new ArrayList<BluetoothDevice>();
        deviceCheck = new HashMap<>();
        bluetoothUtils = new BluetoothUtils(this);
        mainListDialogAdapter = new MainListDialogAdapter(this, devices);
        deviceListView.setAdapter(mainListDialogAdapter);

        //扫描加载框
        sacnDialog = new ProgressDialog(this);
        sacnDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
        sacnDialog.setTitle("提示");//设置标题
        sacnDialog.setMessage("正在扫描");
        sacnDialog.setCancelable(true);//设置进度条是否可以按退回键取消
        sacnDialog.setIndeterminate(false);//设置进度条是否为不明确
        sacnDialog.setOnCancelListener(new Dialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                bluetoothUtils.stopScan();
            }
        });

        //连接加载框
        connDialog = new ProgressDialog(this);
        connDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
        connDialog.setTitle("提示");//设置标题
        connDialog.setMessage("正在连接");
        connDialog.setCancelable(true);//设置进度条是否可以按退回键取消
        connDialog.setIndeterminate(false);//设置进度条是否为不明确
        connDialog.setOnCancelListener(new Dialog.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                bluetoothUtils.disconnect();
                connTimer.cancel();
            }
        });


        myHandler = new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {

                switch (msg.what) {

                    // 说明查询到新的设备
                    case Constants.FIND_NEW_DEVICE:
                        BluetoothDevice device = msg.getData().getParcelable(Constants.NEW_DEVICE);
                        if(device == null || device.getAddress() == null || device.getName() == null){
                            return;
                        }
                        mainListDialogAdapter.setDevices(devices);
                        mainListDialogAdapter.notifyDataSetChanged();

                        // 显示提示语
                        if(devices != null && devices.size() > 0) {
                            ble_conn_tip.setText("选择连接时请注意观察设备指示以确保连接正确");
                        }

                        break;



                    // 找到服务
                    case Constants.FIND_SERVICES:

                        serviceUUID = null;

                        // 获取写的服务和特征
                        if(services.size() > 0){
                            for (BluetoothGattService bluetoothGattService : services) {
                                String uuid = bluetoothGattService.getUuid().toString();
                                if(Constants.BLE_SERVICE_UUID.equalsIgnoreCase(uuid)){
                                    serviceUUID = uuid;
                                    break;
                                }
                            }
                            System.out.println("=================================服务UUID: " + serviceUUID);

                            // 根据服务获取特征
                            if(serviceUUID != null){
                                characteristics = bluetoothUtils.discoverCharacteristic(UUID.fromString(serviceUUID));
                                if(characteristics != null && characteristics.size() > 0){
                                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                                        if (Constants.BLE_CHARACTERISTIC_UUID_1.equalsIgnoreCase(characteristic.getUuid().toString())) {
                                            int property = characteristic.getProperties();
                                            if ((property | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                                bluetoothUtils.setNotify(UUID.fromString(serviceUUID), UUID.fromString(Constants.BLE_CHARACTERISTIC_UUID_1), true);
                                            }
                                        }
                                        if (Constants.BLE_CHARACTERISTIC_UUID_2.equalsIgnoreCase(characteristic.getUuid().toString())) {
                                            characterUUID = Constants.BLE_CHARACTERISTIC_UUID_2;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("=================================特征UUID: " + serviceUUID);
                        if(characterUUID != null){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            
                            
                            // 在这写数据到蓝牙中
                            writeData();
                            return;
                        }
                        connFailHandle();
                        break;


                    // 解析通知过来的数据
                    case Constants.CHARACTERISTIC_NOTIFY:
                        if(connTimer == null){
                            return;
                        }
                        byte[] notify_value = (byte[]) msg.getData().get("value");
                        String data = DataUtil.bytesToHexString(notify_value);

                        if(currCount == 0){
                            builder = new StringBuilder();
                            count = Integer.parseInt(data.substring(4, 8), 16) + 4;
                            count = count / 20;
                            if(count % 20 > 0){
                                count++;
                            }
                        }
                        builder.append(data);
                        currCount++;
                        if(count == currCount){

                            // 数据处理.....
                            System.out.println("=================================builder: " + builder.toString());

                            // 因为我的业务需求是连接上获取到需要的数据就断开连接了
                            bluetoothUtils.disconnect();
                            connDialog.dismiss();
                            if(connTimer != null) {
                                connTimer.cancel();
                            }

                        }
                        break;

                    // 连接超时
                    case -1:

                        if(BleConnActivity.this.isFinishing()){
                            return;
                        }

                        if(connDialog != null && connDialog.isShowing()){
                            connDialog.dismiss();
                        }

                        MToast.showToast(BleConnActivity.this, "连接超时请重试", Toast.LENGTH_LONG);

                        break;


                    case REQUEST_CODE_SCAN:

                        DevicePropertyInfo devicePropertyInfo = QrCodeUtil.deviceQrCodeHandle(qrcode);
                        if(devicePropertyInfo.getBle_mac() != null){
                            // 连接蓝牙
                            if(devices != null && devices.size() > 0) {
                                for (BluetoothDevice bluetoothDevice : devices) {
                                    if (devicePropertyInfo.getBle_mac().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                                        connBle(bluetoothDevice);
                                        return;
                                    }
                                }
                            }
                        }

                        MToast.showToast(BleConnActivity.this, "当前范围内没有可连接的设备", Toast.LENGTH_LONG);

                        break;


                }

            }
        };

    }




    void findView(){
        titleBar = new TitleBar(findViewById(R.id.title_bar));
        main_sacnning_ble = findViewById(R.id.ble_conn_sacnning_ble);
        deviceListView = findViewById(R.id.deviceListView);
        ble_conn_tip = findViewById(R.id.ble_conn_tip);

    }
    void setListener(){
        titleBar.setTitleText("蓝牙连接");
        titleBar.setRightText("快捷");
        titleBar.setRightImageResource(R.mipmap.sacn);
        titleBar.showRightImageView(true);
        titleBar.showRightTextView(true);
        titleBar.setRightClickListener(rightClickListener);
        titleBar.setLeftClickListener(leftClickListener);
        main_sacnning_ble.setOnClickListener(sacnBleListener);
        deviceListView.setOnItemClickListener(deviceListViewListener);
    }


    private View.OnClickListener leftClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };



    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 动态权限申请
            if (ContextCompat.checkSelfPermission(BleConnActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BleConnActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                //扫码
                goScan();

            }

        }
    };





    private View.OnClickListener sacnBleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ble_conn_tip.setText("未发现蓝牙设备, 请确认设备电源已打开");
            sacnBle(5000);

        }
    };

    private AdapterView.OnItemClickListener deviceListViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            device = mainListDialogAdapter.getItem(position);
            connBle(device);


        }
    };




    /**
     * 连接蓝牙操作
     * @param device
     */
    void connBle(final BluetoothDevice device){
        connDialog.show();
        bluetoothUtils.connect(device);
        System.out.println(System.currentTimeMillis() + "=================================开始连接蓝牙 ");
        if(connTimer != null){
            connTimer.cancel();
        }
        connCount = 1;
        connTimer = new Timer();
        connTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                // 重新连接
                bluetoothUtils.disconnect();
                if(BleConnActivity.device == null || BleConnActivity.device.getAddress() == null){
                    connTimer.cancel();
                    return;
                }
                bluetoothUtils.connect(device);
                connCount++;

                // N秒后还没有获取到数据并跳转的话则关闭转圈窗口
                if(connCount > 2) {
                    connTimer.cancel();
                    connTimer = null;
                    Message message = new Message();
                    message.what = -1;
                    myHandler.sendMessage(message);
                }
            }
        }, 10000, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 扫描蓝牙设备
     * @param time 毫秒
     */
    void sacnBle(long time){

        if(checkBlePermission()){
            if(BluetoothAdapter.STATE_ON != mBluetoothAdapter.getState()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
                return;
            }

            // 弹出转圈
            if(!sacnDialog.isShowing()){
                sacnDialog.show();
            }

            // 清空列表
            devices.clear();
            deviceCheck.clear();
            mainListDialogAdapter.setDevices(devices);
            mainListDialogAdapter.notifyDataSetChanged();

            // 开始扫描
            bluetoothUtils.startScan(null);
            if(sacnTimer != null){
                sacnTimer.cancel();
            }

            sacnTimer = new Timer();
            sacnTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 关闭转圈
                    if(!BleConnActivity.this.isFinishing()){
                        sacnDialog.dismiss();
                        // 停止扫描
                        bluetoothUtils.stopScan();
                    }

                }
            }, time);

        }
    }



    /**
     * 检查蓝牙权限
     */
    public boolean checkBlePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ENABLE_LOC);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_LOC:
                // 如果请求被取消，则结果数组为空。
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("=================================同意申请");
                } else {
                    System.out.println("=================================拒绝申请");
                }
                break;

            case REQUEST_CODE_SCAN:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //扫码
                    goScan();
                } else {
                    MToast.showToast(this, "你拒绝了权限申请，无法打开相机扫码哟！", Toast.LENGTH_SHORT);
                }

                break;
        }
    }



    //查询设备序列号
    public void writeData() {
        //转换值为十六进制字符串并拼接
        StringBuilder sb = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append("FFFF");
        builder.append("000E");
        builder.append("0000");
        builder.append("0000");
        builder.append("06");
        builder.append("00");
        builder.append("0000");
        builder.append("0004");
        byte[] crcs = DataUtil.HexString2Bytes(builder.toString());
        String crc = CRC16.getCRC(crcs);
        builder.append(crc.substring(2, 4) + crc.substring(0, 2));
        //安全码
        builder.append("0000");
        //转换值为byte数组
        Queue<byte[]> bytes = DataUtil.splitPacketFor20Byte(DataUtil.hexStringToBytes(builder.toString()));
        for (byte[] byte1 : bytes) {
            //写入值
            bluetoothUtils.writeCharacteristic(UUID.fromString(serviceUUID), UUID.fromString(characterUUID), byte1);
            count = 0;
            currCount = 0;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("=================================写入数据: " + builder.toString());

    }



    void connFailHandle(){
        if (connDialog != null && connDialog.isShowing()){
            connDialog.dismiss();
            MToast.showToast(getApplicationContext(), "连接失败", Toast.LENGTH_LONG);
        }
        bluetoothUtils.disconnect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK)
                    sacnBle(5000);
                break;

            case 20:

                if (data != null) {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (scanResult != null && scanResult.getContents() != null){
                        //扫码回调
                        qrcode = scanResult.getContents();
                        System.out.println(1111);
                    }else{
                        //相册/蓝牙回调
                        qrcode = data.getStringExtra("qrcode");
                    }

                    if(qrcode != null){
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("qrcode", qrcode);
                        msg.setData(bundle);
                        msg.what = REQUEST_CODE_SCAN;
                        myHandler.sendMessage(msg);
                        return;

                    }
                }

                MToast.showToast(this, "请重新扫描", Toast.LENGTH_LONG);


                break;

        }

    }





    /**
     * 跳转到扫码界面扫码
     */
    private void goScan(){

        IntentIntegrator integrator = new IntentIntegrator(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(QrcodeCaptrueActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt(""); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        IntentIntegrator.REQUEST_CODE = 20;
        integrator.initiateScan();

    }
}
