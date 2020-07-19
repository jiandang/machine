package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dwin.navy.serialportapi.com_zhongji;
import com.example.myapp.R;
import com.example.myapplication.bean.Student;
import com.example.myapplication.util.Comment;
import com.example.myapplication.util.Heart;
import com.example.myapplication.util.MD5Util;
import com.example.myapplication.util.MyGridView;
import com.example.myapplication.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import okhttp3.Call;

public class CeShiActivity extends AppCompatActivity {
    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    //    private ReadThread mReadThread;
    private TextView tv;
    private EditText et;
    private Button btn_send;
    private String devices = "";
    private int baudrates;
    private SharedPreferences spf;
    private String data;
    StringBuffer bufffer = new StringBuffer("");
    StringBuffer sb = new StringBuffer("");
    private String data0015;
    private String YuE;
    private long timeStamp;
    private String jinE;
    private byte[] rxByteArray = null;// 接收到的字节信息
    private String TAG = "CeshiActivity";
    private int isFrist = 0;
    private Runnable runnable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.d(TAG, "handleMessage: --------------------------------->");
                    break;
            }
        }
    };
    private MyGridView gv;
    /**
     * 中吉串口API
     */
    private com_zhongji zhongjiSerial;
    /**
     * 数据库的配置信息对象
     */
    private DbManager.DaoConfig daoConfig;

    private WebView web;
    private Logger gLogger;

    public void configLog() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("CrifanLiLog4jTest");
    }

    int a = 1;
    private int duration = 5;      //倒计时3秒
    Timer timer = new Timer();
    private Runnable runnableState;
    String state = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ce_shi);
//        configLog();
//        gLogger.debug("test android log to file in sd card using log4j");
        //初始化数据库配置信息
        initDaoConfig();
        final Button buttonSetup = (Button) findViewById(R.id.ButtonSetup);
        //点击跳转设置串口
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                startActivityForResult(new Intent(CeShiActivity.this, SerialPortPreferences.class), 0);
//                startActivity(new Intent(CeShiActivity.this, EquipmentAndManageActivity.class));
            }
        });

        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        btn_send = (Button) findViewById(R.id.btn);
        spf = getSharedPreferences("order", Context.MODE_PRIVATE);

        //点击向硬件发送命令
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectAID();
                Log.d(TAG, "on: ------------------------------------------------");
                //随机数
//                String random = Utils.randomHexString(8);
//                Log.d(TAG, "onClick: "+random);
                //获取当前日期
//                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//                String s = sdf.format(new Date());
//                Log.d(TAG, "onClick: "+s);
                //获取当前时间
//                SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
//                String s1 = sdf1.format(new Date());
//                Log.d(TAG, "onClick: "+s1);
                a++;
                String et = CeShiActivity.this.et.getText().toString();
                String ssLast = Integer.toHexString(Integer.parseInt(et)*100);
                Log.d("TAG", "onClick: "+ssLast);
                if (ssLast.length() % 2 != 0) {
                    ssLast = "0" + ssLast;//0F格式
                }
                if(ssLast.length() == 2){
                    ssLast = "00" + ssLast;//0F格式
                }
                String sLast = ssLast.toUpperCase();
                Log.d("TAG", "onCreate: "+sLast);
            }
        });
        Log.d(TAG, "fhdsakfhkdsjk: ------------------------>");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                state = "出货成功，请取货";
