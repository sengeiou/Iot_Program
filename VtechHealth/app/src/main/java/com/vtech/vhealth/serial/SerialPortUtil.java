package com.vtech.vhealth.serial;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

/**
 * Created by changyan on 17-10-2.
 */

public class SerialPortUtil {
    private static final String TAG = "SerialPort";

    private int baudrate = 57600;
    private String path;
    private static Context context;
    private static SerialPortUtil instance;
    private SerialPort mSerialPort = null;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;
    private ReadThread mReadThread;

    public static final String RESPIRE_DEV_PATH = "/dev/ttyS3";//"/dev/ttyMT1";

    /**
     * 获取实例，非线程安全
     *
     * @return
     */
    public static SerialPortUtil getInstance(Context context) {
        if (instance == null || SerialPortUtil.context != context) {
            instance = new SerialPortUtil(context, RESPIRE_DEV_PATH);
        }
        return instance;
    }

    public SerialPortUtil(String path) {
        this(null, path);

    }

    public SerialPortUtil(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public interface OnDataReceiveListener {

        void onDataReceive(short[] buffer);

        void onDataType(int type);
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }


    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            if (mOutputStream != null) {
                mOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate EXCEP " + e);
        }
    }

    public void setStart() {
        isStop = false;
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    public void setStop() {
        if (!isStop) {
            isStop = true;
            if (mReadThread != null)
                mReadThread.interrupt();
            try {
                if (mOutputStream != null) {
                    mOutputStream.flush();
                    mOutputStream.close();
                    mOutputStream=null;
                }
                if (mInputStream != null){
                    mInputStream.close();
                    mInputStream=null;
                }
                if (mSerialPort != null){
                    mSerialPort.devOff();
                    mSerialPort =null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setClose() {
        setStop();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 发送指令到串口        mSerial.onCreate();
     * mSerial.setOnDataReceiveListener(serialReceiveListener);
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd + "\r\n").getBytes();
        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        String tail = "\r\n";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        //注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                result = false;
                Log.w(TAG, "sendBuffer fail mOutputStream is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "sendBuffer fail " + e);
            result = false;
        }
        Log.w(TAG, "sendBuffer result is " + result);
        return result;
    }

    int index = 0;
    String complete_data = "";
    int sub_size = 0;

    byte[] bytes_long = new byte[]{(byte) (170 & 0xff), (byte) (170 & 0xff), 0x12};
    byte[] bytes_short = new byte[]{(byte) (170 & 0xff), (byte) (170 & 0xff), 0x04};

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.w(TAG, "ReadThread isStop " + isStop + " mInputStream is " + mInputStream);

            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null)
                        return;
                    byte[] buffer = new byte[128];
                    Log.w(TAG, "ReadThread bf 1 mInputStream.read");
                    size = mInputStream.read(buffer);

                    Log.w(TAG, "ReadThread read size is  " + size);

                    if (size > 0) {
/*                        String hs = "";
                        String stmp = "";
                        for (int n = 0; n < size; n++) {
                            stmp = Integer.toHexString(buffer[n] & 0xff);
                            if (stmp.length() == 1) {
                                hs = hs + "0" + stmp;
                            } else {
                                hs = hs + stmp;
                            }
                        }

                        Log.w(TAG, "statistics length is:" + size + (hs.indexOf("aaaa12") == -1 ? "" : " ,long_data_index == " + hs.indexOf("aaaa12")) + ",data is: " + hs);

                        if (hs.indexOf("aaaa12") != -1) {
                            int long_data_index = hs.indexOf("aaaa12");
                            sub_size += long_data_index;
                            complete_data += (hs.substring(0, long_data_index));

                            Log.i(TAG, "statistics sub size : " + sub_size + "  ,complete data is: " + complete_data.toUpperCase());
                            index = 0;
                            sub_size = 0;
                            complete_data = hs.substring(hs.indexOf("aaaa12"));
                        }
                        index++;
                        sub_size += size;
                        complete_data += hs;*/

//                        new Thread(new ParsingRunnable(buffer, size)) {}.start();
//                        new ParsingThread(buffer, size).start();


                        for (int i = 0; i < size; ) {
                            if (i + 7 > size) {
                                i++;
                                continue;
                            }
                            if (buffer[i] == bytes_short[2]
                                    && buffer[i + 6] == bytes_short[0]
                                    && buffer[i + 7] == bytes_short[1]) {
                                byte[] b = new byte[]{buffer[i + 3], buffer[i + 4]};
                                short t_s = bytesToShort(b);

                                if (null != onDataReceiveListener) {
//                                    Log.i(TAG, "static for  second ,short data == " + String.valueOf(t_s));
                                    onDataReceiveListener.onDataReceive(new short[]{t_s});
                                }
                                index++;
                                i += 6;
                            } else if (buffer[i] == bytes_short[0]
                                    && buffer[i + 1] == bytes_short[1]
                                    && buffer[i + 2] == bytes_short[2]) {
                                byte[] b = new byte[]{buffer[i + 5], buffer[i + 6]};
                                short t_s = bytesToShort(b);

                                if (null != onDataReceiveListener) {
//                                    Log.i(TAG, "static for first,short data == " + String.valueOf(t_s));
                                    onDataReceiveListener.onDataReceive(new short[]{t_s});
                                }
                                index++;
                                i += 8;
                            } else if (buffer[i] == bytes_long[0] && buffer[i + 1] == bytes_long[0] && buffer[i + 2] == bytes_long[2]) {

                                short pqValue[] = {(short) (buffer[i + 4] & 0xff)};
                                Log.i(TAG, "statistics pqvalue : " + pqValue[0] + " , index : " + index);
                                index = 0;

                                if (null != onDataReceiveListener) {
                                    onDataReceiveListener.onDataType(pqValue[0]);
                                }

                                if (i + 22 < size && buffer[i + 22] == bytes_long[0]) {
                                    i += 22;
                                } else {
                                    i += 3;
                                }
                            } else {
                                i++;
                            }
                        }
                    }

                    Thread.sleep(2);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static short bytesToShort(byte[] bs) {
        short a = (short) ((bs[0] << 8) | bs[1]);
        return a;
    }

    public SerialPort openSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
//            String path = "/dev/ttyGS0";
//            int baudrate = 960 0;aaaa048002ffb2ccaaaa048002ff5a24aaaa048002fef788aaaa048002fecfb0aaaa048002feff80aaaa048002ff621caaaa048002ffb6c8aaaa048002ffe599aaaa048002000677aaaa048002002d50aaaa04800200522baaaa04800200631aaaaa048002006a13aaaa0480020085f8aaaa04800200b1ccaaaa04800200c0bdaaaa0480020089f4aaaa048002002657aaaa048002ffef8faaaa04800200116caaaa04800200522baaaa048002004a33aaaa048002ffe19daaaa048002ff7509aaaa048002ff720caaaa048002ffd9a5aaaa04800200433aaaaa048002006f0eaaaa0480020080fdaaaa04800200add0aaaa04800200e09daaaa04800200ccb1aaaa04800200710caaaa048002002c51aaaa048002004439aaaa0480020083faaaaa048002007904aaaa048002000f6eaaaa1202000366840500f900034408338503ffffe525aaaa048002ffa5d9aaaa048002ff99e5aaaa048002ffdea0aaaa048002002459aaaa048002004934aaaa048002006914aaaa048002008eefaaaa0480020093eaaaaa048002006a13aaaa048002004538aaaa048002006518aaaa04800200b6c7aaaa04800200dda0aaaa04800200a3daaaaa048002003f3eaaaa048002001766aaaa04800200502daaaa04800200b1ccaaaa04800200e39aaaaa04800200cbb2aaaa048002008df0aaaa048002006419aaaa048002006c11aaaa0480020087f6aaaa048002007effaaaa048002003a43aaaa048002ffeb93aaaa048002ffd8a6aaaa04800200116caaaa048002004538aaaa04800200235aaaaa048002ffbac4aaaa048002ff710daaaa048002ff8cf2aaaa048002ffe39baaaa048002001667aaaa048002fff985aaaa048002ffbec0aaaa048002ffa3dbaaaa048002ffb4caaaaa048002ffd2acaaaa048002ffe896aaaa048002fff589aaaa048002fff08eaaaa048002ffcab4aaaa048002ff97e7aaaa048002ff90eeaaaa048002ffd7a7aaaa04800200423baaaa0480020080fdaaaa048002007b02aaaa04800200720baaaa04800200abd2aaaa048002010e6eaaaa048002014636aaaa048002012a52aaaa04800200e598aaaa04800200c1bcaaaa04800200cab3aaaa04800200c6b7aaaa048002008eefaaaa048002003746aaaa048002fff28caaaa048002ffd5a9aaaa048002ffd5a9aaaa048002ffe09eaaaa048002fff38baaaa048002000875aaaa04800200106daaaa048002000d70aaaa048002001469aaaa048002003449aaaa048002005726aaaa04800200522baaaa048002001a63aaaa048002ffd9a5aaaa048002ffc0beaaaa048002ffccb2aaaa048002ffcfafaaaa048002ffabd3aaaa048002ff7e00aaaa048002ff6f0faaaa048002ff80feaaaa048002ff86f8aaaa048002ff6915aaaa048002ff4737aaaa048002ff4539aaaa048002ff621caaaa048002ff7707aaaa048002ff6b13aaaa048002ff641aaaaa048002ff90eeaaaa048002ffed91aaaa048002003746aaaa048002003746aaaa048002fffd81aaaa048002ffdda1aaaa048002000b72aaaa048002006419aaaa0480020096e7aaaa048002007a03aaaa04800200413caaaa048002003d40aaaa0480020083faaaaa04800200d1acaaaa04800200d6a7aaaa0480020088f5aaaa04800200324baaaa048002001b62aaaa04800200423baaaa04800200720baaaa048002008af3aaaa04800200a1dcaaaa04800200d2abaaaa048002010d6faaaa048002012e4eaaaa048002013448aaaa048002013f3daaaa048002015d1faaaa048002016a12aaaa04800201413baaaa04800200fa83aaaa04800200d3aaaaaa04800200e499aaaa04800200ff7eaaaa04800200e994aaaa04800200b1ccaaaa04800200a0ddaaaa04800200d9a4aaaa048002012b51aaaa048002014d2faaaa048002013844aaaa048002012a52aaaa048002014735aaaa04800201621aaaaa04800201314baaaa04800200bcc1aaaa048002005e1faaaa04800200611caaaa04800200a3daaaaa04800200b6c7aaaa048002006815aaaa04800200037aaaaa048002ffef8faaaa04800200334aaaaa048002007706aaaa04800200700daaaa048002003548aaaa048002001667aaaa048002003a43aaaa048002007effaaaa04800200afceaaaa04800200c1bcaaaa04800200cab3aaaa04800200d6a7aaaa04800200dda0aaaa04800200dca1aaaa04800200dba2aaaa04800200e09daaaa04800200e895aaaa04800200f786aaaa048002012656aaaa048002017705aaaa04800201b3c9aaaa048002019edeaaaa048002014537aaaa04800200fe7faaaa048002010f6daaaa048002014f2daaaa048002014834aaaa04800200c9b4aaaa04800200205daaaa048002ffceb0aaaa048002fff589aaaa04800200423baaaa048002005528aaaa048002001d60aaaa048002ffdca2aaaa048002ffc6b8aaaa048002ffceb0aaaa048002ffcdb1aaaa048002ffb7c7aaaa048002ff9ae4aaaa048002ff84faaaaa048002ff6e10aaaa048002ff4b33aaaa048002ff1f5faaaa048002fef788aaaa048002fed4abaaaa048002feb0cfaaaa048002fe92edaaaa048002fe8df2aaaa048002fea3dcaaaa048002febdc2aaaa048002febbc4aaaa048002fea2ddaaaa048002fe98e7aaaa048002febfc0aaaa048002ff0579aaaa048002ff334baaaa048002ff2955aaaa048002ff047aaaaa048002fef986aaaa048002ff1b63aaaa048002ff4e30aaaa048002ff6d11aaaa048002ff81fdaaaa048002ffa1ddaaaa048002ffd4aaaaaa048002ff
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        mInputStream = mSerialPort.getInputStream();
        mOutputStream = mSerialPort.getOutputStream();
        return mSerialPort;
    }

    public void write2port(String s) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int j = 170;
        System.out.println("Number = " + j);

     /* returns the string representation of the unsigned integer value
     represented by the argument in hexadecimal (base 16) */
        System.out.println("Hex = " + Integer.toHexString(j));


        String s = "AAAA120200036E840500F900034408388503FFFFE518AAAA048002003F3EAAAA04800200700DAAAA04800200AFCEAAAA04800200C0BDAAAA048002009FDEAAAA048002007FFEAAAA048002008CF1AAAA04800200BBC2AAAA04800200CCB1AAAA048002009DE0AAAA048002005726AAAA048002003B42AAAA048002005825AAAA04800200720BAAAA048002005429AAAA048002002855AAAA048002006617AAAA048002013D3FAAAA048002024536AAAA04800202D9A2AAAA04800202CCAFAAAA0480020295E6AAAA04800202C0BBAAAA048002033B3FAAAA048002037802AAAA048002031763AAAA04800202601BAAAA04800201DD9FAAAA04800201B9C3AAAA04800201B3C9AAAA048002018FEDAAAA04800201720AAAAA04800201A6D6AAAA048002022457AAAA048002029BE0AAAA04800202CBB0AAAA04800202C8B3AAAA04800202C6B5AAAA04800202CCAFAAAA04800202BDBEAAAA0480020289F2AAAA048002024239AAAA04800202017AAAAA04800201C6B6AAAA0480020188F4AAAA04800201522AAAAA048002013F3DAAAA048002015B21AAAA0480020191EBAAAA04800201B7C5AAAA04800201B2CAAAAA048002017FFDAAAA048002014A32AAAA048002014E2EAAAA0480020199E3AAAA04800201EF8DAAAA04800201F488AAAA0480020196E6AAAA04800201314BAAAA048002011F5DAAAA048002014D2FAAAA048002014F2DAAAA04800200FD80AAAA04800200AAD3AAAA04800200B1CCAAAA04800200D9A4AAAA0480020089F4AAAA048002FF9CE2AAAA048002FEA2DDAAAA048002FE3946AAAA048002FE3E41AAAA048002FE037CAAAA048002FD522EAAAA048002FCD8A9AAAA048002FD532DAAAA048002FE95EAAAAA048002FFABD3AAAA048002000A73AAAA048002002A53AAAA04800200C0BDAAAA04800201C1BBAAAA048002026A11AAAA048002024F2CAAAA04800201E597AAAA04800201DAA2AAAA048002022358AAAA04800202116AAAAA048002014537AAAA048002003D40AAAA048002FFAFCFAAAA048002FFB0CEAAAA048002FFAFCFAAAA048002FF3A44AAAA048002FE7A05AAAA048002FDF58BAAAA048002FDE69AAAAA048002FE0E71AAAA048002FE1F60AAAA048002FE0B74AAAA048002FDF58BAAAA048002FE017EAAAA048002FE2F50AAAA048002FE700FAAAA048002FEACD3AAAA048002FECFB0AAAA048002FED3ACAAAA048002FEBEC1AAAA048002FEA5DAAAAA048002FE91EEAAAA048002FE89F6AAAA048002FE93ECAAAA048002FEAFD0AAAA048002FED2ADAAAA048002FEE59AAAAA048002FEE09FAAAA048002FED2ADAAAA048002FED5AAAAAA048002FEEF90AAAA048002FF1668AAAA048002FF3C42AAAA048002FF5F1FAAAA048002FF7B03AAAA048002FF84FAAAAA048002FF7608AAAA048002FF6519AAAA048002FF710DAAAA048002FF9EE0AAAA048002FFC8B6AAAA048002FFBFBFAAAA048002FF80FEAAAA048002FF3F3FAAAA048002FF3549AAAA048002FF621CAAAA048002FF8DF1AAAA048002FF86F8AAAA048002FF522CAAAA048002FF215DAAAA048002FF146AAAAA048002FF2B53AAAA048002FF4C32AAAA048002FF730BAAAA048002FFA3DBAAAA048002FFC8B6AAAA048002FFD3ABAAAA048002FFCFAFAAAA048002FFD9A5AAAA048002FFF589AAAA04800200007DAAAA048002FFED91AAAA048002FFF38BAAAA04800200522BAAAA04800200F885AAAA048002017903AAAA048002018FEDAAAA0480020180FCAAAA04800201D6A6AAAA04800202B9C2AAAA04800203A4D6AAAA04800203F189AAAA0480020386F4AAAA04800202F08BAAAA04800202B4C7AAAA04800202DAA1AAAA048002030575AAAA04800202EB90AAAA04800202A0DBAAAA048002025E1DAAAA048002024437AAAA048002024B30AAAA048002025C1FAAAA048002026516AAAA048002025A21AAAA048002023F3CAAAA048002022C4FAAAA048002022259AAAA048002020378AAAA04800201AECEAAAA048002013547AAAA04800200DBA2AAAA04800200C6B7AAAA04800200C2BBAAAA048002007805AAAA048002FFED91AAAA048002FF8EF0AAAA048002FFB8C6AAAA048002003F3EAAAA0480020092EBAAAA048002007409AAAA04800200413CAAAA048002007706AAAA048002011468AAAA0480020187F5AAAA048002016418AAAA04800200DAA3AAAA04800200700DAAAA04800200700DAAAA04800200A9D4AAAA04800200C3BAAAAA04800200ACD1AAAA0480020089F4AAAA048002007607AAAA048002006815AAAA048002004736AAAA048002002B52AAAA04800200413CAAAA0480020091ECAAAA04800200E19CAAAA04800200DDA0AAAA048002007706AAAA048002000479AAAA048002FFF589AAAA048002005D20AAAA04800200CCB1AAAA04800200C1BCAAAA048002003A43AAAA048002FFBAC4AAAA048002FFBBC3AAAA048002002954AAAA0480020082FBAAAA048002007904AAAA048002004934AAAA04800200512CAAAA0480020095E8AAAA04800200BEBFAAAA0480020091ECAAAA048002003F3EAAAA048002002855AAAA048002005825AAAA048002008FEEAAAA048002008CF1AAAA048002004B32AAAA048002000479AAAA048002FFE39BAAAA048002FFF589AAAA04800200304DAAAA048002007904AAAA04800200A0DDAAAA048002007C01AAAA048002002459AAAA048002FFEB93AAAA048002000776AAAA048002004736AAAA048002004736AAAA048002FFDDA1AAAA048002FF5826AAAA048002FF314DAAAA048002FF7B03AAAA048002FFD";

/*        byte[] b = new byte[4062];
        System.out.println("length = " + s.length() + " , data = " + s);
        System.out.println();
        String data = s.substring(s.indexOf("AAAA0480"));

        if (s.indexOf("aaaaa0480") != -1) {
            System.out.println("index  = " + s.indexOf("AAAA0480") + " subdata is :  " + data);
        }

        for (i = 0; i < s.length(); ) {
            if (i + 16 > s.length()) break;

            String temp = data.substring(i, i += 16);

            System.out.println("temp: " + temp);
            if (temp.startsWith("AAAA048002")) {

                String hex = "0x" + temp.substring(10, 14);

                System.out.println(Integer.parseInt(hex, 16));
            }
        }*/

        String sss = "aaaa048002ffbbc3aaaa048002ff7b03aaaa048002ff4b33aaaa048002fed9a6aaaa048002fe2d52aaaa048002fda5dbaaaa048002fd7d03aaaa048002fd99e7aaaa048002fdb3cdaaaa048002fdb2ceaaaa048002fda5dbaaaa048002fda9d7aaaa048002fdc6baaaaa048002fdf888aaaa048002fe344baaaa048002fe631c";


        byte[] bytes_short = new byte[3];
        bytes_short[0] = (byte) (170 & 0xff);
        bytes_short[1] = (byte) (170 & 0xff);
        bytes_short[2] = 0x04;

//        byte[] buffer = new byte[]{(byte) (170 & 0xff), (byte) (170 & 0xff), 0x04, (byte) (0x80 & 0xff), 0x02, (byte) (0xef & 0xff), 0x5c, 0x32, (byte) (170 & 0xff)};

        byte[] buffer = new byte[]{0x04, (byte) (0x80 & 0xff), 0x02, (byte) (0xef & 0xff), 0x5c, 0x32, (byte) (170 & 0xff), (byte) (170 & 0xff)};


        int size = buffer.length;
        System.out.println("buffer size " + size);

        for (int i = 0; i < size; ) {
            System.out.println("i == " + i);
            if (i + 8 > size) {
                i++;
                continue;
            }
            if (buffer[i] == bytes_short[2] && buffer[i + 6] == bytes_short[0] && buffer[i + 7] == bytes_short[0]) {
                byte[] b = new byte[]{buffer[i + 3], buffer[i + 4]};
                short t_s = bytesToShort(b);

                System.out.println("static for  second i = " + i + "  ,short data == " + String.valueOf(t_s) + " i + 6 == " + buffer[i + 6]);

                i += 6;
            } else if (buffer[i] == bytes_short[0] && buffer[i + 1] == bytes_short[0] && buffer[i + 2] == bytes_short[2]) {
                byte[] b = new byte[]{buffer[i + 5], buffer[i + 6]};
                short t_s = bytesToShort(b);

                System.out.println("static for first  i = " + i + "  ,short data == " + String.valueOf(t_s));

                System.out.println("static for first i + 8 == " + buffer[i + 8] + " 0x32 == " + 0x32);

                i += 8;
            } else {
                i++;
            }

        }

        byte[] bytes = new byte[]{(byte) (0xef & 0xff), 0x5c};

        System.out.println("short: " + bytesToShort(bytes));


        byte[] bs = new byte[]{0x00, 0x03};


        System.out.println("short: " + bytesToShort(bs));


        byte[] bs2 = new byte[]{0x02, 0x00};


        System.out.println("bs2 int: " + (0x02 & 0xff) + " , short = " + bytesToShort(bs2));


        short p = (short) (0xC8 & 0xff);

        System.out.println("short: " + p);

        System.out.println("short byte: " + 0XC8);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
