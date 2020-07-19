
package com.dwin.navy.serialportapi;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.example.myapplication.util.Cof;
import com.example.myapplication.util.TransformUtils;
import com.sodo.serialport.SodoSerialPort;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.mindpipe.android.logging.log4j.LogConfigurator;

import static java.lang.Integer.toHexString;

// 这是售货用的串口通讯类-读卡器用
public class com_zhongji
{
	private static final String TAG = "com_track";

	SimpleDateFormat formathms = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

	private SodoSerialPort serialPort;

	private ReadThread mReadThread;

	private byte[] rxByteArray = null;// 接收到的字节信息

	private boolean readThreadisRunning = false;

	private static com_zhongji ins;
	private OutputStream outputStream;
	private InputStream inputStream;

	public static com_zhongji getInstance(String serilno){
		configLog();
		if(ins==null){
			synchronized (com_zhongji.class){
				if(ins==null){
					ins = new com_zhongji(serilno);
				}
			}
		}
		return ins;
	}

	private com_zhongji(String serialno){
		//(1) 串口定义
		serialPort = new SodoSerialPort();
		serialPort.open(serialno,//串口号，dev/tty已经默认添加直接写例如S1，S2之类的串口号
				115200,//波特率
				8, 1, 'n');
		outputStream = serialPort.getOutputStream();
		inputStream = serialPort.getInputStream();
//		serialPort.mDevNum = serailno;//串口序号
//		serialPort.mSpeed = 115200;//波特率
//		serialPort.mDataBits = 8;//数据位
//		serialPort.mStopBits = 1;//停止位
//		serialPort.mParity = 'n';//校验位
		readThreadisRunning = true;
//			serialPort.writeBytes("[IKK]".getBytes());
		mReadThread = new ReadThread();
		mReadThread.setName("售货用的串口线程：" + formathms.format(new Date(System.currentTimeMillis())));
		mReadThread.start();
	}
	public void openSerialPort() {

		// (2) 打开串口
//		if (serialPort.mFd == null) {
//			serialPort.openDev(serialPort.mDevNum);
//
//			serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);
//			serialPort.setParity(serialPort.mFd, serialPort.mDataBits,serialPort.mStopBits, serialPort.mParity);

			readThreadisRunning = true;

//			serialPort.writeBytes("[IKK]".getBytes());
			mReadThread = new ReadThread();
			mReadThread.setName("售货用的串口线程：" + formathms.format(new Date(System.currentTimeMillis())));
			mReadThread.start();

//		}
	}

	public void closeSerialPort() {
		Log.d(TAG, "closeSerialPort: 1111111111111");
		if (mReadThread != null) {
			readThreadisRunning = false;
			SystemClock.sleep(100);    //暂停0.1秒保证mReadThread线程结束
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outputStream = null;
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
		}
		if (serialPort.mFd != null) {
			serialPort.closeDev(serialPort.mFd);
			ins = null;
		}
	}
	private class ReadThread extends Thread {
		byte[] buf = new byte[512];
		byte[] rxByteArrayTemp = null;// 临时变量：接收到的字节信息

