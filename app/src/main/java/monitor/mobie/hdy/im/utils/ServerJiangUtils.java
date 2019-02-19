package monitor.mobie.hdy.im.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

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
    public static void sendTest(String SCKEY){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        //这里需要进行修改.
        Random random = new Random();
        final Request request = new Request.Builder()
                .url("https://sc.ftqq.com/" + SCKEY + ".send?text=" + URLEncoder.encode("测试发送"+random.nextDouble()) + "&desp=" + URLEncoder.encode("测试发送"+random.nextDouble()))
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
