package monitor.mobie.hdy.im.broadcastreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import monitor.mobie.hdy.im.service.MonitorService;

/**
 * Created by hdy on 2017/9/10.
 */

public class BootBroadcastReciver extends BroadcastReceiver {
    private MonitorService monitorService;
    private boolean flag;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("手机开机了....");
        Intent serviceIntent = new Intent(context, MonitorService.class);
        context.startService(serviceIntent);
    }
}
