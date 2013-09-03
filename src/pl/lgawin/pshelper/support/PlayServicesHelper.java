
package pl.lgawin.pshelper.support;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import pl.lgawin.pshelper.PlayServicesHelperBase;
import pl.lgawin.pshelper.PlayServicesHelperInterface;

public class PlayServicesHelper extends PlayServicesHelperBase implements
        PlayServicesHelperInterface {

    public static class ErrorFragment extends DialogFragment {

        private Dialog dialog;
        private OnCancelListener cancelListener;
        private OnCancelListener dismissListener;

        public void setDialog(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return dialog;
        }

        public void setOnCancel(OnCancelListener listener) {
            cancelListener = listener;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if (cancelListener != null) {
                cancelListener.onCancel(dialog);
            }
            super.onCancel(dialog);
        }

        public void setOnDismissOnce(OnCancelListener listener) {
            dismissListener = listener;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (dismissListener != null) {
                synchronized (this) {
                    if (dismissListener != null) {
                        dismissListener.onCancel(dialog);
                        dismissListener = null;
                    }
                }
            }
            super.onDismiss(dialog);
        }

        private void dismissWithoutCallback() {
            dismissListener = null;
            dismiss();
        }

    }

    private FragmentActivity activity;
    private ErrorFragment fragment;
    protected OnCancelListener defaultCancelHandler;

    public PlayServicesHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    protected OnCancelListener getOnCancelHandler() {
        if (defaultCancelHandler == null) {
            defaultCancelHandler = new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    activity.finish();
                }
            };
        }
        return defaultCancelHandler;
    }

    @Override
    public void showErrorDialog(int errorCode, OnCancelListener listener) {
        Dialog errorDialog = getErrorDialog(errorCode);
        if (errorDialog != null) {
            fragment = new ErrorFragment();
            fragment.setDialog(errorDialog);
            fragment.show(activity.getSupportFragmentManager(), ERROR_DIALOG_TAG);
            if (listener != null) {
                fragment.setOnCancel(listener);
            }
            // Hack to handle SERVICE_INVALID correctly - it doesn't call
            // another activity so onResume() check will not be performed.
            // Therefore OnCancel listener (if provided) will be use for
            // fragment's onDismiss (only for one successive call).
            if (ConnectionResult.SERVICE_INVALID == getLastCheckStatus() && listener != null) {
                fragment.setOnDismissOnce(listener);
            }
        }
    }

    protected Dialog getErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity,
                PLAY_SERVICE_ERROR_DIALOG_CODE);
        return errorDialog;
    }

    @Override
    public void hideErrorDialog() {
        if (fragment != null) {
            Dialog dialog = fragment.getDialog();
            if (dialog != null && dialog.isShowing()) {
                fragment.dismissWithoutCallback();
            }
        }
    }

    @Override
    protected Context getContext() {
        return activity;
    }

}
