package disp.moveis.luisfelipe.pratica1;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class MotorHelper {
    private final Vibrator vibrator;

    public MotorHelper(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
            VibratorManager vm = (VibratorManager) ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vm.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public void iniciarVibracao() {
        if (vibrator == null || !vibrator.hasVibrator()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+
            long[] pattern = {0, 500, 500};
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            long[] pattern = {0, 500, 500};
            vibrator.vibrate(pattern, 0);
        }
    }

    public void pararVibracao() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}