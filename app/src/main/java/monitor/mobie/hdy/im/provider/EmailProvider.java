package monitor.mobie.hdy.im.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.smailnet.emailkit.Draft;
import com.smailnet.emailkit.EmailKit;
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
 * Created by hdy on 2021/09/27.
 * 用于推送email的工具类
 */
public class EmailProvider extends PushProvider {

    @Override
    public void send(String title, String text,String packageName) {
        SharedPreferences data = getContext().getSharedPreferences(Constant.DATA, Context.MODE_MULTI_PROCESS);
        boolean openSckey = data.getBoolean(Constant.EMAIL_ENABLE, true);
        String account = data.getString(Constant.EMAIL_ACCOUNT, null);
        String passwd = data.getString(Constant.EMAIL_PASSWORD, null);
        int type = Integer.valueOf(data.getString(Constant.EMAIL_TYPE, "-1"));
        // 短信接收者
        String receiver = data.getString(Constant.EMAIL_RECEIVER,null);
        if (openSckey && account != null && passwd != null && type != -1 && receiver!=null){
            String[] spilt = spilt(receiver);
            for (int i = 0; i < spilt.length; i++) {
                //初始化框架
                EmailKit.initialize(getContext());

                //配置发件人邮件服务器参数
                EmailKit.Config config = new EmailKit.Config()
                        .setMailType(EmailKit.MailType.QQ)     //选择邮箱类型，快速配置服务器参数
                        .setAccount(account)             //发件人邮箱
                        .setPassword(passwd);                   //密码或授权码

                //设置一封草稿邮件
                Draft draft = new Draft()
                        .setNickname("恶搞大王手机通知转发器")                      //发件人昵称
                        .setTo(receiver)                        //收件人邮箱
                        .setSubject(title)             //邮件主题
                        .setText(packageName + ":" + text);                 //邮件正文

                //使用SMTP服务发送邮件
                EmailKit.useSMTPService(config)
                        .send(draft, new EmailKit.GetSendCallback() {
                            @Override
                            public void onSuccess() {
                                Log.i("EmailProvider", "发送成功！");
                            }

                            @Override
                            public void onFailure(String errMsg) {
                                Log.i("EmailProvider", "发送失败，错误：" + errMsg);
                            }
                        });
            }
        }
    }

    @Override
    public void sendTest() {
        send("恶搞大王手机通知转发器通知测试","测试内容","测试内容");
    }


    @Override
    public void saveErrorProvider(int times) {
        //暂不处理

    }
}
