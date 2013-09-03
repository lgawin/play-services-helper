
package pl.lgawin.pshelper;

import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

import com.google.android.gms.common.GooglePlayServicesClient;

public interface PlayServicesHelperInterface extends
        GooglePlayServicesClient.OnConnectionFailedListener {

    boolean checkIfAvailable();

    boolean showErrorDialogIfNotAvailable();

    boolean showErrorDialogIfNotAvailable(OnCancelListener cancelHandler);

    void showErrorDialog(int errorCode, OnCancelListener listener);

    void hideErrorDialog();

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

}
