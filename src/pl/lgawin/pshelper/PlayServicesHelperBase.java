
package pl.lgawin.pshelper;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public abstract class PlayServicesHelperBase implements PlayServicesHelperInterface {

    public static final int PLAY_SERVICE_ERROR_DIALOG_CODE = 6354;
    protected static final String ERROR_DIALOG_TAG = "error-dialog";
    private Integer serviceStatus;

    public PlayServicesHelperBase() {
        super();
    }

    abstract protected Context getContext();

    @Override
    public boolean checkIfAvailable() {
        return checkIfAvailable(getContext());
    }

    @Override
    public boolean showErrorDialogIfNotAvailable() {
        return showErrorDialogIfNotAvailable(getOnCancelHandler());
    }

    @Override
    public boolean showErrorDialogIfNotAvailable(OnCancelListener cancelHandler) {
        if (checkIfAvailable(getContext())) {
            return true;
        }
        if (cancelHandler != null) {
            showErrorDialog(getLastCheckStatus(), cancelHandler);
        }
        return false;
    }

    protected abstract OnCancelListener getOnCancelHandler();

    protected boolean checkIfAvailable(Context context) {
        serviceStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == getLastCheckStatus()) {
            // log
            return true;
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlayServicesHelperBase.PLAY_SERVICE_ERROR_DIALOG_CODE) {
            // Actually nothing needs to be done, as another check is handled by
            // activity's onResume() method
            return true;
        }
        return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            showErrorDialog(result.getErrorCode(), null);
        }
    }

    protected int getLastCheckStatus() {
        if (serviceStatus == null) {
            throw new IllegalStateException(
                    "Call checkIfAvailable() or showErrorDialogIfNotAvailable(OnCancelListener) first!");
        }
        return serviceStatus;
    }

}
