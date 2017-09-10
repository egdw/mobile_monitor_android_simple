package monitor.mobie.hdy.im.serviceimpl;

import android.os.Binder;

import monitor.mobie.hdy.im.service.MonitorService;

/**
 * Created by hdy on 2017/9/9.
 */

public class MonitorBinder extends Binder {
    private MonitorService service;

    public MonitorBinder(MonitorService service) {
        this.service = service;
    }

    public MonitorService getService(){
        return service;
    }
}
