package com.alixlp.ship.activity.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alixlp.ship.R;
import com.alixlp.ship.activity.BaseActivity;
import com.alixlp.ship.bean.Order;
import com.alixlp.ship.biz.OrderBiz;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.alixlp.ship.util.T;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;
    private final String KUAIDIID = "kuaidiid";

    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;

    private static final String TAG = "OrderDetailActivity-app";
    private OrderBiz mOrderBiz = new OrderBiz();
    private TextView mOrderId; // 订单号
    private TextView mAddTime; //下单时间
    private TextView mName;   //下单人
    private TextView mTel; // 电话
    private TextView mAddress; // 地址
    private TextView mGoods;  // 商品信息
    private TextView mScanGoods; // 掃入信息

    private int oid;
    private int orderStatus;

    private int mKuaiDiID;


    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            mScanGoods.setText("");
            mVibrator.vibrate(100);

            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            Log.d(TAG, "barcodelen: " + barcodelen);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            barcodeStr = new String(barcode, 0, barcodelen);
            Log.d(TAG, "barcodeStr: " + barcodeStr);
            if (0 == orderStatus || 3 == orderStatus) {
                // barcodeStr = "http://new.913fang.com/index/fw?f=392343496700";
                if (barcodeStr.indexOf("?f=") != -1) {
                    Log.d(TAG, "onReceive: 包含");
                } else {
                    Log.d(TAG, "onReceive: 不包含");
                }


                Log.d(TAG, "onReceive: " + barcodeStr);
                return;
                // 待发货  已推迟
/*                mOrderBiz.kd(oid, barcodeStr, new CommonCallback<Order>() {
                    @Override
                    public void onError(Exception e) {
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Order response) {
                        Log.d(TAG, "onSuccess: " + response);
                    }
                });*/

            } else if (1 == orderStatus || 2 == orderStatus) {
                mOrderBiz.express(oid, mKuaiDiID, barcodeStr, new CommonCallback<List>() {
                    @Override
                    public void onError(Exception e) {
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(List response, String info) {

                    }
                });

            }
            Log.d(TAG, "onReceive: " + orderStatus);

            mScanGoods.setText(barcodeStr);

        }

    };


    @Override
    protected void onInitVariable() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ORDER");
        oid = bundle.getInt("OID");
        orderStatus = bundle.getInt("ORDERSTATUS");

    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_order_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = "";
        switch (orderStatus) {
            case 0:
                title = "待发货";
                break;
            case 1:
                title = "待录单";
                break;
            case 2:
                title = "已录单";
                break;
            case 3:
                title = "已推迟";
                break;
        }
        toolbar.setTitle(title);
        mOrderId = findViewById(R.id.id_tv_order_sn);
        mAddTime = findViewById(R.id.id_tv_add_time);
        mName = findViewById(R.id.id_tv_name);
        mTel = findViewById(R.id.id_tv_tel);
        mAddress = findViewById(R.id.id_tv_address);
        mGoods = findViewById(R.id.id_tv_goods);
        mScanGoods = findViewById(R.id.id_tv_scan_goods);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mKuaiDiID = (int) SPUtils.getInstance().get(Config.KUAIDIID, 0); // 管理員綁定的快遞ID


    }

    @Override
    protected void onRequestData() {
        startLoadingProgress();
        mOrderBiz.orderDetail(oid, new CommonCallback<Order>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
            }

            @Override
            public void onSuccess(Order response, String info) {
                stopLoadingProgress();
                mOrderId.setText(response.getOrderid());
                mAddTime.setText(response.getAdd_time());
                mName.setText(response.getName());
                mTel.setText(response.getTel());
                mAddress.setText(response.getAddress());
                String goodsInfo = "";
                for (int index = 0; index < response.getGoods().size(); index++) {
                    goodsInfo += "名称：" + response.getGoods().get(index).getTitle() + "， 数量：" +
                            response.getGoods().get(index).getNum() + "\n";
                }
                Log.d(TAG, "onSuccess: " + goodsInfo);
                mGoods.setText(goodsInfo);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver); // 取消
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
        mScanGoods.setText("");
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID
                .WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        registerReceiver(mScanReceiver, filter); // 注册
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
}