		@Override
		public void run() {
			super.run();
			Log.d(TAG, "closeSerialPort: 2222222");
			while (readThreadisRunning) {
				Log.d(TAG, "closeSerialPort: 33333333");
				int size = 0;
				if(buf == null){
					//线程被中止了
					Log.d(TAG, "run: ++++++++++++");
					return;
				} else {
					try {
						if (inputStream!=null) {
							size = inputStream.read(buf);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (size > 0) {
					// 发现有信息后就追加到临时变量
					rxByteArrayTemp = ArrayAppend(rxByteArrayTemp, buf,size);
				} else {
					// 这次发现没有信息，如果以前有信息的，那就是我们要的数据
					if(rxByteArrayTemp != null){
						rxByteArray = ArrayAppend(rxByteArrayTemp, null);
						rxByteArrayTemp = null;
					}
				}
				// 每20个毫秒去读取数据
				SystemClock.sleep(20);
			}
		}
	}
	/**
	 * 将源数组追加到目标数组
	 *
	 * @param
	 * @param
	 * @return:<br>返回一个新的数组，包括了原数组1和原数组2
	 */
	public static byte[] ArrayAppend(byte[] byte_1, byte[] byte_2)
	{
		// java 合并两个byte数组

		if (byte_1 == null && byte_2 == null)
		{
			return null;
		} else if (byte_1 == null)
		{
			byte[] byte_3=new byte[ byte_2.length];
			System.arraycopy(byte_2, 0, byte_3, 0, byte_2.length);
			return byte_3;
			//return byte_2;
		} else if (byte_2 == null)
		{
			byte[] byte_3=new byte[byte_1.length ];
			System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
			return byte_3;
			//return byte_1;
		} else
		{
			byte[] byte_3 = new byte[byte_1.length + byte_2.length];
			System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
			System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
			return byte_3;
		}

	}
	/**
	 * 将源数组追加到目标数组
	 *
	 * @param byte_1 Sou1原数组1
	 * @param byte_2 Sou2原数组2
	 * @return:<br>返回一个新的数组，包括了原数组1和原数组2
	 */
	public static byte[] ArrayAppend(byte[] byte_1, byte[] byte_2,int size)
	{
		// java 合并两个byte数组

		if (byte_1 == null && byte_2 == null)
		{
			return null;
		} else if (byte_1 == null)
		{
			byte[] byte_3=new byte[ size];
			System.arraycopy(byte_2, 0, byte_3, 0, size);
			return byte_3;
			//return byte_2;
		} else if (byte_2 == null)
		{
			byte[] byte_3=new byte[byte_1.length ];
			System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
			return byte_3;
			//return byte_1;
		} else
		{
			byte[] byte_3 = new byte[byte_1.length + size];
			System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
			System.arraycopy(byte_2, 0, byte_3, byte_1.length, size);
			return byte_3;
		}

	}
	/**
	 * 转换字节为十六进制
	 * @param src
	 * @param size
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int size) {
		String ret = "";
		if (src == null || size <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			String hex = toHexString(src[i] & 0xFF);
			if (hex.length() < 2) {
				hex = "0" + hex;
			}
			ret += hex;
		}
		return ret.toUpperCase(Locale.US);
	}
	/**
	 * 切换温控开关
	 * @param switchTmp
	 */
	public boolean updateTmpSwitch(boolean switchTmp) {
		try{
			String CMD = Integer.toHexString(204);
			String JIAN = Integer.toHexString(255-204);

			String sendStr = null;
			if(switchTmp){
				sendStr ="00ff"+CMD+JIAN+"ff00";
			}else{
				sendStr ="00ff"+CMD+JIAN+"00ff";
			}
			Log.i(TAG, "发送温控开关:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<6){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到温控开关数据"+recStr);
				//TODO 根据厂家协议判断 是否成功， 暂无协议，留空
				return  true;


			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return false;
	}
	public boolean updateTmpValue(int tmp){

		try{
			String CMD = Integer.toHexString(221);
			String JIAN = Integer.toHexString(255-221);

			String sendStr =sendStr ="00ff"+CMD+JIAN+intToByteArray(tmp);

			Log.i(TAG, "发送改变温度:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<5){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到改变温度数据"+recStr);
				//TODO 根据厂家协议判断 是否成功， 暂无协议，留空
				return  true;


			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return false;

	}
	public boolean updateLedSwitch(boolean switchLed) {

		try{
			String CMD = Integer.toHexString(221);
			String JIAN = Integer.toHexString(255-221);

			String sendStr = null;
			if(switchLed){
				 sendStr ="00ff"+CMD+JIAN+"aa55";
			}else{
				 sendStr = "00ff"+CMD+JIAN+"55aa";
			}
			Log.i(TAG, "LED开关:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<6){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到LED开关数据"+recStr);
				//TODO 根据厂家协议判断 是否成功， 暂无协议，留空
				return  true;


			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return false;
	}
	public static String intToByteArray(int a) {
		byte[] b =  new byte[] {
				(byte) ((a >> 24) & 0xFF),
				(byte) ((a >> 16) & 0xFF),
				(byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF)
		};
		if(b.length>4){
			return "0000";
		}else{
			return bytesToHexString(b,b.length).substring(4,8);
		}
	}
	public boolean updateHeatGlass(boolean switchHeadGlass) {

		try{
			String CMD = Integer.toHexString(212);
			String JIAN = Integer.toHexString(255-212);
			String sendStr = null;
			if(switchHeadGlass){
				sendStr ="00ff"+CMD+JIAN+"ff00";
			}else{
				sendStr ="00ff"+CMD+JIAN+"00ff";
			}
			Log.i(TAG, "发送玻璃加热开关:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<5){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到玻璃加热开关数据"+recStr);
				//TODO 根据厂家协议判断 是否成功， 暂无协议，留空
				return  true;


			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return false;
	}
	public boolean updateCoolHot(boolean switchCoolHot) {

		try{
			String CMD = Integer.toHexString(205);
			String JIAN = Integer.toHexString(255-205);
			String sendStr = null;
			if(switchCoolHot){
				sendStr ="00ff"+CMD+JIAN+"ff00";
			}else{
				sendStr ="00ff"+CMD+JIAN+"00ff";
			}
			Log.i(TAG, "制冷加热开关:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<5){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到制冷加热开关数据"+recStr);
				//TODO 根据厂家协议判断 是否成功， 暂无协议，留空
				return  true;


			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return false;
	}
	/**
	 * 查下温度
	 * @return
	 */
	public String checkTmp(){
		try{
			String CMD = Integer.toHexString(220);
			String JIAN = Integer.toHexString(255-220);
			String sendStr ="00ff"+CMD+JIAN+"55aa";
			Log.i(TAG, "检查温度:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			serialPort.writeBytes(sendCmd);

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<5){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到检查温度数据"+recStr);
				int tmp = rxByteArray[2]&0xff;
				return  tmp+"";
//				return recStr+"--->"+tmp;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return  null;
	}
	private static Logger gLogger;
	public static void configLog() {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
		// Set the root log level
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();

		//gLogger = Logger.getLogger(this.getClass());
		gLogger = Logger.getLogger("TransportationCardFragment");
	}
	public String checkXunKa(String sendStr){
		if ((null == outputStream) || (null == sendStr)) {
			return null;
		}
		try{
			Log.i(TAG, "寻卡:"+ sendStr);
			gLogger.debug("寻卡:"+ sendStr);
			rxByteArray = null;
			byte[] sendCmd = TransformUtils.HexString2Bytes(sendStr);
			outputStream.write(sendCmd);
			outputStream.flush();

			for(int i = 0 ; i <10; i++){
				Thread.sleep(100);
				if(rxByteArray==null||rxByteArray.length<5){
					continue;
				}
				//接收到shuju
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到寻卡数据"+recStr);
				gLogger.debug("接收到寻卡数据"+recStr);
				int tmp = rxByteArray[2]&0xff;
				return  recStr;
//				return  tmp+"";
//				return recStr+"--->"+tmp;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		rxByteArray = null;
		return  null;
	}

	public static void main(String [] args){
//		byte[] b = new byte[]{0x01,0x02, (byte) 0xdc};
//		System.out.println("main: "+(b[2]&0xff));
//		System.out.println("main: "+b.length);
//		String str = bytesToHexString(b,b.length);
//		System.out.println(str);

	}


	/**
	 * 售货函数
	 *
	 * @param dao
	 * @return 返回货道结构类型
	 */
	public String chu_huo_track(int dao,String  deviceModel)
	{
		System.out.println("计算前："+dao);
		if(Cof.MODEL_02.equals(deviceModel)){
			if(dao<7){
				dao = dao ;
			}else if(dao>=7&&dao<=12){
				dao = dao+4;
			}else if(dao>12&&dao <= 18){
				dao = dao+8;
			}else if (dao>18&&dao <= 22){
				dao = dao+12;
			}else if(dao>22&&dao <29){
				dao = dao+18;
			}
		}

		if(Cof.MODEL_04.equals(deviceModel)){
			if(dao<7){
				dao = dao ;
			}else if(dao>=7&&dao<=12){
				dao = dao+4;
			}else if(dao>12&&dao <= 18){
				dao = dao+8;
			}else if (dao>18&&dao <= 24){
				dao = dao+12;
			}else if(dao>24&&dao <=30){
				dao = dao+16;
			}else if(dao>30&&dao <=36  ){
				dao = dao+20;
			}
		}


		System.out.println("zj计算后"+dao);

		String hui = "出货命令发送4次，对方无应答。";

		String hexDAO = Integer.toHexString(dao);
		hexDAO = hexDAO.length()<2?"0"+hexDAO:hexDAO;
		int jian = Integer.parseInt("FF", 16) - dao;
		String hexJian = Integer.toHexString(jian);

		Log.i(TAG, "chu_huo_track: "+"00ff"+hexDAO+hexJian+"aa55");
		byte[] fa_cmd = TransformUtils.HexString2Bytes("00ff"+hexDAO+hexJian+"aa55");


		long nowTime = 0l;
		long endWhile = 0l;
			if (null != serialPort.mFd) {
				rxByteArray = null;			// 接收到的字节信息
				Log.i(TAG, "com_track发送的数据_出货：" + bytesToHexString(fa_cmd,fa_cmd.length));
				serialPort.writeBytes(fa_cmd);//只发一次

				//开始等串口返回
				nowTime = System.currentTimeMillis();
				endWhile = nowTime + 11000;  // 最多等待11000毫秒=11秒
				while(nowTime < endWhile){
					SystemClock.sleep(200);
					if(rxByteArray == null){
						// 需要再次发送,继续循环
						nowTime = System.currentTimeMillis();
						continue;  //while
					} else {
						break;
					}
				}

				if(rxByteArray == null){
					//等11秒还是没返回数据，提示掉货失败;
					return "未检测到掉货";
				}
				// 哈哈，有数据了
				String recStr = bytesToHexString(rxByteArray,rxByteArray.length);
				Log.i(TAG, "接收到数据：" +recStr);
				if(rxByteArray.length==5){
					if(recStr.substring(6,8).equals("E2")||recStr.substring(6,8).equals("AA")){
						return null;
					}else{
						return "掉货失败";
					}

				} else {
					// 说明收到的数据的长度不对
					hui = "出货命令后收到的数据的长度不对：" + rxByteArray.length;
					return hui;
				}

			}else {
				return "出货指令发送失败";
			}
	}

	private boolean checkSum(byte[] bytes){
		//长度校验
		if(bytes.length==6){
			byte checkValue = 0;
			for(int i = 0 ; i < bytes.length-2;i++){
				checkValue+=bytes[i];
			}

			//检验和
			if((checkValue%100)==bytes[bytes.length-1]){
				return true;
			}else{
				return false;
			}

		}else{
			return false;
		}
	}


	private String getCheckSum(String str){
		if(null==str||str.isEmpty()){
			return "";
		}
		int data = 0 ;
		for(int i = 0 ; i<str.length();i++){
			data+=(int)str.charAt(i);
		}
		return String.valueOf(data);

	}


}
