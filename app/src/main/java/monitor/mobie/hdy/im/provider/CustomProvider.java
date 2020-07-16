package monitor.mobie.hdy.im.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.config.Constant;

/**
 * 用户自定义请求链接提供者
 */
public class CustomProvider extends PushProvider {

    private void sendGet(String title, String text, String packageName, String remark, MessageModel model) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        StringBuilder sb = new StringBuilder(model.getUrl());
        sb.append("?").append("title=").append(title).append("&text=").append(text).append("&packageName=").append(packageName).append("&remark=").append(remark).append("&date=").append(Constant.FORMATTER.format(new Date()));
        final Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //如果请求失败了...
                Log.i("自定义请求","发送自定义请求失败!");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("自定义请求","发送自定义请求成功!"+response);
            }
        });
    }

    private void sendPostOrOther(String title, String text, String packageName, String remark, MessageModel model) {
        OkHttpClient client = new OkHttpClient();
        //构建表单参数
        FormEncodingBuilder builder = new FormEncodingBuilder();
        //添加请求体
        RequestBody requestBody = builder
                .add("title", title)
                .add("text", text)
                .add("packageName", packageName)
                .add("remark", remark)
                .add("date", Constant.FORMATTER.format(new Date()))
                .build();

        Request request = new Request.Builder()
                .url(model.getUrl())
                .post(requestBody)
                .build();
        //异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("自定义请求","发送自定义请求失败!");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("自定义请求","发送自定义请求成功!");
            }
        });

    }

    private void send(String title, String text, String packageName, String remark, List<MessageModel> urls) {
        for (int i = 0; i < urls.size(); i++) {
            MessageModel model = urls.get(i);
            if (Constant.GET.equals(model.getMethod())) {
                sendGet(title, text, packageName, remark, model);
            } else {
                sendPostOrOther(title, text, packageName, remark, model);
            }
        }
    }


    @Override
    public void send(String title, String text, String packageName) {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        boolean coustom_enable = data.getBoolean(Constant.COUSTOM_ENABLE, false);
        if (coustom_enable) {
            String coustom_url = data.getString(Constant.COUSTOM_URL, null);
            String coustom_method = data.getString(Constant.COUSTOM_METHOD, null);
            String coutome_remark = data.getString(Constant.COUSTOM_REMARK, "");
            if (coustom_url != null && coustom_method != null) {
                coustom_url = coustom_url.trim();
                //去除前后空格并转换为大小写
                coustom_method = coustom_method.trim().toUpperCase();
                String[] coustom_url_spilt = coustom_url.split("\n");
                String[] coustom_method_spilt = coustom_method.split("\n");
                if (coustom_method_spilt.length == coustom_url_spilt.length) {
                    List<MessageModel> modelList = new ArrayList<>();
                    for (int i = 0; i < coustom_method_spilt.length; i++) {
                        MessageModel model = new MessageModel();
                        model.setUrl(coustom_url_spilt[i]);
                        model.setMethod(coustom_method_spilt[i]);
                        modelList.add(model);
                    }
                    send(title, text, packageName, coutome_remark, modelList);
                }
            }
        }
    }

    @Override
    public void sendTest() {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        String coustom_url = data.getString(Constant.COUSTOM_URL, null);
        String coustom_method = data.getString(Constant.COUSTOM_METHOD, null);
        String coustom_remark = data.getString(Constant.COUSTOM_REMARK, "");
        if (coustom_url != null && coustom_method != null) {
            coustom_url = coustom_url.trim();
            //去除前后空格并转换为大小写
            coustom_method = coustom_method.trim().toUpperCase();
            String[] coustom_url_spilt = coustom_url.split("\n");
            String[] coustom_method_spilt = coustom_method.split("\n");
            if (coustom_method_spilt.length == coustom_url_spilt.length) {
                List<MessageModel> modelList = new ArrayList<>();
                for (int i = 0; i < coustom_method_spilt.length; i++) {
                    MessageModel model = new MessageModel();
                    model.setUrl(coustom_url_spilt[i]);
                    model.setMethod(coustom_method_spilt[i]);
                    modelList.add(model);
                }
                send(getContext().getString(R.string.app_name), getContext().getString(R.string.test_messgae), "im.hdy.monitor", coustom_remark, modelList);
            }
        }
    }

    class MessageModel {
        private String url;
        private String method;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}