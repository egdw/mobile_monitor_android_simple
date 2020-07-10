package monitor.mobie.hdy.im.utils;

import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

import monitor.mobie.hdy.im.R;

/**
 * Created by hdy on 2019/2/19.
 * 用于推送server酱的工具类
 */

public class ServerJiangUtils {

    public static void send(String SCKEY, String title, StringBuilder sb) {
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
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //String htmlStr =  response.body().string();

            }
        });
    }

    //用于测试
    public static void sendTest(Context context,String SCKEY){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://sc.ftqq.com/" + SCKEY + ".send?text=" + URLEncoder.encode(context.getResources().getString(R.string.test_messgae)) + "&desp=" + URLEncoder.encode("恶搞大王通知转发器测试推送"))
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //如果请求失败了...

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //String htmlStr =  response.body().string();

            }
        });
    }

}
