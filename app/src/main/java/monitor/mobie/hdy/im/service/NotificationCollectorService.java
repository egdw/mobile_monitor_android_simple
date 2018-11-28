package monitor.mobie.hdy.im.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 在这里可以监听数据
 * @author egdw
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationCollectorService extends NotificationListenerService {

    //当接受到新的通知信息时就会调用该函数
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        SharedPreferences data = getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        String SCKEY = data.getString("SCKEY", "");
        boolean light = data.getBoolean("LIGHT", false);
        boolean ifOpen = false;
        //新增支持是否点亮屏幕也推送数据.默认为关闭
        if (light) {
//            屏幕亮屏的情况下不发送任何信息.
            PowerManager powerManager = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            ifOpen = powerManager.isScreenOn();
            if(ifOpen){
                //现在是关闭了亮屏推送.那么如果当前的屏幕是点亮的话就不执行下面的代码
                return;
            }
        }

        if (SCKEY != null && !SCKEY.equals("")) {
            try {
                String packageNmae = sbn.getPackageName();
//            String tickerText = sbn.getNotification().tickerText.toString();
                if (sbn.getNotification().extras.get("android.text") != null && sbn.getNotification().extras.get("android.title") != null) {
                    String text = sbn.getNotification().extras.get("android.text").toString();
                    String title = sbn.getNotification().extras.get("android.title").toString();
                    StringBuilder sb = new StringBuilder();
                    sb.append("# ").append(title).append("\r\n")
                            .append("## ").append(text).append("\r\n");
                    //创建okHttpClient对象
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    //创建一个Request
                    //这里需要进行修改.
                    final Request request = new Request.Builder()
                            .url("https://sc.ftqq.com/" + SCKEY + ".send?text=" + URLEncoder.encode(title) + "&desp=" + URLEncoder.encode(sb.toString()))
                            .build();
                    //new call
                    Call call = mOkHttpClient.newCall(request);
                    //请求加入调度
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            //如果请求失败了...
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {
                            //String htmlStr =  response.body().string();

                        }
                    });
                }
            } catch (Exception e) {
                //如果出现异常.说明信息推送有问题.放入数据库当中存放.
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}