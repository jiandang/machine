package com.example.myapplication.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class TcnUtility {

	public static boolean sendEmptyMsg(Handler h, int what) {
		if (null == h) {
			return false;
		}
		
		h.removeMessages(what);
		
    	return h.sendEmptyMessage(what);
    }
	
	public static boolean sendMsgDelayed(Handler h, int what, int arg1, long delayMillis, Object obj) {
		if (null == h) {
			return false;
		}
		
		Message msg = h.obtainMessage();
		
		msg.what	= what;
		msg.arg1	= arg1;
		msg.obj		= obj;
		
		return h.sendMessageDelayed(msg, delayMillis);
    }

	public static boolean sendMsgDelayed(Handler h, int what, int arg1, int arg2, long delayMillis, Object obj) {
		if (null == h) {
			return false;
		}

		Message msg = h.obtainMessage();

		msg.what	= what;
		msg.arg1	= arg1;
		msg.arg2	= arg2;
		msg.obj		= obj;

		return h.sendMessageDelayed(msg, delayMillis);
	}
    
    public static boolean sendMsg(Handler h, int what, int arg1, int arg2, Object obj) {
    	if (null == h) {
    		return false;
    	}
    	
    	//h.removeMessages(what);
    	
		Message msg = h.obtainMessage();
		
		msg.what	= what;
		msg.arg1	= arg1;
		msg.arg2	= arg2;
		msg.obj		= obj;
		
		return h.sendMessage(msg);
	}
    
    public static boolean sendMsgAtFrontOfQueue(Handler h, int what, int arg1, int arg2, Object obj) {
    	if (null == h) {
    		return false;
    	}
    	
    	h.removeMessages(what);
    	
		Message msg = h.obtainMessage();
		
		msg.what	= what;
		msg.arg1	= arg1;
		msg.arg2	= arg2;
		msg.obj		= obj;
		
		return h.sendMessageAtFrontOfQueue(msg);
	}

	public static void removeMessages(Handler h, int what) {
    	if (h != null) {
			h.removeMessages(what);
		}
    }

	/**
	 * 自定义toast的样式
	 * @param context
	 * @param text
	 */
	public static void getToast(Context context,String text){
		Toast toast=new Toast(context);
		View view= LayoutInflater.from(context).inflate(R.layout.toast, null);
		TextView msg=(TextView) view.findViewById(R.id.msg);
		msg.setText(text);
		view.setBackgroundResource(R.drawable.toaststyle);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(500);
		toast.show();
	}


	/**
	 * 根据格式获取时间 "yyyyMMddHHmmss"
	 * @return
	 */
	public static String getTime(String dataFormat) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(dataFormat);
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}

	public static String getTime14B() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}

	public static String getTime6B() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("HHmmss");
		return dateFormat.format(new Date(System.currentTimeMillis()));
	}

	//写一个方法实现：把一个十进制的数转换成为16进制的数
	public static String deciToHexData(int a) {
		String str = "";
		//1:用a去除以16，得到商和余数
		int sun = a / 16;
		int yuShu = a % 16;
		str = "" + shuZhiToZhiMu(yuShu);
		while(sun > 0 ) {
			//2：继续用商去除以16，得到商和余数
			yuShu = sun % 16;
			sun = sun / 16;
			//3：如果商为0，那么就终止
			//4：把所有的余数倒序排列
			str = shuZhiToZhiMu(yuShu) + str;
		}
		return str;
	}

	private static String shuZhiToZhiMu(int a){
		switch(a){
			case 10 :
				return "A";
			case 11 :
				return "B";
			case 12 :
				return "C";
			case 13 :
				return "D";
			case 14 :
				return "E";
			case 15 :
				return "F";
		}
		return ""+a;
	}

	/**
	 * 判断是否是含小数
	 * @param str
	 * @return
	 */
	public static boolean isContainDeciPoint(String str){
		if ((null == str) || (str.length() < 1)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否全部由数字组成
	 * @param str
	 * @return
	 */
	public static boolean isDigital(String str){
		if ((null == str) || (str.length() < 1)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[0-9]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 检测网络是否连接
	 *
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected() && ni.isAvailable()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 * @param fileName
	 * @return byte[]
	 */
	public static  byte[] readFile(String fileName)
	{
		FileInputStream fis=null;
		ByteArrayOutputStream baos=null;
		byte[] data=null;
		try {
			fis=new FileInputStream(fileName);
			byte[] buffer=new byte[8*1024];
			int readSize=-1;
			baos=new ByteArrayOutputStream();
			while((readSize=fis.read(buffer))!=-1)
			{
				baos.write(buffer, 0, readSize);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally
		{
			try {
				if (fis!=null)
				{
					fis.close();
				}
				if (baos!=null)
					baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;

	}

	/**
	 * 十六进制转换字符串
	 * @param (:[616C6B])
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

	public static String getAmountFen(String strAmount) {
		if (null == strAmount) {
			return null;
		}
		int iPos = strAmount.indexOf(".");
		int iTmpLength = strAmount.length();
		if (iPos != -1) {
			if ((iTmpLength - iPos) == 2) {
				strAmount = strAmount + "0";
			}
		} else {
			strAmount = strAmount + "00";
		}

		String amount = strAmount.replace(".", "");

		return amount;
	}

	/**
	 *@desc 用于金额格式转换
	 *@author Jiancheng,Song
	 *@time 2016/6/6 8:36
	 */
	public static String getAmount6B(String strAmount) {
		if (null == strAmount) {
			return null;
		}
		int iPos = strAmount.indexOf(".");
		int iTmpLength = strAmount.length();
		if (iPos != -1) {
			if ((iTmpLength - iPos) == 2) {
				strAmount = strAmount + "0";
			}
		} else {
			strAmount = strAmount + "00";
		}

		String amount6B = strAmount.replace(".", "");
		int iLength = amount6B.length();
		if (iLength > 6 || iLength < 1) {
			return null;
		}

		for (int i = 0; i < (6 - iLength); i++) {
			amount6B = "0" + amount6B;
		}
		return amount6B;
	}

	/**
	 *@desc 用于金额格式转换
	 *@author Jiancheng,Song
	 *@time 2016/6/6 8:36
	 */
	public static String getAmount12B(String strAmount) {
		if (null == strAmount) {
			return null;
		}
		int iPos = strAmount.indexOf(".");
		int iTmpLength = strAmount.length();
		if (iPos != -1) {
			if ((iTmpLength - iPos) == 2) {
				strAmount = strAmount + "0";
			}
		} else {
			strAmount = strAmount + "00";
		}

		String amount12B = strAmount.replace(".", "");
		int iLength = amount12B.length();
		if (iLength > 12 || iLength < 1) {
			return null;
		}

		for (int i = 0; i < (12 - iLength); i++) {
			amount12B = "0" + amount12B;
		}
		return amount12B;
	}

	/**
	 *@desc 用于金额格式转换
	 *@author Jiancheng,Song
	 *@time 2016/6/6 8:36
	 */
	public static String getAmount10B(String strAmount) {
		if (null == strAmount) {
			return null;
		}
		int iPos = strAmount.indexOf(".");
		int iTmpLength = strAmount.length();
		if (iPos != -1) {
			if ((iTmpLength - iPos) == 2) {
				strAmount = strAmount + "0";
			}
		} else {
			strAmount = strAmount + "00";
		}

		String amount10B = strAmount.replace(".", "");
		int iLength = amount10B.length();
		if (iLength > 10 || iLength < 1) {
			return null;
		}

		for (int i = 0; i < (10 - iLength); i++) {
			amount10B = "0" + amount10B;
		}
		return amount10B;
	}

	public static String getRandomNumber(int length, int start, int end) {
		int iRandomInt = (int)(Math.random() *(end-start+1))+start;
		String strRandom = String.valueOf(iRandomInt);
		int randomLength = strRandom.length();
		if (randomLength > length) {
			strRandom = strRandom.substring(randomLength - length);
		} else {
			for (int i = 0; i < (length - randomLength); i++) {
				strRandom = "0" + strRandom;
			}
		}
		return strRandom;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 *
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray, int byteCount) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < byteCount; i++) {
			stmp = Integer.toHexString(bArray[i] & 0xFF);
			sb.append((stmp.length()==1)? "0"+stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}

	/* Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
            * @param src byte[] data
    * @return hex string
    */
	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String bytesToHexString(byte src){
		StringBuilder stringBuilder = new StringBuilder("");
		int v = src & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.startsWith("0x") || hv.startsWith("0X")) {
			hv = hv.substring(2);
		}
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);
		return stringBuilder.toString();
	}


	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
			hexString = hexString.substring(2);
		}
		if (hexString.length() == 1) {
			hexString = "0"+hexString;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte hexStringToByte(String hexString) {
		byte bRet = (byte) 0xFF;
		if (hexString == null || hexString.equals("")) {
			return bRet;
		}
		if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
			hexString = hexString.substring(2);
		}
		if (hexString.length() == 1) {
			hexString = "0"+hexString;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		if (d.length > 0) {
			bRet = d[0];
		}

		return bRet;
	}
	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	//time   HH:mm
	public static String getHours(String time) {
		String strHour = null;
		if((null != time) && (time.contains(":"))) {
			String[] strarr = time.split(":");
			if ((null != strarr)) {
				if (strarr.length > 1) {
					strHour = strarr[0];
				}
			}
		}
		return strHour;

	}

	//time   HH:mm
	public static String getMinutes(String time) {
		String strMinute = null;
		if((null != time) && (time.contains(":"))) {
			String[] strarr = time.split(":");
			if ((null != strarr)) {
				if (strarr.length > 1) {
					strMinute = strarr[1];
				}
			}
		}
		return strMinute;
	}

	public static String getChecksumHex(String hexdata) {
		if (hexdata == null || hexdata.equals("")) {
			return "";
		}
		if (hexdata.startsWith("0x") || hexdata.startsWith("0X")) {
			hexdata = hexdata.substring(2);
		}
		if (hexdata.length() == 1) {
			hexdata = "0"+hexdata;
		}

		int total = 0;
		int len = hexdata.length();
		int num = 0;
		while (num < len) {
			String s = hexdata.substring(num, num + 2);
			System.out.println(s);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}
		/**
		 * 用256求余最大是255，即16进制的FF
		 */
		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		if (hex.startsWith("0x") || hex.startsWith("0X")) {
			hex = hex.substring(2);
		}
		len = hex.length();
		// 如果不够校验位的长度，补0,这里用的是两位校验
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex.toUpperCase();
	}


	public static String getCheckXOR(String hexdata) {
		String str1 = "00";
		String str2 = "00";
		int iLength = hexdata.length();
		BigInteger big2= new BigInteger(str2, 16);
		for (int i = 0; i < iLength / 2; i++) {
			str1 = hexdata.substring(i*2, (i+1)*2);
			BigInteger big1= new BigInteger(str1, 16);
			big2 = big1.xor(big2);
		}
		String ret = big2.toString(16);
		if (ret.length() == 1) {
			ret = "0" + ret;
		}
		return ret;
	}

	public static String getCheckXor(String strData){
		byte[] datas = strData.getBytes();
		byte temp=datas[0];

		for (int i = 1; i <datas.length; i++) {
			temp ^=datas[i];
		}
		String iRet = String.valueOf((int)temp);
		return iRet;
	}

	public static String getLengthData(int lentth, String data) {
		if (null == data) {
			return "";
		}
		String strDataLength = String.valueOf(data.length());
		if (strDataLength != null) {
			int iTemp = strDataLength.length();
			if (iTemp < lentth) {
				for (int j = 0; j < (lentth - iTemp); j++) {
					strDataLength = "0"+strDataLength;
				}
			}
		}

		return strDataLength;
	}

	public static long hexStringToDecimal(String hexData) {
		if(hexData==null||hexData.length()<1) {
			throw new RuntimeException("字符串不合法");
		}
		long sum=0;
		int iLength = hexData.length();
		for (int i = 0; i < iLength; i++) {
			long iData = 1 ;
			String tmp = hexData.substring(i,i+1);
			if ("A".equalsIgnoreCase(tmp)) {
				iData = 10;
			} else if ("B".equalsIgnoreCase(tmp)) {
				iData = 11;
			} else if ("C".equalsIgnoreCase(tmp)) {
				iData = 12;
			} else if ("D".equalsIgnoreCase(tmp)) {
				iData = 13;
			} else if ("E".equalsIgnoreCase(tmp)) {
				iData = 14;
			} else if ("F".equalsIgnoreCase(tmp)) {
				iData = 15;
			} else if ("0".equals(tmp)) {
				iData = 0;
			} else if ("1".equals(tmp)) {
				iData = 1;
			} else if ("2".equals(tmp)) {
				iData = 2;
			} else if ("3".equals(tmp)) {
				iData = 3;
			} else if ("4".equals(tmp)) {
				iData = 4;
			} else if ("5".equals(tmp)) {
				iData = 5;
			} else if ("6".equals(tmp)) {
				iData = 6;
			} else if ("7".equals(tmp)) {
				iData = 7;
			} else if ("8".equals(tmp)) {
				iData = 8;
			} else if ("9".equals(tmp)) {
				iData = 9;
			} else {

			}
			for (int j = 0; j < (iLength - i - 1); j++) {
				iData = iData * 16;
			}
			sum = sum + iData;
		}
		return sum;
	}

	public static BigInteger hexStringToBigInteger(String hexData) {
		if(hexData==null||hexData.length()<1) {
			throw new RuntimeException("字符串不合法");
		}
		BigInteger sum=BigInteger.valueOf(0);
		int iLength = hexData.length();
		for (int i = 0; i < iLength; i++) {
			BigInteger iData = BigInteger.valueOf(1);
			String tmp = hexData.substring(i,i+1);
			if ("A".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(10);
			} else if ("B".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(11);
			} else if ("C".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(12);
			} else if ("D".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(13);
			} else if ("E".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(14);
			} else if ("F".equalsIgnoreCase(tmp)) {
				iData = BigInteger.valueOf(15);
			} else if ("0".equals(tmp)) {
				iData = BigInteger.valueOf(0);
			} else if ("1".equals(tmp)) {
				iData = BigInteger.valueOf(1);
			} else if ("2".equals(tmp)) {
				iData = BigInteger.valueOf(2);
			} else if ("3".equals(tmp)) {
				iData = BigInteger.valueOf(3);
			} else if ("4".equals(tmp)) {
				iData = BigInteger.valueOf(4);
			} else if ("5".equals(tmp)) {
				iData = BigInteger.valueOf(5);
			} else if ("6".equals(tmp)) {
				iData = BigInteger.valueOf(6);
			} else if ("7".equals(tmp)) {
				iData = BigInteger.valueOf(7);
			} else if ("8".equals(tmp)) {
				iData = BigInteger.valueOf(8);
			} else if ("9".equals(tmp)) {
				iData = BigInteger.valueOf(9);
			} else {

			}
			for (int j = 0; j < (iLength - i - 1); j++) {
				iData = iData.multiply(BigInteger.valueOf(16)) ;
			}
			sum = sum.add(iData);
		}
		return sum;
	}

	/**
	 * 字符串转换为16进制字符串
	 *
	 * @param s
	 * @return
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			if (s4.length() == 1) {
				s4 = "0"+s4;
			}
			str = str + s4;
		}
		return str;
	}

	/**
	 * 16进制字符串转换为字符串
	 *
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s, String charsetName) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, charsetName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encodeToHexString(String data) {
		if (null == data) {
			return null;
		}
		String HEX_STRING_TABLE = "0123456789ABCDEF";
		//根据默认编码获取字节数组
		byte[] bytes = data.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length*2);
		//将字节数组中每个字节拆解成2位16进制整数
		for(int i = 0; i < bytes.length; i++) {
			sb.append(HEX_STRING_TABLE.charAt((bytes[i]&0xf0)>>4));
			sb.append(HEX_STRING_TABLE.charAt((bytes[i]&0x0f)>>0));
		}
		return sb.toString();
	}

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encodeToHexStringUpperCase(String data) {
		if (null == data) {
			return null;
		}
		String HEX_STRING_TABLE = "0123456789ABCDEF";
		//根据默认编码获取字节数组
		byte[] bytes = data.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length*2);
		//将字节数组中每个字节拆解成2位16进制整数
		for(int i = 0; i < bytes.length; i++) {
			sb.append(HEX_STRING_TABLE.charAt((bytes[i]&0xf0)>>4));
			sb.append(HEX_STRING_TABLE.charAt((bytes[i]&0x0f)>>0));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String hexStringDecode(String hexString)
	{
		if ((null == hexString) || (hexString.length() < 1)) {
			return null;
		}
		String HEX_STRING_TABLE = "0123456789ABCDEF";
		ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length()/2);
		//将每2位16进制整数组装成一个字节
		for(int i = 0; i < hexString.length(); i+=2) {
			baos.write((HEX_STRING_TABLE.indexOf(hexString.charAt(i))<<4 |HEX_STRING_TABLE.indexOf(hexString.charAt(i+1))));
		}
		return new String(baos.toByteArray());
	}
}
