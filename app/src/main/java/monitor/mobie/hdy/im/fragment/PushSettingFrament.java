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
import monitor.mobie.hdy.im.config.Constant;
import monitor.mobie.hdy.im.provider.BarkProvider;
import monitor.mobie.hdy.im.provider.CustomProvider;
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
        getPreferenceManager().setSharedPreferencesName(Constant.DATA);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
        addPreferencesFromResource(R.xml.preference_push);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        final SharedPreferences shp = getPreferenceManager().getSharedPreferences();
        final EditTextPreference SCKEY = (EditTextPreference) findPreference(Constant.SCKEY);
        final EditTextPreference wx_corpid = (EditTextPreference) findPreference(Constant.WX_CORPID);
        final EditTextPreference wx_corpsecret = (EditTextPreference) findPreference(Constant.WX_CORPSECRET);
        final EditTextPreference wx_agentid = (EditTextPreference) findPreference(Constant.WX_AGENTID);
        //获取自定义的请求方法
        final EditTextPreference coustom_method = (EditTextPreference) findPreference(Constant.COUSTOM_METHOD);
        //获取自定义的请求地址
        final EditTextPreference coustom_url = (EditTextPreference) findPreference(Constant.COUSTOM_URL);
        //获取备注信息
        final EditTextPreference coustom_remark = (EditTextPreference) findPreference(Constant.COUSTOM_REMARK);
        Preference findPreference = findPreference(Constant.BARK_URL);

        Preference coustom_enable = findPreference(Constant.COUSTOM_ENABLE);
        Preference sckey_enable = findPreference(Constant.SCKEY_ENABLE);
        Preference wx_enable = findPreference(Constant.WX_ENABLE);
        Preference bark_enable = findPreference(Constant.BARK_ENABLE);
        final EditTextPreference bark_urls = (EditTextPreference) findPreference;

        SCKEY.setSummary(getValue(shp.getString(Constant.SCKEY, getString(R.string.empty))));
        wx_corpid.setSummary(getValue(shp.getString(Constant.WX_CORPID, getString(R.string.empty))));
        wx_corpsecret.setSummary(getValue(shp.getString(Constant.WX_CORPSECRET, getString(R.string.empty))));
        wx_agentid.setSummary(getValue(shp.getString(Constant.WX_AGENTID, getString(R.string.empty))));
        coustom_method.setSummary(getValue(shp.getString(Constant.COUSTOM_METHOD, getString(R.string.empty))));
        coustom_url.setSummary(getValue(shp.getString(Constant.COUSTOM_URL, getString(R.string.empty))));
        coustom_remark.setSummary(getValue(shp.getString(Constant.COUSTOM_REMARK,getString(R.string.empty))));
        bark_urls.setSummary(getValue(shp.getString(Constant.BARK_URL, getString(R.string.empty))));

        findPreference(Constant.BARK_URL).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    bark_urls.setDefaultValue(PushSettingFrament.this.getString(R.string.empty));
                    return true;
                }
                bark_urls.setSummary(newValue.toString());
                return true;
            }
        });


        findPreference(Constant.COUSTOM_REMARK).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    coustom_remark.setDefaultValue(getString(R.string.empty));
                } else {
                    coustom_remark.setSummary(newValue.toString());
                }
                return true;
            }
        });
        findPreference(Constant.COUSTOM_METHOD).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    coustom_method.setDefaultValue(getString(R.string.empty));
                } else {
                    coustom_method.setSummary(newValue.toString());
                }
                return true;
            }
        });

        findPreference(Constant.COUSTOM_URL).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    coustom_url.setDefaultValue(getString(R.string.empty));
                } else {
                    coustom_url.setSummary(newValue.toString());
                }
                return true;
            }
        });

        findPreference("bark_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (!shp.getString(Constant.SCKEY, "").isEmpty()) {
                    BarkProvider provider = new BarkProvider();
                    provider.setContext(PushSettingFrament.this.getContext());
                    provider.sendTest();
                    ToastUtils.toast(PushSettingFrament.this.getContext(), "发送测试完成");
                    return true;
                }
                ToastUtils.toast(PushSettingFrament.this.getContext(), "请输入Bark的地址");
                return true;
            }
        });
        findPreference("sckey_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String sckey = shp.getString(Constant.SCKEY, "");
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

        findPreference("coustom_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String coustom_url = shp.getString(Constant.COUSTOM_URL, null);
                String coustom_method = shp.getString(Constant.COUSTOM_METHOD, null);
                String[] coustom_url_spilt = spilt(coustom_url);
                String[] coustom_method_spilt = spilt(coustom_method);
                if (coustom_url_spilt!=null && coustom_method_spilt!=null && coustom_url_spilt.length == coustom_method_spilt.length) {
                    new CustomProvider().setContext(PushSettingFrament.this.getContext()).sendTest();
                    ToastUtils.toast(PushSettingFrament.this.getContext(),"发送测试完成");
                }else{
                    if (coustom_url_spilt == null || coustom_method_spilt == null){
                        ToastUtils.toast(PushSettingFrament.this.getContext(),"请填写完整数据!");
                    }else{
                        ToastUtils.toast(PushSettingFrament.this.getContext(),"链接地址 和 请求方式 数量不一致!请重新检查!!");
                    }
                }
                return true;
            }
        });

        findPreference("wx_test").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String wxCorpid = shp.getString(Constant.WX_CORPID, null);
                String wxCorpsecret = shp.getString(Constant.WX_CORPSECRET, null);
                String wxAgentid = shp.getString(Constant.WX_AGENTID, null);
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
        coustom_enable.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                return true;
            }
        });
        SCKEY.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().isEmpty()) {
                    SCKEY.setSummary(getString(R.string.empty));
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
                    wx_corpid.setDefaultValue(getString(R.string.empty));
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
                    wx_corpsecret.setSummary(getString(R.string.empty));
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
                    wx_agentid.setSummary(getString(R.string.empty));
                } else {
                    wx_agentid.setSummary(newValue.toString());
                }
                return true;
            }
        });
    }

    private String getValue(String get) {
        if (get.isEmpty()) {
            return getString(R.string.empty);
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
