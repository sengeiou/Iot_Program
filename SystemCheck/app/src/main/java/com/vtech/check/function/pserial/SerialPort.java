package com.vtech.check.function.pserial;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by changyan on 17-10-2.
 *
 * 血压串口工具类
 *
 */

public class SerialPort {
    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;


    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        devOn();
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            Log.w(TAG, "can read" + device.canRead() + " canWrite " + device.canWrite());
        }
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }
    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }
    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native   void devOn();
    public native   void devOff();

    public native void close();

    static {
        //System.loadLibrary("serial_port");
        System.loadLibrary("native-lib");
    }

}
