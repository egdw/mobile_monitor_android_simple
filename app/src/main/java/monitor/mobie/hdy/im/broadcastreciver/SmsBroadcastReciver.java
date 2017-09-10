package monitor.mobie.hdy.im.broadcastreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

/**
 * Created by hdy on 2017/9/8.
 * 短信广播监听器
 */

public class SmsBroadcastReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        //判断广播消息
        if (action.equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            //如果不为空
            if (bundle != null) {
                //将pdus里面的内容转化成Object[]数组
                Object pdusData[] = (Object[]) bundle.get("pdus");// pdus ：protocol data unit  ：
                //解析短信
                SmsMessage[] msg = new SmsMessage[pdusData.length];
                for (int i = 0; i < msg.length; i++) {
                    byte pdus[] = (byte[]) pdusData[i];
                    msg[i] = SmsMessage.createFromPdu(pdus);
                }
                StringBuffer content = new StringBuffer();//获取短信内容
                StringBuffer phoneNumber = new StringBuffer();//获取地址
                //分析短信具体参数
                for (SmsMessage temp : msg) {
                    content.append(temp.getMessageBody());
                    phoneNumber.append(temp.getOriginatingAddress());
                }

                System.out.println("发送者号码：" + phoneNumber.toString() + "  短信内容：" + content.toString());
            }
        }
    }
}
