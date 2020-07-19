
package com.dwin.navy.serialportapi;

import android.os.SystemClock;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 这是售货用的串口通讯类
public class com_track
{
	private static final String TAG = "com_track";
	
	SimpleDateFormat formathms = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
	
	private SerailPortOpt serialPort;

	private ReadThread mReadThread;
	
	private byte[] rxByteArray = null;// 接收到的字节信息
	
	private boolean readThreadisRunning = false;
	
	public com_track(int serailno){
		//(1) 串口定义
		serialPort = new SerailPortOpt();
		serialPort.mDevNum = serailno;   //串口序号
		serialPort.mSpeed = 9600;//波特率
		serialPort.mDataBits = 8;//数据位
		serialPort.mStopBits = 1;//停止位
		serialPort.mParity = 'n';//校验位
	}
	
	public void openSerialPort() {

		// (2) 打开串口
		if (serialPort.mFd == null) {
			serialPort.openDev(serialPort.mDevNum);

			serialPort.setSpeed(serialPort.mFd, serialPort.mSpeed);
			serialPort.setParity(serialPort.mFd, serialPort.mDataBits,serialPort.mStopBits, serialPort.mParity);

			readThreadisRunning = true;
			mReadThread = new ReadThread();
			mReadThread.setName("售货用的串口线程：" + formathms.format(new Date(System.currentTimeMillis())));
			mReadThread.start();

		}
	}
	
	public void closeSerialPort() {

		if (mReadThread != null) {
			readThreadisRunning = false;
			SystemClock.sleep(100);    //暂停0.1秒保证mReadThread线程结束
		}

		if (serialPort.mFd != null) {
			serialPort.closeDev(serialPort.mFd);
		}
	}
	
	private class ReadThread extends Thread {
		byte[] buf = new byte[512];
		byte[] rxByteArrayTemp = null;// 临时变量：接收到的字节信息

