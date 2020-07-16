package monitor.mobie.hdy.im.provider;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.config.Constant;
import monitor.mobie.hdy.im.model.Send;
import monitor.mobie.hdy.im.model.Token;

/**
 * Created by hdy on 2019/2/19.
 * 用于封装企业微信推送
 */

public class WxPushProvider extends PushProvider {
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
    private void send(final String corpid, final String corpsecret, final String agentId, final String message) {
        if (TOKEN == null) {
            //尝试获取token
            getToken(corpid, corpsecret, agentId, message);
            return;
        }
        //把传进来的messgae转换成为json格式
        OkHttpClient mOkHttpClient = new OkHttpClient();
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


    private void getToken(final String corpid, final String corpsecret, final String agentId, final String message) {
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
                }
            }
        });
    }

    public void sendTest(String corpid, final String corpsecret, final String agentId){
        String[] wx_corpid_spilt = spilt(corpid);
        String[] wx_corpsecret_spilt = spilt(corpsecret);
        String[] wx_agentid_spilt = spilt(agentId);
        if (wx_agentid_spilt!=null && wx_corpsecret_spilt!=null && wx_corpid_spilt!=null && wx_agentid_spilt.length == wx_corpsecret_spilt.length && wx_agentid_spilt.length == wx_corpid_spilt.length) {
            for (int i = 0;i<wx_agentid_spilt.length;i++) {
                send(wx_corpid_spilt[i], wx_corpsecret_spilt[i], wx_agentid_spilt[i], getContext().getResources().getString(R.string.test_messgae)+i);
            }
        }
    }


    @Override
    public void send(String title, String text,String packageName) {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        //是否打开企业微信推送
        boolean wxEnable = data.getBoolean(Constant.WX_ENABLE, true);
        if(wxEnable){
            //微信企业id
            String wx_corpid = data.getString(Constant.WX_CORPID, null);
            String[] wx_corpid_spilt = spilt(wx_corpid);
            //微信应用密钥
            String wx_corpsecret = data.getString(Constant.WX_CORPSECRET, null);
            String[] wx_corpsecret_spilt = spilt(wx_corpsecret);
            //微信应用id
            String wx_agentid = data.getString(Constant.WX_AGENTID, null);
            String[] wx_agentid_spilt = spilt(wx_agentid);

            //判断是否数据数量是否相同,如果不相同则不能提交
            if (wx_agentid_spilt!=null && wx_corpsecret_spilt!=null && wx_corpid_spilt!=null && wx_agentid_spilt.length == wx_corpsecret_spilt.length && wx_agentid_spilt.length == wx_corpid_spilt.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("标题： ").append(title).append("\r\n")
                        .append("内容：").append(text).append("\r\n")
                        .append("应用：").append(packageName).append("\r\n")
                        .append("时间：").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())).append("\r\n");
                for(int i = 0;i<wx_agentid_spilt.length;i++) {
                    if (title != null && text != null && wx_corpid != null && wx_corpsecret != null && wx_agentid != null) {
                        send(wx_corpid_spilt[i], wx_corpsecret_spilt[i], wx_agentid_spilt[i], sb.toString());
                    }
                }
            }
        }
    }

    @Override
    public void sendTest() {
        SharedPreferences data = getContext().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        String wx_corpid = data.getString(Constant.WX_CORPID, null);
        //微信应用密钥
        String wx_corpsecret = data.getString(Constant.WX_CORPSECRET, null);
        //微信应用id
        String wx_agentid = data.getString(Constant.WX_AGENTID, null);
        sendTest(wx_corpid,wx_corpsecret,wx_agentid);
    }
}
