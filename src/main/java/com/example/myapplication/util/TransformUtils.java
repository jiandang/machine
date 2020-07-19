package com.example.myapplication.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * 
 * 各种类型转换函数整理
 * 
 * @author Damin
 */
public class TransformUtils {

	/**
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";
	private int lihaifeng666666;

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 * 
	 * @param str
	 * @return String
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		try {
			bytes = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String decode(String bytes) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));

		return new String(baos.toByteArray(), "GBK");

	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase(Locale.getDefault()));
		}
		return sb.toString();
	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param src
	 * @param length
	 * @return String
	 * 
	 */
	public static String bytesToHexString(byte[] src, int size) {
		String ret = "";
		if (src == null || size <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			String hex = Integer.toHexString(src[i] & 0xFF);
			// String hex = String.format("%02x", src[i] & 0xFF);
			if (hex.length() < 2) {
				hex = "0" + hex;
			}
			hex += "";
			ret += hex;
		}
		return ret.toLowerCase(Locale.getDefault());
	}

	/**
	 * 把字节数组转换成16进制字符串 0x0A格式
	 * 
	 * @param src
	 * @param length
	 * @return String
	 */
	public static String bytesToHexStringWithOx(byte[] src, int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = " 0x" + Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToBytes(String hex) {
		while (hex.length() < 2)
			return null;
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (charToByte(achar[pos]) << 4 | charToByte(achar[pos + 1]));
		}
		return result;
	}

	public static byte charToByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * int to byte
	 * 
	 * @param 0 <= input <= 255
	 * @return
	 */
	public static byte intToByte(int input) {
		byte output1;
		output1 = (byte) (input & 0xff);
		return output1;
	}

	/**
	 * byte to int
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		return b < 0 ? b & 0x7F + 128 : b;
	}

	/**
	 * 数字字符串转ASCII码字符串
	 * 
	 * @param String
	 *            字符串
	 * @return ASCII字符串
	 */
	public static String StringToAsciiString(String content) {
		String result = "";
		int max = content.length();
		for (int i = 0; i < max; i++) {
			char c = content.charAt(i);
			String b = Integer.toHexString(c);
			result = result + b;
		}
		return result;
	}

	/**
	 * 十六进制字符串装十进制
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return 十进制数值
	 */
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * 校验和 Checksum: (Start+Command+Length+Data)&0xFF
	 * 
	 * @param b
	 * @param Len
	 * 
	 * @return byte[]
	 */
	public static byte[] Check_Sum(byte[] Data, int Len) {
		byte[] Sum = new byte[1];

		byte CheckSum = 0;
		for (int i = 0; i < Len; i++)
			CheckSum += Data[i];
		Sum[0] = (byte) (CheckSum & 0xFF);

		return Sum;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
	 * 0xD9}
	 *
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}


	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 *
	 * @param src0
	 *            byte
	 * @param src1
	 *            byte
	 * @return byte
	 */
	private static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
}