//            }
//        });
//
//        final Dialog dialog = builder.create();
//        dialog.show();
//        final Handler handler = new Handler();
//        runnableState= new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 1000);
//                Log.d(TAG, "状态: +"+state);
//                if(state.equals("出货成功，请取货")|| state.equals("设备异常，出货失败")){
//                    handler.removeCallbacks(runnableState);
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            runOnUiThread(new Runnable() {      // UI thread
//                                @Override
//                                public void run() {
//                                    duration--;
//                                    Log.d(TAG, "run: " + duration + "s");
//                                    if (duration < 2) {
//                                        timer.cancel();
//                                        dialog.dismiss();
//                                        Log.d(TAG, "run: ++++++");
//                                    }
//                                }
//                            });
//                        }
//                    }, 1000, 1000);
//                }
//            }
//        };
//        handler.postDelayed(runnableState, 1000);//线程时间1秒刷新
        Log.d(TAG, "onCreate: +++++++++++++++++++++++++++");
//        Heart.get(CeShiActivity.this);
//        sendHeart();
//        sockerStar();
        gv = findViewById(R.id.gv);
    }

    //心跳包
    private void sendHeart() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 3);
                Heart.resume();
                String netWorkInfo = Heart.getNetWorkInfo();
                if (netWorkInfo != null && !netWorkInfo.equals("0")) {
                    Log.d(TAG, "onResume: " + netWorkInfo);
                }
            }
        };
        handler.postDelayed(runnable, 1000 * 3);//线程时间30秒刷新
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Heart.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Heart.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Heart.destory();
    }

    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public void TimerManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15); //1:凌晨1点 我用20：晚上8点试下
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        Log.d(TAG, "TimerManager: " + date);
        Log.d(TAG, "TimerManager: " + new Date());
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 0);
        }
        Timer timer = new Timer();
        Task task = new Task();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task, date, PERIOD_DAY);
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    class Task extends TimerTask {
        public void run() {
            Log.d(TAG, "我有一头小毛驴!");
            String url = Comment.URL + "/terminal/signin";
            timeStamp = getTimeStamp();
            Log.d("xxxxx", timeStamp + "");
            //auth_sign MD5加密
            String ciphertext = MD5Util.encrypt(Comment.terminal_code + timeStamp + Comment.signatureMiYao);// "12345678":签到秘钥 先用这个，之后会改动的
            final Map<String, String> params = new HashMap<>();
            params.put("terminal_code", Comment.terminal_code);//终端编号（平台定义）
            params.put("timestamp", String.valueOf(timeStamp));//Unix时间戳（秒级）
            params.put("auth_sign", ciphertext);//签名

            OkHttpUtils.post().url(url).params(params).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.d("TAG", "onResponseD:失败" + e);
                    Log.d("TAG", "onResponseD:失败" + params);
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("TAG", "onResponseD:成功" + response);
                }
            });
        }
    }

    //秒级时间戳获取
    private long getTimeStamp() {
        //秒级时间戳获取
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    int i = 0;

    private void sockerStar() {
        //几秒后发送获取订单状态
        runnable = new Runnable() {
            @Override
            public void run() {
                //定时向后台获取订单状态
                handler.postDelayed(this, 1000 * 3);
                Log.d(TAG, "run: ------------------------>");
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        };
        handler.postDelayed(runnable, 3000);//线程时间1秒刷新
    }

    /**
     * 增加数据,插入数据库操作的时候会判断是否存在这张表，如果不存在就会去创建,所以不需要手动去创建表了
     */
    DbManager db;

    public void addData() {
        try {
            //根据配置信息获取操作数据的db对象
            db = x.getDb(daoConfig);
//            List<Student> list = new ArrayList<Student>();
//            for (int i = 0; i < 10; i++) {
//                Student stu = new Student();
//                stu.setAge(10+i);
//                stu.setName("学生"+i);
//                stu.setSex(i % 2 == 0? "男":"女");
//                list.add(stu);
//                //db.save(stu);//插入一条数据
//            }
//            db.save(list);//保存实体类或实体类的List到数据库
            //db.replace(list);保存或更新实体类或实体类的List到数据库, 根据id和其他唯一索引判断数据是否存在
            //db.saveOrUpdate(list);保存或更新实体类或实体类的List到数据库, 根据id对应的数据是否存在.
            //db.saveBindingId(list);保存实体类或实体类的List到数据库,如果该类型的id是自动生成的, 则保存完后会给id赋值.
            /**
             * 1.如果在你建表的时候你的主键设置成自增长，那么你在插入数据的时候直接调replace方法就可以了，
             *   但是saveOrUpdate只能达到插入的效果，达不到更新原有数据的效果.
             * 2.如果在你建表的时候你的主键设置成不是自增长，replace方法当然可以插入，saveOrUpdate方法既可以插入也可以达到更新的效果
             */
            Student stu = new Student();
            stu.setAge(23);
            stu.setName("孙");
            stu.setSex("女");
            stu.setAaaaa("123");
            db.save(stu);//插入一条数据
        } catch (DbException e) {
            e.printStackTrace();
            try {
                db.dropTable(Student.class);//删除表
                addData();
            } catch (DbException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 查询数据
     */
    public void onQuertyData() {
        try {
            DbManager db = x.getDb(daoConfig);
//            Student student = db.findById(Student.class, 2);//根据主键来查找student表里的数据
//            Log.i("tag", "findById:"+student);
//            Student first = db.findFirst(Student.class);//返回当前表的第一条数据
//            Log.i("tag", "第一条数据:"+first);
            //查询所有数据
            List<Student> all = db.findAll(Student.class);
            Log.i("tag", "所有数据:" + all.toString());
            //按条件查找,查询年龄大于15的
//            List<DbModel> dbModelAll = db.findDbModelAll(new SqlInfo("select * from student where age > 15"));
//            for (int i = 0; i < dbModelAll.size() ; i++) {
//                String name = dbModelAll.get(i).getString("name");
//                String age = dbModelAll.get(i).getString("age");
//                String sex = dbModelAll.get(i).getString("sex");
//                Log.i("tag", "查询的数据: name="+name+",age="+age+",sex="+sex);
//            }
//            //第二种条件查找
//            List<Student> all1 = db.selector(Student.class).where("age", ">", 14).and("age", "<", 16).findAll();
//            Log.i("tag", "第二种:"+all1.toString());
//            //第三种
//            List<Student> all2 = db.selector(Student.class).expr("age>14 and age<17").findAll();
//            Log.i("tag", "第三种:"+all2.toString());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void init(String data) {
        try {
//            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 115200, 0);
            Log.d("TAG", "onClick: " + data);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            byte[] bytes = Utils.hexStr2Byte(data);
            mOutputStream.write(bytes);
            Log.i("test2", "发送成功");
//            Toast.makeText(getActivity(), "发送成功！！！", Toast.LENGTH_SHORT).show();
//            mReadThread = new ReadThread();
//            mReadThread.start();
        } catch (IOException e) {
            Log.i("test", "发送失败");
//            Toast.makeText(getActivity(), "发送失败！！！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override//设置串口的返回值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        devices = data.getStringExtra("devices");
        baudrates = Integer.parseInt(data.getStringExtra("baudrates"));
        Log.d("TAG", "onActivityResult: " + devices + baudrates);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("devices", devices);
        editor.putInt("baudrates", baudrates);
        editor.commit();
    }

    /**
     * 初始化获取数据库的配置信息
     */
    public void initDaoConfig() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("my.db")  //设置数据库名称
                .setDbVersion(1)  //设置数据库版本
                .setDbDir(null) //设置数据库保存的路径  getCacheDir().getAbsoluteFile()
                .setAllowTransaction(true) //设置允许开启事务
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                        try {
//                            db.addColumn(Student.class,"bbbb");//数据库更新监听
//                            db.addColumn(Student.class,"classes");//数据库更新监听
//                            db.addColumn(Student.class,"aaaaa");//数据库更新监听
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启WAL.对写入加速提示很大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("tag", "onTableCreated: 创建了表:" + table.getName());
                    }
                });
    }
}
