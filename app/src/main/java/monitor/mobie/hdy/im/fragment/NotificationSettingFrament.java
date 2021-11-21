package monitor.mobie.hdy.im.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import monitor.mobie.hdy.im.MainActivity;
import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.database.AppinfosDatabase;

/**
 * Created by hdy on 2019/2/18.
 * 通知设置fragment
 */

public class NotificationSettingFrament extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("data");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
        addPreferencesFromResource(R.xml.preference_notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        final SQLiteDatabase writeInstance =
                AppinfosDatabase.getWriteInstance(getActivity());
        SwitchPreference listenAll = (SwitchPreference) findPreference("customListen");
        listenAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean b = (Boolean) newValue;
                MainActivity mainActivity = (MainActivity) getActivity();
                if (b) {
                    AppinfosDatabase.getInstance(getActivity()).removeAll(writeInstance);
                    mainActivity.myHandler.sendEmptyMessage(0x3);
                } else {
                    mainActivity.myHandler.sendEmptyMessage(0x2);
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

}
