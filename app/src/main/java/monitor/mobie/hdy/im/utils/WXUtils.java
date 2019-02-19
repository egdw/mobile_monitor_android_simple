package monitor.mobie.hdy.im.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import monitor.mobie.hdy.im.model.Send;
import monitor.mobie.hdy.im.model.Token;

/**
 * Created by hdy on 2019/2/19.
 * 用于封装企业微信推送
 */

public class WXUtils {
    //保存当前的token信息
    private static String TOKEN;

    /**
     * 向企业微信发送信息
     *
     * @param corpid     企业id
     * @param corpsecret 应用密钥
     * @param agentId    应用代理id
     * @param message    需要发送的内容
     */
    public static void send(final String corpid, final String corpsecret, final String agentId, final String message) {
        if (TOKEN == null) {
            //尝试获取token
            getToken(corpid, corpsecret, agentId, message);
            return;
        }
        //把传进来的messgae转换成为json格式
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Log.e("发送的内容", JSON.toJSONString(new Send(agentId, message)));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(new Send(agentId, message)));
        Request request = new Request.Builder().url("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + TOKEN).post(requestBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.e("发送微信推送失败", request + "");
                Log.e("发送微信推送失败", request.body() + "");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                System.out.println(body);
                Token token = JSON.parseObject(body, Token.class);
                if (token.getErrcode() == 42001) {
                    //说明是token过期.
                    //进行重新获取
                    TOKEN = null;
                    send(corpid, corpsecret, agentId, message);
                } else if (token.getErrcode() == 0 && token.isOk()) {
                    //说明已经发送完成
                    Log.i("企业微信推送", "推送完成.");
                }
            }
        });
    }


    private static void getToken(final String corpid, final String corpsecret, final String agentId, final String message) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //如果请求失败了...
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Token token = JSON.parseObject(body, Token.class);
                if (token.isOk()) {
                    //说明请求成功
                    TOKEN = token.getAccess_token();
                    //重新在发送一次.
                    send(corpid, corpsecret, agentId, message);
                    Log.e("微信发送", "获取token成功" + TOKEN);
                }
            }
        });
    }
}
