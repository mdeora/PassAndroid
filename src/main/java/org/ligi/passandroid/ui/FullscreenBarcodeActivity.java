package org.ligi.passandroid.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;

import org.ligi.axt.AXT;
import org.ligi.passandroid.R;

import static butterknife.ButterKnife.findById;

public class FullscreenBarcodeActivity extends TicketViewActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_image);
        final ImageView iv = findById(this, R.id.fullscreen_image);

        int smallestSize = AXT.at(getWindowManager()).getSmallestSide();

        setBestFittingOrientationForBarCode();

        iv.setImageBitmap(pass.getBarcodeBitmap(smallestSize));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * QR and AZTEC are best fit in Portrait
     * PDF417 is best viewed in Landscape
     * <p/>
     * main work is to avoid changing if we are already optimal
     * ( reverse orientation / sensor is the problem here ..)
     */
    private void setBestFittingOrientationForBarCode() {

        if (pass.getBarcodeFormat() == BarcodeFormat.PDF_417) {
            switch (getRequestedOrientation()) {

                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE:
                    return; // do nothing

                default:
                    AXT.at(this).lockOrientation(Configuration.ORIENTATION_LANDSCAPE);
            }

        } else { // QR and AZTEC are square -> best fit is portrait
            switch (getRequestedOrientation()) {

                case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT:
                    return; // do nothing

                default:
                    AXT.at(this).lockOrientation(Configuration.ORIENTATION_PORTRAIT);
            }

        }
    }

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

}
