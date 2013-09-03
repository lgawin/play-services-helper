
package pl.lgawin.pshelper.samples.v11;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;

import pl.lgawin.pshelper.v11.PlayServicesHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PlayServicesHelperForTests extends PlayServicesHelper {

    public static class SampleActivity extends Activity {

    }

    public Dialog dialog;

    private SharedPreferences prefs;

    public PlayServicesHelperForTests(Activity activity) {
        super(activity);
        prefs = activity.getSharedPreferences("PlayServicesHelperForTests", Context.MODE_PRIVATE);
    }

    @Override
    protected boolean checkIfAvailable(Context context) {
        return prefs.getBoolean("isAvailable", false);
    }

    public void setAvailable(boolean value) {
        Editor edit = prefs.edit();
        edit.putBoolean("isAvailable", value);
        edit.apply();
    }

    // @Override
    // protected Dialog getErrorDialog(int errorCode) {
    // if (dialog == null) {
    // throw new IllegalArgumentException("dialog not set!");
    // }
    // return dialog;
    // }

    @Override
    protected int getLastCheckStatus() {
        // SUCCESS, SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
        // SERVICE_DISABLED, SERVICE_INVALID
        // return ConnectionResult.SERVICE_MISSING;
        return ConnectionResult.SERVICE_INVALID;
    }

}
