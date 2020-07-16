package monitor.mobie.hdy.im.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URLEncoder;
import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.config.Constant;

/**
 * Created by hdy on 2020/07/10.
 * 用于推送server酱的工具类
 */
public class ServerJiangProvider extends PushProvider {


    private void send(String SCKEY, String title, StringBuilder sb) {
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
                saveErrorProvider(0);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
            }
        });
    }

    //用于测试
    private void sendTest(String SCKEY){
        String[] spilt = spilt(SCKEY);
        for (int i = 0;i<spilt.length;i++) {
            send(spilt[i], "恶搞大王通知转发器Server酱测试推送"+i, new StringBuilder(getContext().getResources().getString(R.string.test_messgae)));
        }
    }

    @Override
    public void send(String title, String text,String packageName) {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        boolean openSckey = data.getBoolean(Constant.SCKEY_ENABLE, true);
        if (openSckey){
            String SCKEY = data.getString(Constant.SCKEY, null);
            String[] spilt = spilt(SCKEY);
            StringBuilder sb = new StringBuilder();
            sb.append("# ").append(title).append("\r\n")
                    .append("## ").append(text).append("\r\n");
            for (String sckey:spilt) {
                if (sckey != null && title != null && text != null) {
                    send(sckey, title, sb);
                }
            }
        }
    }

    @Override
    public void sendTest() {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        String SCKEY = data.getString(Constant.SCKEY, "");
        sendTest(SCKEY);
    }


    @Override
    public void saveErrorProvider(int times) {
        //暂不处理

    }
}
