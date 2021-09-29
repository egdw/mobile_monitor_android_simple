package monitor.mobie.hdy.im.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.HashMap;
import java.util.regex.Pattern;

import monitor.mobie.hdy.im.database.AppinfosDatabase;
import monitor.mobie.hdy.im.provider.BarkProvider;
import monitor.mobie.hdy.im.provider.CustomProvider;
import monitor.mobie.hdy.im.provider.EmailProvider;
import monitor.mobie.hdy.im.provider.PushProvider;
import monitor.mobie.hdy.im.provider.ServerJiangProvider;
import monitor.mobie.hdy.im.provider.WxPushProvider;

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
        //亮屏推送
        boolean light = data.getBoolean("LIGHT", false);
        //是否监听所有的应用
        boolean listenAll = data.getBoolean("listenAll", true);
        //获取屏蔽关键词
        String block = data.getString("block", "");
        //获取内容
        String text = sbn.getNotification().extras.get("android.text").toString();
        //获取标题
        String title = sbn.getNotification().extras.get("android.title").toString();
        //获取推送应用的包名
        String packageName = sbn.getPackageName();
        Log.i("接收到数据", text);
        if (!block.isEmpty()) {
            //判断是否包含关键词
            boolean exist = false;
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
            //屏幕亮屏的情况下不发送任何信息.
            PowerManager powerManager = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            if (powerManager.isScreenOn()) {
                //现在是关闭了亮屏推送.那么如果当前的屏幕是点亮的话就不执行下面的代码
                return;
            }
        }
        //判断当前的app是否需要能被push
        boolean appNeedPush = isAppNeedPush(packageName, listenAll);
        if (appNeedPush) {
            try {
                //进行Server酱推送
                new ServerJiangProvider().setContext(NotificationCollectorService.this).send(title, text, packageName);

                //进行企业微信推送
                new WxPushProvider().setContext(NotificationCollectorService.this).send(title, text, packageName);

                //进行自定义推送
                new CustomProvider().setContext(NotificationCollectorService.this).send(title, text, packageName);

                //Bark 推送
                new BarkProvider().setContext(NotificationCollectorService.this).send(title, text, packageName);

                //邮箱 推送
                new EmailProvider().setContext(NotificationCollectorService.this).send(title,text,packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }


    /**
     * 判断app是否需要推送
     *
     * @param packageName 包名
     * @param listenAll   是否监听所有
     */
    private boolean isAppNeedPush(String packageName, boolean listenAll) {
        //这里要进行判断,不需要的packageName就不用通知了.
        if (!listenAll) {
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