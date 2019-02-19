package monitor.mobie.hdy.im.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import monitor.mobie.hdy.im.R;
import monitor.mobie.hdy.im.database.AppinfosDatabase;
import monitor.mobie.hdy.im.model.AppInfo;

/**
 * Created by hdy on 28/11/2018.
 */

public class AppInfosAdapter extends BaseAdapter {

    Context context;
    List<AppInfo> appInfos;
    SQLiteDatabase writeInstance;


    public AppInfosAdapter(Context context, List<AppInfo> infos) {
        this.context = context;
        this.appInfos = infos;
        writeInstance =
                AppinfosDatabase.getWriteInstance(getContext());
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != appInfos) {
            return appInfos.size();
        }
        return count;

    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.app_infos_item, null);
            viewHolder.app_infos_img = (ImageView) convertView.findViewById(R.id.app_infos_img);
            viewHolder.app_infos_name = (TextView) convertView.findViewById(R.id.app_infos_name);
            viewHolder.app_infos_pacakge_name = (TextView) convertView.findViewById(R.id.app_infos_pacakge_name);
            viewHolder.app_infos_switch = (Switch) convertView.findViewById(R.id.app_infos_switch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (appInfos != null) {
            final AppInfo appInfo = appInfos.get(position);
            viewHolder.app_infos_img.setBackground(appInfo.getDrawable());
            viewHolder.app_infos_name.setText(appInfo.getAppName());
            viewHolder.app_infos_pacakge_name.setText(appInfo.getPackageName());
            viewHolder.app_infos_switch.setOnCheckedChangeListener(null);
            viewHolder.app_infos_switch.setChecked(appInfo.isOpen());
            viewHolder.app_infos_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    appInfo.setOpen(isChecked);
                    appInfos.set(position, appInfo);
                    if(appInfo.isOpen()){
                        AppinfosDatabase.getInstance(context).insert(writeInstance, appInfo.getPackageName());
                    }else {
                        AppinfosDatabase.getInstance(context).removeOne(writeInstance, appInfo.getPackageName());
                    }
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView app_infos_img;
        TextView app_infos_name;
        TextView app_infos_pacakge_name;
        Switch app_infos_switch;
    }
}
