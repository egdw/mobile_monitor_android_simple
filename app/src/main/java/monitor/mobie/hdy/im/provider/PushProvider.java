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

    public PushProvider setContext(Context context){
        this.context = context;
        return this;
    }

    Context getContext(){
        return this.context;
    }


    //如果出现错误将会调用这个函数
    void saveErrorProvider(int times) {

    }


    /**
     * 切分多个换行输入
     * @param context 输入的文本
     * @return
     */
    public String[] spilt(String context){
        if(context!=null){
            return context.trim().split("\n");
        }
        return null;
    }

}