		@Override
		public void run() {
			super.run();
			
			while (readThreadisRunning) {

				int size = 0;
				if(buf == null){
					//线程被中止了
					return;
				} else {
					size = serialPort.readBytes(buf);
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
	 * @param Sou1原数组1
	 * @param Sou2原数组2
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
			String hex = Integer.toHexString(src[i] & 0xFF);
			if (hex.length() < 2) {
				hex = "0" + hex;
			}
			ret += hex;
		}
		return ret.toUpperCase(Locale.US);
	}
	

	/**
	 * 售货函数
	 *
	 * @param dao  售货的货道号（从10开始的，就是我们看到的货道编号）
	 * @return 返回货道结构类型
	 */
	public String chu_huo_track(int dao)   // dao 是从10开始的
	{

		String lie="";//个
		String han="";//十
		System.out.println("计算前："+dao);
		dao = dao -1;
		if(dao<5){
			lie=dao+"";
			han = "1" ;
		}else if(dao>=5&&dao<15){
			lie=(dao+5)%10+"";
			han = "2" ;
		}else if(dao>=15&&dao < 25){
			lie=(dao+5)%10+"";
			han = "3" ;
		}else if (dao>=25&&dao < 35){
			lie=(dao+5)%10+"";
			han = "4" ;
		}else if(dao>=35&&dao <45){
			lie=(dao+5)%10+"";
			han = "5" ;
		}


//		  lie =  "" + (dao%10 );  // 个位数
		byte bytehan = lie.getBytes()[0];

//		  han =  "" + (dao/10 );  // 十位数
		byte bytelie = han.getBytes()[0];
		
		String hui = "出货命令发送4次，对方无应答。";

		// 开始出货物
		// 通讯格式：
		//  起始标志(0xAA)+命令字(1 byte)S+数据包(5 bytes)+校验码（1 bytes）+结束码（0xAC）
		//  1Byte列+1Byte行+1Byte(光幕是否要检测) + 2Byte层高   10轨道  列：00开始  行：01开始
		//  校验码 = 命令字 ^ 数据包
		byte[] fa_cmd = new byte[]{ (byte) 0xAA,(byte) 0x53,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x41,(byte) 0xAC};
		fa_cmd[2] = bytelie;
		fa_cmd[3] = bytehan;
		fa_cmd[7] = (byte) (fa_cmd[1]^fa_cmd[2]^fa_cmd[3]^fa_cmd[4]^fa_cmd[5]^fa_cmd[6]);




		long nowTime = 0l;
		long endWhile = 0l;
		boolean sendOK = false;
		// 发送出货指令
		for(int sendcount = 1;sendcount <= 4;sendcount++){
			// 最多发送4次
			if (null != serialPort.mFd) {
				rxByteArray = null;			// 接收到的字节信息
				Log.i(TAG, "com_track发送的数据_出货：" + bytesToHexString(fa_cmd,fa_cmd.length));
				serialPort.writeBytes(fa_cmd);
				
				nowTime = System.currentTimeMillis();
				endWhile = nowTime + 250;  // 最多等待250毫秒=0.25秒
				while(nowTime < endWhile){
					SystemClock.sleep(10);
					if(rxByteArray == null){
						// 需要再次发送,继续循环
						nowTime = System.currentTimeMillis();
						continue;  //while
					} else {
						break;
					}
				}
				
				if(rxByteArray == null){
					continue;  //for
				}
				
				// 哈哈，有数据了
				Log.i(TAG, "接收到数据：" + bytesToHexString(rxByteArray,rxByteArray.length));
				int lastcode = 2;
				if(rxByteArray.length == 5 || rxByteArray.length == 10 || rxByteArray.length == 15 || rxByteArray.length == 20){
					if(rxByteArray.length == 10)
						lastcode = 7;
					if(rxByteArray.length == 15)
						lastcode = 12;
					if(rxByteArray.length == 20)
						lastcode = 17;
					
					// 收到正确长度的数据后
					// 查看收到回复的数据
					if( rxByteArray[lastcode] == (byte) 0x31 ){  
						// 说明正忙，需要再次发送
						// 继续循环
						continue;
						
					} else if( rxByteArray[lastcode] == (byte) 0x30 ){
						// 表示处理已接收
						sendOK = true;
						break;
						
					} else {
						hui = "未知错误、收到的内容：" + bytesToHexString(rxByteArray,rxByteArray.length) ;
							
						Log.e(TAG, "出货命令后收到的数据的内容：其它" + "、发货发送次数：" + sendcount);

						return hui;
					}
				} else {
					// 说明收到的数据的长度不对
					hui = "出货命令后收到的数据的长度不对：" + rxByteArray.length;
					
					Log.e(TAG, hui + "、发货发送次数共：" + sendcount);
					return hui;
				}
				
			}
		}
		
		if(sendOK){
			hui = "出货命令发送成功，对方已接收，查询处理结果对方无响应。";
			//表示处理已接收，这时需要查询状态的
			
			// 到这一步说明，发送出货指令成功了，机器正在出货。接下是 查询指令执行结果
			long nowMills = System.currentTimeMillis(); // 现在的毫秒数
			long whileEndMills = nowMills + 30*1000;  // 最长30秒的时间，等待出咖完成！
			
			// 0xAA+R+1Byte列+1Byte行+3Byte层高+checknum+END
			// 正在忙: 0xAA+R+0x30+checknum+END; 完成: 0xAA+R+大于0x30+checknum+END
			fa_cmd = new byte[]{ (byte) 0xAA,(byte) 0x52,(byte) 0x00,(byte) 0x00,(byte) 0x31,(byte) 0x31,(byte) 0x31,(byte) 0x41,(byte) 0xAC};
			fa_cmd[2] = bytelie;
			fa_cmd[3] = bytehan;
			fa_cmd[7] = (byte) (fa_cmd[1]^fa_cmd[2]^fa_cmd[3]^fa_cmd[4]^fa_cmd[5]^fa_cmd[6]);
			
			int whilecount = 0;
			while (nowMills < whileEndMills){
				whilecount ++;

				rxByteArray = null;			// 接收到的字节信息
				Log.i(TAG, "com_track发送的数据_查询：" + bytesToHexString(fa_cmd,fa_cmd.length));
				serialPort.writeBytes(fa_cmd);
				
				nowTime = System.currentTimeMillis();
				endWhile = nowTime + 250;  // 最多等待250毫秒=0.25秒
				while(nowTime < endWhile){
					SystemClock.sleep(10);
					if(rxByteArray == null){
						// 需要再次发送,继续循环
						nowTime = System.currentTimeMillis();
						continue;  //while
					} else {
						break;
					}
				}
				
				if(rxByteArray == null){
					// 需要再次发送,继续循环
					nowMills = System.currentTimeMillis();
					continue;
				}
				
				// 哈哈，有数据了
				Log.i(TAG, "接收到数据：" + bytesToHexString(rxByteArray,rxByteArray.length));
				int lastquery = 2;
				if (rxByteArray.length == 5 || rxByteArray.length == 10 || rxByteArray.length == 15 || rxByteArray.length == 20){
					if(rxByteArray.length == 10)
						lastquery = 7;
					if(rxByteArray.length == 15)
						lastquery = 12;
					if(rxByteArray.length == 20)
						lastquery = 17;
					
					if( rxByteArray[lastquery] == (byte) 0x30){
						// 说明正在处理中，需要再次发送
						//SystemClock.sleep(250);   //等待0.25秒   不等待，马上继续发送查询指令。 2015-3-7 陈啸龙
						// 继续循环
						nowMills = System.currentTimeMillis();
						continue;
						
					} else {
						// 说明有结果了   
	
						byte resultMotor = (byte) (rxByteArray[lastquery] & 0x03);
						if(resultMotor == (byte)0x01){
							// 都正常了
							hui = "";
							
							Log.e(TAG, "出货完成");
							
						} else if(resultMotor == (byte)0x00){
							hui = "电机运行没到位。"  ;
							
							Log.e(TAG, "电机运行没到位。数据的长度：" + rxByteArray.length + "、查询发送次数："  + whilecount);
						} else if(resultMotor == (byte)0x02){
							hui = "电机运行超时。" ;
							
							Log.e(TAG, "电机运行超时。数据的长度：" + rxByteArray.length + "、查询发送次数：" + whilecount);
						} else {
							hui = "未知的错误" ;
							
							Log.e(TAG, "未知的错误。数据的长度：" + rxByteArray.length + "、查询发送次数：" + whilecount);
						}
						return hui;
					}
				} else {
					// 说明收到的数据的长度不对
					hui = "查询命令后收到的数据的长度不对：" + rxByteArray.length ; 
					
					Log.e(TAG, "查询命令后收到的数据的长度不对：" + rxByteArray.length + "、查询循环次数：" + whilecount);
					
					//SystemClock.sleep(250);   //等待0.25秒   不等待，马上继续发送查询指令。 2015-3-7 陈啸龙
					
					//再次去查询出货的结果
					nowMills = System.currentTimeMillis();
					continue;
				}
			}  // while ----- end
		} 
			
		// 错误返回
		return hui;
	}
	

}
