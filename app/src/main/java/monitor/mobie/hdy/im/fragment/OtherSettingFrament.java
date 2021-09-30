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
 * Created by hdy on 2021/9/29.
 * 其他设置fragment
 */

public class OtherSettingFrament extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("data");
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
        addPreferencesFromResource(R.xml.preference_others);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

}
