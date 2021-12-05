package com.manong.putSystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.manong.putSystem.R;
import com.manong.putSystem.basic.TitleBar;
import com.manong.putSystem.util.MToast;
import com.manong.putSystem.util.PhotoUtils;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.List;

/**
 * 自定义扫码
 */
public class QrcodeCaptrueActivity extends Activity implements View.OnClickListener{
    //相册
    private RelativeLayout layout_scan_photo;
    //闪光灯
    private RelativeLayout layout_scan_flash;
    //文字
    private TextView text_scan_flash;

    private TitleBar titleBar;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    //判断开灯还是关灯
    private Boolean isTorchOn = false;

    //图片
    private Bitmap bitmap;

    private static final int REQUEST_LIST_CODE = 0;
    private static final int RESULT_DECODE_BITMAP = 5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowsColor();

        setWindowsTintColor();

        barcodeScannerView = initializeContent();

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        //文字
        text_scan_flash = (TextView) findViewById(R.id.text_scan_flash);
        //相册
        layout_scan_photo = (RelativeLayout) findViewById(R.id.layout_scan_photo);
        layout_scan_photo.setOnClickListener(this);
        //闪光灯
        layout_scan_flash = (RelativeLayout) findViewById(R.id.layout_scan_flash);
        layout_scan_flash.setOnClickListener(this);
        titleBar = new TitleBar(findViewById(R.id.title_bar));
        titleBar.setTitleText("扫一扫");
        titleBar.setLeftClickListener(leftClickListener);
    }


    private View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.qr_code_captrue_layout);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onResume() {
        super.onResume();

        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 设置最上面菜单栏的颜色
     */
    public void setWindowsColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.backgroundcolor));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_scan_photo:
                ISListConfig config = new ISListConfig.Builder()
                        .multiSelect(true)
                        .needCamera(false)
                        // 是否记住上次选中记录
                        .rememberSelected(false)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#049c98"))
                        .titleBgColor(Color.parseColor("#049c98")).maxNum(1).build();

                ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
                break;
            case R.id.layout_scan_flash:
                if (isTorchOn) {
                    isTorchOn = false;
                    text_scan_flash.setText("开灯");
                    barcodeScannerView.setTorchOff();
                } else {
                    isTorchOn = true;
                    text_scan_flash.setText("关灯");
                    barcodeScannerView.setTorchOn();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            //获取本地图片
            File file = new File(pathList.get(0));
            //转换Uri
            Uri uri = Uri.fromFile(file);
            try {
                // 下面这句话可以通过URi获取到文件的bitmap
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);

                // 在这里我用到的 getSmallerBitmap 非常重要，下面就要说到
                bitmap = PhotoUtils.getSmallerBitmap(bitmap);

                // 获取bitmap的宽高，像素矩阵
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[width*height];
                bitmap.getPixels(pixels,0,width,0,0,width,height);

                // 最新的库中，RGBLuminanceSource 的构造器参数不只是bitmap了
                RGBLuminanceSource source = new RGBLuminanceSource(width,height,pixels);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Reader reader = new MultiFormatReader();
                Result result = null;

                // 尝试解析此bitmap，！！注意！！ 这个部分一定写到外层的try之中，因为只有在bitmap获取到之后才能解析。写外部可能会有异步的问题。（开始解析时bitmap为空）
                try {
                    result = reader.decode(binaryBitmap);

                    System.out.println("=================================扫码数据: " + result.getText());

                    finish();
                } catch (Exception e) {
                    MToast.showToast(this,"解析失败，请您确认所选条形码是否正确！", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断Android系统版本是否 >= M(API23)
     */
    private boolean isM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置最上面菜单栏的颜色
     */
    public void setWindowsTintColor(){
        //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.backgroundcolor));
        }
    }
}
