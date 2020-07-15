package monitor.mobie.hdy.im.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;

import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.provider.PushProvider;
import monitor.mobie.hdy.im.provider.ServerJiangProvider;
import monitor.mobie.hdy.im.utils.ToastUtils;
import monitor.mobie.hdy.im.provider.WxPushProvider;

/**
 * Created by hdy on 2019/2/18.
 * 推送通知设置fragment
 */

public class PushSettingFrament extends PreferenceFragment {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("data");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
        addPreferencesFromResource(R.xml.preference_push);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        final SharedPreferences shp = getPreferenceManager().getSharedPreferences();
        final EditTextPreference SCKEY = (EditTextPreference) findPreference("SCKEY");
        final EditTextPreference wx_corpid = (EditTextPreference) findPreference("wx_corpid");
        final EditTextPreference wx_corpsecret = (EditTextPreference) findPreference("wx_corpsecret");
        final EditTextPreference wx_agentid = (EditTextPreference) findPreference("wx_agentid");
        Preference sckey_enable = findPreference("SCKEY_enable");
        Preference wx_enable = findPreference("wx_enable");
        SCKEY.setSummary(getValue(shp.getString("SCKEY", "当前为空")));
        wx_corpid.setSummary(getValue(shp.getString("wx_corpid", "当前为空")));
        wx_corpsecret.setSummary(getValue(shp.getString("wx_corpsecret", "当前为空")));
        wx_agentid.setSummary(getValue(shp.getString("wx_agentid", "当前为空")));

        findPreference("sckey_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String sckey = shp.getString("SCKEY", "");
                if (!sckey.isEmpty()) {
                    ServerJiangProvider provider = new ServerJiangProvider();
                    provider.setContext(PushSettingFrament.this.getContext());
                    provider.sendTest();
                    ToastUtils.toast(PushSettingFrament.this.getContext(),"发送测试完成");
                } else {
                    ToastUtils.toast(PushSettingFrament.this.getContext(),"请输入SCKEY");
                }
                return true;
            }
        });

        findPreference("wx_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String wxCorpid = shp.getString("wx_corpid", "");
                String wxCorpsecret = shp.getString("wx_corpsecret", "");
                String wxAgentid = shp.getString("wx_agentid", "");
                String[] wx_corpid_spilt = spilt(wxCorpid);
                String[] wx_corpsecret_spilt = spilt(wxCorpsecret);
                String[] wx_agentid_spilt = spilt(wxAgentid);
                if (wx_agentid_spilt!=null && wx_corpsecret_spilt!=null && wx_corpid_spilt!=null && wx_agentid_spilt.length == wx_corpsecret_spilt.length && wx_agentid_spilt.length == wx_corpid_spilt.length) {
                    if (!wxCorpid.isEmpty() && !wxCorpsecret.isEmpty() && !wxAgentid.isEmpty()) {
                        PushProvider provider = new WxPushProvider();
                        provider.setContext(PushSettingFrament.this.getContext());
                        provider.sendTest();
                        ToastUtils.toast(PushSettingFrament.this.getContext(),"发送测试完成");
                    } else {
                        ToastUtils.toast(PushSettingFrament.this.getContext(),"请填写完整数据!");
                    }
                    return true;
                }
                if (wx_agentid_spilt == null || wx_corpid_spilt == null || wx_corpsecret_spilt == null){
                    ToastUtils.toast(PushSettingFrament.this.getContext(),"请填写完整数据!");
                }else{
                    ToastUtils.toast(PushSettingFrament.this.getContext(),"企业编号 企业密钥 应用编号 数量不一致!请重新检查!!");
                }
                return false;
            }
        });

        sckey_enable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });
        wx_enable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });
        SCKEY.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    SCKEY.setSummary("当前为空");
                } else {
                    SCKEY.setSummary(newValue.toString());
                }
                return true;
            }
        });
        wx_corpid.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    wx_corpid.setDefaultValue("当前为空");
                } else {
                    wx_corpid.setSummary(newValue.toString());
                }
                return true;
            }
        });
        wx_corpsecret.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    wx_corpsecret.setSummary("当前为空");
                } else {
                    wx_corpsecret.setSummary(newValue.toString());
                }
                return true;
            }
        });
        wx_agentid.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    wx_agentid.setSummary("当前为空");
                } else {
                    wx_agentid.setSummary(newValue.toString());
                }
                return true;
            }
        });
    }

    private String getValue(String get) {
        if (get.isEmpty()) {
            return "当前为空";
        }
        return get;
    }

    /**
     * 切分多个换行输入
     * @param context 输入的文本
     * @return
     */
    private String[] spilt(String context){
        if(context!=null){
            return context.trim().split("\n");
        }
        return null;
    }
}
