package monitor.mobie.hdy.im.broadcastreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import monitor.mobie.hdy.im.service.MonitorService;

/**
 * Created by hdy on 2017/9/10.
 */

public class BootBroadcastReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MonitorService.class);
        context.startService(serviceIntent);
    }

}
