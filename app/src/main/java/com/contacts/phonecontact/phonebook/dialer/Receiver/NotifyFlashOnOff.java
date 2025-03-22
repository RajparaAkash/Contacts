package com.contacts.phonecontact.phonebook.dialer.Receiver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.SystemClock;

public class NotifyFlashOnOff implements Runnable {

    public int time_on = 200;
    boolean isFlashAvailable;
    private boolean finished = false;
    private boolean isFlashOn = false;
    private int repeat = 0;
    private int time_off = 200;
    private CameraManager mCameraManager;
    private String mCameraId;

    public NotifyFlashOnOff(Context context, int paramInt1, int paramInt2, int paramInt3) {
        this.time_on = paramInt1;
        this.time_off = paramInt2;
        this.repeat = paramInt3;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        isFlashAvailable = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        try {
            if (mCameraManager != null) {
                mCameraId = mCameraManager.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return !finished;
    }

    public void run() {
        finished = false;
        if (repeat == 0) {
            while (true) {
                if (finished) {
                    offFlash();
                } else if (!finished) {
                    try {
                        onFlash();
                        SystemClock.sleep((long) time_on);
                        offFlash();
                        SystemClock.sleep((long) time_off);
                    } catch (Exception e) {
                    }
                }
            }
        } else if (repeat != 0) {
            for (int i = 0; i < repeat; i++) {
                try {
                    onFlash();
                    SystemClock.sleep((long) time_on);
                    offFlash();
                    SystemClock.sleep((long) time_off);
                } catch (Exception e2) {
                    return;
                }
            }
        }
    }

    public void set_repeat(int param) {
        this.repeat = param;
    }

    public void set_timeoff(int param) {
        this.time_off = param;
    }

    public void set_timeon(int param) {
        this.time_on = param;
    }

    public void stop() {
        finished = true;
    }

    public void start() {
        finished = false;
    }

    public void onFlash() {
        try {
            if (mCameraManager != null) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void offFlash() {
        try {
            if (mCameraManager != null) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
