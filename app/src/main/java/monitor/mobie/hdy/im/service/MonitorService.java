package monitor.mobie.hdy.im.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import monitor.mobie.hdy.im.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by hdy on 2017/9/9.
 * 用于监听事件
 */

public class MonitorService extends Service {
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String SCKEY;
    private SharedPreferences data;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * 初始化操作
     */
    public void init() {
        data = getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        SCKEY = data.getString("SCKEY", "");
        if (SCKEY != null && !SCKEY.equals("")) {
            openNotificationAccess();
            toggleNotificationListenerService();
        }
        startForeground(this);
    }


    @Override
    public void onDestroy() {
        //进行自动重启
        Intent intent = new Intent(MonitorService.this, MonitorService.class);
        //重新开启服务
        startService(intent);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        uploadAll();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startForeground(Service context) {
        //设置常驻通知栏
        //保持为前台应用状态
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("手机监控器监听通知栏中...")
                .setContentText("")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MIN)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        Notification notification = builder.build();
        context.startForeground(8888, notification);
    }



    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
}
