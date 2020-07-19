package com.example.myapplication.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ASXY_home on 2018-10-15.
 */

public class Utils {
    //crc 校验 length:数组长度
    public static int CRC16_Check(byte Pushdata[],int length) {
        int Reg_CRC=0xffff;
        int temp;
        int i,j;

        for( i = 0; i<length; i ++) {
            temp = Pushdata[i];
            if(temp < 0) temp += 256;
            temp &= 0xff;
            Reg_CRC^= temp;

            for (j = 0; j<8; j++) {
                if ((Reg_CRC & 0x0001) == 0x0001)
                    Reg_CRC=(Reg_CRC>>1)^0xA001;
                else
                    Reg_CRC >>=1;
            }
        }
        return (Reg_CRC&0xffff);
    }
    //获取随机数 len是指要生成几位，
    public static String randomHexString(int len)  {
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<len;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;

    }
    //中文转换成GBK码(16进制字符串)，每个汉字2个字节
    public static String Chinese2GBK(String chineseStr){
        StringBuffer GBKStr = new StringBuffer();
        byte[] GBKDecode = new byte[0];
        try {
            GBKDecode = chineseStr.getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (byte b : GBKDecode)
            GBKStr.append(Integer.toHexString(b & 0xFF));
        return GBKStr.toString().toUpperCase();
    }
    /**
     *  异或校验
     *
     * @param datalast 十六进制串
     * @return checkData  十六进制串
     *
     * */
    public static String checkXor(String datalast) {
        //dataLast格式：String datalast = "000000 "+s2+data2;不带空格的
        int checkDataLast = 0;
        for (int i = 0; i < datalast.length();i = i+2) {
            //将十六进制字符串转成十进制
            int start = Integer.parseInt(datalast.substring(i,i+2), 16);
            //进行异或运算
            checkDataLast = start ^ checkDataLast;
        }
        return integerToHexString(checkDataLast);
        //dataLast格式：String datalast = "00 00 00 "+s2+" "+data2;带空格的
//        String[] splitLast = datalast.split(" ");
//        int checkDataLast = 0;
//        Log.d("TAG", "onClick: "+splitLast.length);
//        for (int i = 0; i < splitLast.length; i++) {
//            //将十六进制字符串转成十进制
//            int start = Integer.parseInt(splitLast[i], 16);
//            //进行异或运算
//            checkDataLast = start ^ checkDataLast;
//        }
//        Log.d("TAG", "onClick.: "+checkDataLast);
//        return integerToHexString(checkDataLast);
    }

    /**
     * 将十进制整数转为十六进制数，并补位
     */
    public static String integerToHexString(int checkDataLast) {
        String ssLast = Integer.toHexString(checkDataLast);
        if (ssLast.length() % 2 != 0) {
            ssLast = "0" + ssLast;//0F格式
        }
        String sLast = ssLast.toUpperCase();
        return sLast;
    }
    public static String integerToHexStringQuanCun(int checkDataLast) {
        String ssLast = Integer.toHexString(checkDataLast);
        if (ssLast.length() % 2 != 0) {
            ssLast = "0" + ssLast;//0F格式
        }
        if(ssLast.length() == 2){
            ssLast = "00" + ssLast;//0F格式
        }
        String sLast = ssLast.toUpperCase();
        return sLast;
    }
    //指令的异或校验、拼接
    public static String dataXor(String data){
        Log.i("tag",data.length()+"----dataXor---->>"+data);
        int length = data.length();
        //将十进制整数转为十六进制数，并补位
        String s = integerToHexString(length/2);
        String data1 = "becc010300" + s + data;
        String s1 = integerToHexString(data1.length() / 2);
        String data2 = "80000000"+s1+"00003c0000"+data1;
        String s2 = integerToHexString(data2.length()/2);

        String datalast = "000000" + s2 + data2;
        //异或校验  十六进制串
        String checkXor = checkXor(datalast);
        String sXor = "02" + datalast + checkXor + "03";
        return sXor;
    }

    /**
     * 十六进制转换字符串
     * @param //String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex
     *            十六进制字符串
     * @return byte[]
     */
    public static byte[] hexStr2Byte(String hex) {
        if (hex == null) {
            return new byte[] {};
        }
        // 奇数位补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        for (int i = 0; i < length; i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            buffer.put(b);
        }
        return buffer.array();
    }
    /**
     * byte[]转十六进制字符串
     *
     * @param array
     *            byte[]
     * @return 十六进制字符串
     */
    public static String byteArrayToHexString(byte[] array) {
        if (array == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(byteToHex(array[i]));
        }
        return buffer.toString();
    }
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**
     * byte转十六进制字符
     *
     * @param b
     *            byte
     * @return 十六进制字符
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase(Locale.getDefault());
    }

    /**
     * 合并数组
     *
     * @param firstArray
     *            第一个数组
     * @param secondArray
     *            第二个数组
     * @return 合并后的数组
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
        return bytes;
    }
    //cmd:去除了第一位的货道号  firstNum：货道号的第一位数字
    public static byte[] get(int cmd,int firstNum){
        String CMD = null;
        if (cmd<10) {
            CMD ="0"+ Integer.toHexString(cmd);
        }else{
            CMD = Integer.toHexString(cmd);
        }
        String JIAN = Integer.toHexString(255-cmd);
        String sendStr = null;
        if (firstNum == 1) {
            sendStr = "01fe"+CMD+JIAN+"aa55";
        }else if(firstNum == 2){
            sendStr = "00ff"+CMD+JIAN+"aa55";//暂时用00，后再改
        }else if(firstNum == 3){
            sendStr = "03fc"+CMD+JIAN+"aa55";//暂时用00，后再改
        }else if(firstNum == 4){
            sendStr = "04fb"+CMD+JIAN+"aa55";//暂时用00，后再改
        }
        Log.d("TAG", "出货:"+ sendStr);
        if (sendStr != null) {
            byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
            return sendCmd;
        }
        return null;
    }
    /**
     *
     * @param time  1541569323155
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return 2018-11-07 13:42:03
     */
    public static String getDate2String(long time, String pattern) {
        if (time == 0) {
            return "";
        }
        time = time * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        result = format.format(new Date(time));
        return result;
    }
}
