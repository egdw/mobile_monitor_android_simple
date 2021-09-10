package monitor.mobie.hdy.im.provider;

import android.content.SharedPreferences;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URLEncoder;
import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.config.Constant;


public class BarkProvider extends PushProvider {
    public void send(String title, String text, String packageName) {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, 4);
        if (data.getBoolean(Constant.BARK_ENABLE, true)) {
            String url = data.getString(Constant.BARK_URL, (String) null);
            new OkHttpClient().newCall(new Request.Builder().url(url + URLEncoder.encode(title + "," + packageName + "," + text)).build()).enqueue(new Callback() {
                public void onFailure(Request request, IOException e) {
                    BarkProvider.this.saveErrorProvider(0);
                }

                public void onResponse(Response response) throws IOException {
                }
            });
        }
    }

    public void sendTest() {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, 4);
        if (data.getBoolean(Constant.BARK_ENABLE, true)) {
            String[] spilt = spilt(data.getString(Constant.BARK_URL, (String) null));
            for (int i = 0; i < spilt.length; i++) {
                spilt[i] = spilt[i] + URLEncoder.encode(new StringBuilder(getContext().getResources().getString(R.string.test_messgae)).toString());
                new OkHttpClient().newCall(new Request.Builder().url(spilt[i]).build()).enqueue(new Callback() {
                    public void onFailure(Request request, IOException e) {
                        BarkProvider.this.saveErrorProvider(0);
                    }

                    public void onResponse(Response response) throws IOException {
                    }
                });
            }
        }
    }
}