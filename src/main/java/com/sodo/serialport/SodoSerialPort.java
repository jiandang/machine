package com.sodo.serialport;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 调用JNI的串口
 *
 * @author dwin
 */
public class SodoSerialPort extends SerialPort {

    private static final String TAG = "SerialPort";

    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private boolean isOpen = false;
    private Context context;
    private static SodoSerialPort m_Instance = null;
    public static synchronized SodoSerialPort getInstance() {
        if (null == m_Instance) {
            m_Instance = new SodoSerialPort();
        }
        return m_Instance;
    }

    public void initialize(Context context) {
        this.context = context;
    }
    public boolean open(String devNum, int speed, int dataBits, int stopBits,
                        int parity) {
        isOpen = false;

        mDevNum = devNum;
        mDataBits = dataBits;
        mSpeed = speed;
        mStopBits = stopBits;
        mParity = parity;
        Log.e(TAG, "------------>>"+devNum);
        // 打开串口
        mFd = openDev(devNum);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
//            TcnUtility.getToast(context,"native open returns null");
            return false;
        } else {
            setSpeed(speed);
            setParity(dataBits, stopBits, parity);
            isOpen = true;
            mFileInputStream = new FileInputStream(super.mFd);
            mFileOutputStream = new FileOutputStream(super.mFd);
            return true;
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean open485(String devNum) {
        isOpen = false;
        mDevNum = devNum;
        // 打开串口
        mFd = open485Dev(mDevNum);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            return false;
        } else {
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
            isOpen = true;
            return true;
        }
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    public int setSpeed(int speed) {
        if (mFd == null) {
            return -1;
        }
        return super.setSpeed(mFd, speed);
    }

    public int setParity(int databits, int stopbits,
                         int parity) {
        if (mFd == null) {
            return -1;
        }
        return super.setParity(mFd, databits, stopbits, parity);
    }

    public int close() {
        if (mFd == null) {
            return -1;
        }
        int retStatus;
        retStatus = super.closeDev(mFd);
        super.mFd = null;
        return retStatus;
    }

    public int close485() {
        if (mFd == null) {
            return -1;
        }
        int retStatus;
        retStatus = super.close485Dev(mFd);
        super.mFd = null;
        return retStatus;
    }

    public int read(byte[] buffer, int length) {
        if (mFd == null) {
            return -1;
        }
        return super.readBytes(mFd, buffer, length);
    }

    public boolean write(byte[] buffer, int length) {
        if (mFd == null) {
            return false;
        }
        return super.writeBytes(mFd, buffer, length);
    }

    public int readBytes(byte[] buffer) {
        if (mFd == null) {
            return -1;
        }
        return super.readBytes(mFd, buffer, buffer.length);
    }

    public boolean writeBytes(byte[] buffer) {
        if (mFd == null) {
            return false;
        }
        return super.writeBytes(mFd, buffer, buffer.length);
    }

    public int read485Bytes(byte[] buffer) {
        if (mFd == null) {
            return -1;
        }
        return super.readBytes(mFd, buffer, buffer.length);
    }

    public boolean write485Bytes(byte[] buffer) {
        if (mFd == null) {
            return false;
        }
        boolean ret;
        super.set485mod(RS485Write);
        ret = super.writeBytes(mFd, buffer, buffer.length);
        super.set485mod(RS485Read);
        return ret;
    }

}
