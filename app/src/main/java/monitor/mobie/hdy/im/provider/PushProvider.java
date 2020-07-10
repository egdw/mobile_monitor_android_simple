package monitor.mobie.hdy.im.provider;

import android.content.Context;

public abstract class PushProvider {

    private Context context;

    public PushProvider(){

    }

    public PushProvider(Context context){
        this.context = context;
    }

    //发送调用接口
    public void send(String title, String text,String packageName) {

    }

    //发送调用接口
    public void sendTest() {

    }

    public void setContext(Context context){
        this.context = context;
    }

    Context getContext(){
        return this.context;
    }


    //如果出现错误将会调用这个函数
    void saveErrorProvider(int times) {

    }

}
