
package pl.lgawin.pshelper.samples.v11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import pl.lgawin.pshelper.PlayServicesHelperBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayServicesHelperTestActivity extends Activity {

    private PlayServicesHelperForTests psh;

    private AtomicBoolean initailzedOnce = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        psh = new PlayServicesHelperForTests(this);
        psh.dialog = new
                AlertDialog.Builder(getContext()).setTitle("Title").setMessage("Message")
                        .setPositiveButton("Install", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                psh.setAvailable(true);
                                Intent intent = new Intent(getContext(),
                                        PlayServicesHelperForTests.SampleActivity.class);
                                startActivityForResult(intent,
                                        PlayServicesHelperBase.PLAY_SERVICE_ERROR_DIALOG_CODE);
                            }
                        })
                        .create();

        if (!psh.checkIfAvailable()) {
            return;
        }
        initializePlayServicesRelatedOnce();
    }

    @Override
    protected void onPause() {
        super.onPause();
        psh.hideErrorDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!psh.showErrorDialogIfNotAvailable()) {
            return;
        }
        initializePlayServicesRelatedOnce();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (psh.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializePlayServicesRelatedOnce() {
        if (initailzedOnce.getAndSet(true)) {
            return;
        }
        // some GooglePlayServices-related initialization
        Toast.makeText(getContext(), "Google play services is available", Toast.LENGTH_SHORT)
                .show();
    }

    private Context getContext() {
        return this;
    }

}
