package monitor.mobie.hdy.im.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import monitor.mobie.hdy.im.database.AppinfosDatabase;
import monitor.mobie.hdy.im.utils.ServerJiangUtils;
import monitor.mobie.hdy.im.utils.WXUtils;

/**
 * 在这里可以监听数据
 *
 * @author egdw
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationCollectorService extends NotificationListenerService {

    //当接受到新的通知信息时就会调用该函数
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        SharedPreferences data = getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
//        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        String SCKEY = data.getString("SCKEY", "");

        //微信企业id
        String wx_corpid = data.getString("wx_corpid", "");
        //微信应用密钥
        String wx_corpsecret = data.getString("wx_corpsecret", "");
        //微信应用id
        String wx_agentid = data.getString("wx_agentid", "");
        //亮屏推送
        boolean light = data.getBoolean("LIGHT", false);
        //是否打开server酱推送
        boolean openSckey = data.getBoolean("SCKEY_enable", true);
        //是否打开企业微信推送
        boolean wxEnable = data.getBoolean("wx_enable", true);
        //是否监听所有的应用
        boolean listenAll = data.getBoolean("listenAll", true);
        //获取屏蔽关键词
        String block = data.getString("block", "");
        boolean ifOpen = false;

        if (!block.isEmpty()) {
            //判断是否包含关键词
            boolean exist = false;
            String text = sbn.getNotification().extras.get("android.text").toString();
            String title = sbn.getNotification().extras.get("android.title").toString();
            String[] split = block.split("\n");
            for (int i = 0; i < split.length; i++) {
                if (Pattern.matches(split[i], text) || text.contains(split[i])) {
                    exist = true;
                    break;
                }
                if (Pattern.matches(split[i], title) || title.contains(split[i])) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                //如果存在关键词就不推送了!
                return;
            }
        }

        //新增支持是否点亮屏幕也推送数据.默认为关闭
        if (!light) {
//            屏幕亮屏的情况下不发送任何信息.
            PowerManager powerManager = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            ifOpen = powerManager.isScreenOn();
            if (ifOpen) {
                //现在是关闭了亮屏推送.那么如果当前的屏幕是点亮的话就不执行下面的代码
                return;
            }
        }

        try {
            //进行Server酱推送
            if (SCKEY != null && !SCKEY.equals("") && openSckey) {
                sendServerJiang(sbn, listenAll, SCKEY);
            }


            //进行企业微信推送
            if (!wx_corpid.isEmpty() && !wx_agentid.isEmpty() && !wx_corpsecret.isEmpty() && wxEnable) {
                sendWX(sbn, listenAll, wx_corpid, wx_agentid, wx_corpsecret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendServerJiang(StatusBarNotification sbn, boolean listenAll, String SCKEY) {
        if (isAppNeedPush(sbn, listenAll)) {
            if (sbn.getNotification().extras.get("android.text") != null && sbn.getNotification().extras.get("android.title") != null) {
                String text = sbn.getNotification().extras.get("android.text").toString();
                String title = sbn.getNotification().extras.get("android.title").toString();
                StringBuilder sb = new StringBuilder();
                sb.append("# ").append(title).append("\r\n")
                        .append("## ").append(text).append("\r\n");
                ServerJiangUtils.send(SCKEY, title, sb);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendWX(StatusBarNotification sbn, boolean listenAll, String wx_corpid, String wx_agentid, String wx_corpsecret) {
        if (isAppNeedPush(sbn, listenAll)) {
            if (sbn.getNotification().extras.get("android.text") != null && sbn.getNotification().extras.get("android.title") != null) {
                String text = sbn.getNotification().extras.get("android.text").toString();
                String title = sbn.getNotification().extras.get("android.title").toString();
                String packageName = sbn.getPackageName();
                StringBuilder sb = new StringBuilder();

                sb.append("标题： ").append(title).append("\r\n")
                        .append("内容：").append(text).append("\r\n")
                        .append("应用：").append(packageName).append("\r\n")
                        .append("时间：").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())).append("\r\n");
                WXUtils.send(wx_corpid, wx_corpsecret, wx_agentid, sb.toString());
            }
        }
    }

    /**
     * 判断app是否需要推送
     *
     * @param sbn
     * @param listenAll
     */
    private boolean isAppNeedPush(StatusBarNotification sbn, boolean listenAll) {
        //这里要进行判断,不需要的packageName就不用通知了.
        if (!listenAll) {
            String packageName = sbn.getPackageName();
            //如果不是全部监听的话
            //这里判断当前的包名是否和用户勾选的应用包名相同.如果相同的话就进行通知
            //如果不相同就跳过.避免不必要的通知.

            SQLiteDatabase database = AppinfosDatabase.getReadInstance(this);
            HashMap<String, Object> infos = AppinfosDatabase.getInstance(this).selectAll(database);
            if (!infos.containsKey(packageName)) {
                return false;
            }
        }
        return true;
    }
}