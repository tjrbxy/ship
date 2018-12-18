package com.alixlp.ship.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Triggering;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.alixlp.ship.R;


public class ScanActivity extends AppCompatActivity {

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
    private ActionBar actionBar;
    private EditText showScanResult;
    private Button btn;
    private Button mScan;
    private Button mClose;
    private int type;
    private int outPut;

    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            showScanResult.setText("");
            mVibrator.vibrate(100);

            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            barcodeStr = new String(barcode, 0, barcodelen);

            showScanResult.setText(barcodeStr);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setupView();

    }

    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    private void setupView() {
        // TODO Auto-generated method stub
        showScanResult = (EditText) findViewById(R.id.scan_result);
        btn = (Button) findViewById(R.id.manager);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mScanManager.getTriggerMode() != Triggering.CONTINUOUS)
                    mScanManager.setTriggerMode(Triggering.CONTINUOUS);
            }
        });

        mScan = (Button) findViewById(R.id.scan);
        mScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //if(type == 3)
                mScanManager.stopDecode();
                isScaning = true;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mScanManager.startDecode();
            }
        });

        mClose = (Button) findViewById(R.id.close);
        mClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // if(isScaning) {
                //  isScaning = false;
                mScanManager.stopDecode();
                //}
            }
        });

        //btn.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initScan();
        showScanResult.setText("");
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }

        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
}

