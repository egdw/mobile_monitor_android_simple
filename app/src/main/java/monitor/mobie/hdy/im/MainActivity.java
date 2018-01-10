package monitor.mobie.hdy.im;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import monitor.mobie.hdy.im.model.Reply;
import monitor.mobie.hdy.im.service.MonitorService;
import monitor.mobie.hdy.im.service.NotificationCollectorService;
import monitor.mobie.hdy.im.utils.Constants;
import monitor.mobie.hdy.im.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    private MonitorService monitorService;
    private boolean flag;
    private Button button;
    private FloatingActionButton fab;
    private ToastUtils toastUtils;
    private SharedPreferences data;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        requirePermission();
        if (serviceIntent == null) {
            System.out.println("启动服务");
            serviceIntent = new Intent(MainActivity.this, MonitorService.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(serviceIntent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(serviceIntent);
    }


    private void init() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginDisplay();
            }
        });
        toastUtils = new ToastUtils(MainActivity.this);
        data = getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        edit = data.edit();
        if (!isNotificationListenerServiceEnabled(this)) {
            Toast.makeText(this, "请先勾选手机监听器的读取通知栏权限!", Toast.LENGTH_LONG).show();
            return;
        }
        this.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Uri uri = Uri.parse("http://sc.ftqq.com/3.version");
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNotificationListenerServiceEnabled(this)) {
            openNotificationAccess();
            toggleNotificationListenerService();
            Toast.makeText(this, "请先勾选手机监听器的读取通知栏权限!", Toast.LENGTH_LONG).show();
            return;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * 获取短信数据
     */
    private void requirePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 3);
            }
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 2);
            }
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
            return;
        }
    }

    private void openLoginDisplay() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.login_layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        final EditText password = (EditText) view.findViewById(R.id.editText_password);
        final EditText SCKEY_input = (EditText) view.findViewById(R.id.sckey_input);
        //        获取QQname
        final EditText qq_name = (EditText) view.findViewById(R.id.qq_name);
        // 获取email_input
        final EditText email_input = (EditText) view.findViewById(R.id.email_input);
        final EditText phone = (EditText) view.findViewById(R.id.editText_phone);
        phone.setText(data.getString("phone", ""));
        password.setText(data.getString("password", ""));
        SCKEY_input.setText(data.getString("SCKEY", ""));
        qq_name.setText(data.getString("qq_name", ""));
        email_input.setText(data.getString("email_input", ""));
        Button button = (Button) view.findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里进行提交登录
                if (!isNetworkAvailable(MainActivity.this)) {
                    //说明没有网络
                    if (handler != null) {
                        handler.sendEmptyMessage(0x2);
                    }
                }
                final String number = phone.getText().toString();
                final String passwd = password.getText().toString();
                if (number.isEmpty() || passwd.isEmpty()) {
                    toastUtils.toast("输入框不能为空!");
                    return;
                } else {
                    OkHttpClient client = new OkHttpClient();
                    final FormBody formBody = new FormBody.Builder().add("phoneNum", number).add("password", passwd).build();
                    Request request = new Request.Builder()
                            .url(Constants.URL + "/register")
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Reader reader = response.body().charStream();
                            char[] chars = new char[512];
                            int len = -1;
                            StringBuilder sb = new StringBuilder();
                            while ((len = reader.read(chars)) != -1) {
                                sb.append(new String(chars, 0, len));
                            }
                            reader.close();
                            Reply reply = JSON.parseObject(sb.toString(), Reply.class);
                            System.out.println(reply);
                            Message message = handler.obtainMessage();
                            message.what = 0x3;
                            message.obj = reply.getMessage();
                            if (reply == null) {
                                handler.sendEmptyMessage(0x2);
                            } else {
                                if (reply.getCode() == 200) {
                                    handler.sendMessage(message);
                                    edit.putString("phone", number).commit();
                                    edit.putString("password", passwd).commit();
                                    edit.putString("SCKEY", SCKEY_input.getText().toString()).commit();
                                    edit.putString("qq_name", qq_name.getText().toString()).commit();
                                    edit.putString("email_input", email_input.getText().toString()).commit();
                                    serviceIntent = new Intent(MainActivity.this, MonitorService.class);
                                    serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startService(serviceIntent);
                                    System.out.println("service start");
                                    dialog.dismiss();
                                } else {
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            toastUtils.toast("必须需要读取通话记录权限!!!");
        } else if (requestCode == 2) {
            toastUtils.toast("必须需要读取短信权限!!!");
        } else if (requestCode == 3) {
            toastUtils.toast("必须需要读取网络权限!!!");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 0x1:
                    //全部上传成功!
                    toastUtils.toast("恭喜您,同步成功!");
                    button.setEnabled(true);
                    break;
                case 0x3:
                    String info = (String) msg.obj;
                    toastUtils.toast(info);
                    break;
                case 0x4:
                    toastUtils.toast("登录失败!请检查网络是否已连接成功!");
                    break;
                default:
                    //全部上传成功!
                    toastUtils.toast("同步失败!请检查网络是否已连接成功!");
                    button.setEnabled(true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    private static boolean isNotificationListenerServiceEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

}
