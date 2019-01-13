package com.alixlp.ship.fragment.active;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alixlp.ship.R;
import com.alixlp.ship.bean.Agent;
import com.alixlp.ship.bean.Goods;
import com.alixlp.ship.bean.Order;
import com.alixlp.ship.biz.AgentBiz;
import com.alixlp.ship.biz.OrderBiz;
import com.alixlp.ship.config.Config;
import com.alixlp.ship.net.CommonCallback;
import com.alixlp.ship.util.SPUtils;
import com.alixlp.ship.util.T;

import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveOrderFragment extends Fragment {

    private static final String TAG = "app-ActiveOrderFragment";
    private EditText mUserId;
    private Button mBtCheck, mBtSend;
    private AgentBiz mAgentBiz = new AgentBiz();
    private OrderBiz mOrderBiz = new OrderBiz();
    private TextView mConsigneeName;
    private TextView mAgentName;
    // 扫码声音
    private int soundid, sendSuccessSoundid, inputSuccessSoundid, boxCodeRepeatSoundid,
            repeatedSweepCodeSoundid, scanOtherGoodsSoundid;
    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private String barcodeStr;
    private boolean isScaning = false;
    private TextView mScanGoods;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;
    private String ActivieScan;
    private String mImie;
    private String mUid;
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
            ActivieScan = (String) SPUtils.getInstance().get("active", "");
            Log.d(TAG, "onReceive: " + ActivieScan);
            if (ActivieScan.indexOf(barcodeStr) != -1) {
                T.showToast("请勿重复扫码");
                // return;
            }
            // 已扫入列表
            Map parms = new HashMap();
            parms.put("code", barcodeStr);
            parms.put("mImie", mImie);
            mOrderBiz.orderActiveScan(parms, new CommonCallback<List<Goods>>() {
                @Override
                public void onError(Exception e) {
                    T.showToast(e.getMessage());
                    return;
                }

                @Override
                public void onSuccess(List<Goods> response, String info) {
                    if (!info.equals("ok")) {
                        if (info.indexOf("-") != -1) {
                            String code = info.split("-")[1];
                            T.showToast(info.split("-")[0]);
            /*                if (code.equals("101")) {
                                // 重复扫码
                                soundpool.play(repeatedSweepCodeSoundid, 1, 1, 1, 1, 1);
                            }*/
                            Log.d(TAG, "onSuccess: " + info);
                        } else {
                            T.showToast(info);
                        }
                    } else {
                        SPUtils.getInstance().put("active", ActivieScan + "," + barcodeStr);
                    }
                    String scanInfo = "";
                    for (int index = 0; index < response.size(); index++) {
                        scanInfo += response.get(index).getTitle() + ": " + response.get(index).getNum() + "\n";
                    }
                    mScanGoods.setText(scanInfo);
                }
            });

            mScanGoods.setText(barcodeStr);
        }

    };

    public ActiveOrderFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 创建视图
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_order, container, false);
        return view;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        mBtCheck = root.findViewById(R.id.bt_check); // 检测按钮
        mUserId = root.findViewById(R.id.id_tv_user_id);  // 代理ID
        mConsigneeName = root.findViewById(R.id.id_tv_consignee_name); // 收货人
        mAgentName = root.findViewById(R.id.id_tv_agent_name); // 代理姓名
        mScanGoods = root.findViewById(R.id.id_tv_scan_goods); // 扫入信息
        mBtSend = root.findViewById(R.id.id_bt_send); // 确认无误发货

        mImie = (String) SPUtils.getInstance().get("IMIE", "");

        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE); // 提示音
        mBtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUid = mUserId.getText().toString();
                Log.d(TAG, "onClick: " + mUid.length() + ", " + mUid.equals(""));
                if (mUid.length() == 0 || mUid.equals("")) {
                    T.showToast("请填写代理ID");
                    return;
                }
                mAgentBiz.agentInfo(mUid, new CommonCallback<Agent>() {
                    @Override
                    public void onError(Exception e) {
                        T.showToast(e.getMessage());
                        mAgentName.setText("");
                        // 收货人信息
                        mConsigneeName.setText("");
                        return;
                    }

                    @Override
                    public void onSuccess(Agent response, String info) {
                        // 代理信息
                        mAgentName.setText("姓名：" + response.getName() + " ，电话：" + response.getTel() + "\n");
                        // 收货人信息
                        mConsigneeName.setText(response.getAddress());
                        mScanGoods.setText("");
                        SPUtils.getInstance().put("mUid", mUid);
                    }
                });
            }
        });

        // 确认无误发货
        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUid = (String) SPUtils.getInstance().get("mUid", "");
                if (mUid.length() == 0 || mUid.equals("")) {
                    T.showToast("请填写代理ID");
                    return;
                }
                Map params = new HashMap();
                params.put("mUid", mUid);
                params.put("mImie", mImie);
                mOrderBiz.orderActiveSend(params, new CommonCallback<Order>() {
                    @Override
                    public void onError(Exception e) {
                        T.showToast(e.getMessage());
                        return;
                    }

                    @Override
                    public void onSuccess(Order response, String info) {
                        Log.d(TAG, "onSuccess: " + response.getOrderid());
                        T.showToast("发货完成，单号为：" + response.getOrderid());
                        SPUtils.getInstance().put("active", "");
                        mScanGoods.setText("");
                        mAgentName.setText("");
                        mConsigneeName.setText("");
                        mUserId.setText("");
                    }
                });
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        getActivity().unregisterReceiver(mScanReceiver); // 取消
    }

    @Override
    public void onResume() {
        super.onResume();
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
        // 判断当前语言种类
        if ((Boolean) SPUtils.getInstance().get(Config.LANGUAGE, false)) {
            // 粤语
            sendSuccessSoundid = soundpool.load(getActivity(), R.raw.ctsendsuccesssoundid, 1); // 发货成功
            boxCodeRepeatSoundid = soundpool.load(getActivity(), R.raw.ctboxcoderepeatsoundid, 1); //外箱码重复
            inputSuccessSoundid = soundpool.load(getActivity(), R.raw.ctinputsuccesssoundid, 1); //录入成功
            repeatedSweepCodeSoundid = soundpool.load(getActivity(), R.raw.ctrepeatedsweepcodesoundid, 1);
            // 请勿重复扫码
            scanOtherGoodsSoundid = soundpool.load(getActivity(), R.raw.ctscanothergoodssoundid, 1); //
            // 扫入其他产品
        } else {
            // 普通话
            sendSuccessSoundid = soundpool.load(getActivity(), R.raw.sendsuccesssoundid, 1); // 发货成功
            boxCodeRepeatSoundid = soundpool.load(getActivity(), R.raw.boxcoderepeatsoundid, 1); //外箱码重复
            inputSuccessSoundid = soundpool.load(getActivity(), R.raw.inputsuccesssoundid, 1); //录入成功
            repeatedSweepCodeSoundid = soundpool.load(getActivity(), R.raw.repeatedsweepcodesoundid, 1);
            // 请勿重复扫码
            scanOtherGoodsSoundid = soundpool.load(getActivity(), R.raw.scanothergoodssoundid, 1); //
            // 扫入其他产品
        }
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID
                .WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        getActivity().registerReceiver(mScanReceiver, filter); // 注册
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
