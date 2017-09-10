package monitor.mobie.hdy.im.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hdy on 2017/9/10.
 */

public class ToastUtils {
    private Toast toast;
    private Context context;

    public ToastUtils(Context context) {
        this.context = context;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public void toast(String message) {
        toast.setText(message);
        toast.show();
    }
}
