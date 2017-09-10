package monitor.mobie.hdy.im.broadcastreciver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.CALL_STATE_RINGING;

/**
 * Created by hdy on 2017/9/8.
 * 拨打电话和来电监听器
 */

public class DialBroadcastReciver extends BroadcastReceiver {
    private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);

        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {

                case CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    Log.i(TAG, "RINGING :" + mIncomingNumber);
                    break;
                case CALL_STATE_OFFHOOK:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                    }
                    break;
                case CALL_STATE_IDLE:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming IDLE");
                    }
                    break;
            }
        }
    }
}
