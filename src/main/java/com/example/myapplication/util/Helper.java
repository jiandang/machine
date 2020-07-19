package com.example.myapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Helper {
    /**
     * 作用：实现网络访问文件，将获取到数据储存在文件流中
     *
     * @param ：访问网络的url地址
     * @return inputstream
     */
    public static InputStream loadFileFromURL(String urlString) {
        BufferedInputStream bis = null;
        HttpURLConnection httpConn = null;
        try {
            // 创建url对象
            URL urlObj = new URL(urlString);
            // 创建HttpURLConnection对象，通过这个对象打开跟远程服务器之间的连接
            httpConn = (HttpURLConnection) urlObj.openConnection();

            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(5000);
            httpConn.connect();

            // 判断跟服务器的连接状态。如果是200，则说明连接正常，服务器有响应
            if (httpConn.getResponseCode() == 200) {
                // 服务器有响应后，会将访问的url页面中的内容放进inputStream中，使用httpConn就可以获取到这个字节流
                bis = new BufferedInputStream(httpConn.getInputStream());
                return bis;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 对流对象进行关闭，对Http连接对象进行关闭。以便释放资源。
                bis.close();
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到的数据存在字节数组中
     *
     * @param url ：访问网络的url地址
     * @return byte[]
     */
    public static byte[] loadByteFromURL(String url) {
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.connect();
            Log.d("TAG", "loadByteFromURL: " + httpConn.getResponseCode());
//			if (httpConn.getResponseCode() == 200) {
            bis = new BufferedInputStream(httpConn.getInputStream());
            Log.d(TAG, "loadByteFromURL: +++++++++++++++" + bis);
            return streamToByte(bis);
//			}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到的数据保存在指定目录中
     *
     * @param url ：访问网络的url地址
     * @return byte[]
     */
    public static boolean saveFileFromURL(String url, File destFile) {
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.connect();

            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                int c = 0;
                byte[] buffer = new byte[8 * 1024];
                while ((c = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, c);
                    bos.flush();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 作用：实现网络访问文件，先给服务器通过“POST”方式提交数据，再返回相应的数据
     *
     * @param url    ：访问网络的url地址
     * @param params ：访问url时，需要传递给服务器的参数。格式为：username=wangxiangjun&password=abcde&
     *               qq=32432432
     *               为了防止传中文参数时出现编码问题。采用URLEncoder.encode()对含中文的字符串进行编码处理。
     *               服务器端会自动对进行过编码的字符串进行decode()解码。
     * @return byte[]
     */
    public static byte[] doPostSubmit(String url, String params) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpConn = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();

            // 如果通过post方式给服务器传递数据，那么setDoOutput()必须设置为true。否则会异常。
            // 默认情况下setDoOutput()为false。
            // 其实也应该设置setDoInput()，但是因为setDoInput()默认为true。所以不一定要写。

            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setConnectTimeout(5 * 1000);
            // 设置请求方式。请求方式有两种：POST/GET。注意要全大写。
            // POST传递数据量大，数据更安全，地址栏中不会显示传输数据。
            // 而GET会将传输的数据暴露在地址栏中，传输的数据量大小有限制，相对POST不够安全。但是GET操作灵活简便。

            // 判断是否要往服务器传递参数。如果不传递参数，那么就没有必要使用输出流了。
            if (params != null) {
                byte[] data = params.getBytes();
                OutputStream outputStream = httpConn.getOutputStream();
                bos = new BufferedOutputStream(outputStream);
                bos.write(data);
                bos.flush();
            }
            // 判断访问网络的连接状态
            Log.d(TAG, "doPostSubmit: ++++++"+httpConn.getResponseCode());
            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                // 将获取到的输入流转成字节数组
                return streamToByte(bis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final static String TAG = "HTTP";
    private final static int CONNECT_TIME = 10000;
    private final static int READ_TIME = 10000;

    /**
     * 发送post请求
     *
     * @param urlstr
     * @param map
     * @param encoding 字节编码
     * @return
     * @throws IOException
     */
    public static String doPost(String urlstr, Map<String, String> map, String encoding)
            throws IOException {

        StringBuilder data = new StringBuilder();
        // 数据拼接 key=value&key=value
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(URLEncoder.encode(entry.getValue(), encoding));
                data.append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }

        Log.i(TAG, data.toString());
        byte[] entity = data.toString().getBytes();// 生成实体数据
        URL url = new URL(urlstr);
        HttpURLConnection connection = getHttpURLConnection(urlstr, "POST");

        connection.setDoOutput(true);// 允许对外输出数据
        connection.setRequestProperty("Content-Length",
                String.valueOf(entity.length));

        OutputStream outStream = connection.getOutputStream();
        outStream.write(entity);
        if (connection.getResponseCode() == 200) {// 成功返回处理数据
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }

        return null;

    }


    public String doPost(String urlstr) throws IOException {
        return doPost(urlstr, null, "UTF-8");
    }

    public String doPost(String urlstr, Map<String, String> map)
            throws IOException {
        return doPost(urlstr, map, "UTF-8");
    }

    private static HttpURLConnection getHttpURLConnection(String urlstr, String method)
            throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIME);
        connection.setReadTimeout(READ_TIME);
        connection.setRequestMethod(method);

        // 头字段
//		connection.setRequestProperty("Accept", "*/*");
//		connection.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
//		connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
//		connection.setRequestProperty("Accept-Language", "zh-CN");
//		connection.setRequestProperty("User-Agent", "Android WYJ");
//		connection.setRequestProperty("Content-Type",
//				"application/x-www-form-urlencoded");// 头字段

        return connection;

    }

    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 网络访问，上传附件 五个参数： 1、String url：指定表单提交的url地址 2、Map<String, String>
     * map：将上传控件之外的其他控件的数据信息存入map对象 3、String filePath：指定要上传到服务器的文件的客户端路径
     * 4、byte[] body_data：获取到要上传的文件的输入流信息，通过ByteArrayOutputStream流转成byte[]
     * 5、String charset：设置字符集
     */
    public static byte[] upLoad(String host,String path) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        BufferedInputStream bis = null;
        try {
            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                    + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
//            if(file!=null) {
//                /** * 当文件不为空，把文件包装并且上传 */
//                //写入文件数据
//                FileInputStream fs = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=fs.read(bytes))!=-1){
//                    dos.write(bytes, 0, len);
//                }
//                fs.close();
//                dos.flush();
                // 判断访问网络的连接状态
                if (conn.getResponseCode() == 200) {
                    bis = new BufferedInputStream(conn.getInputStream());
                    // 将获取到的输入流转成字节数组
                    return streamToByte(bis);
                }
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网络访问，上传附件 五个参数： 1、String url：指定表单提交的url地址 2、Map<String, String>
     * map：将上传控件之外的其他控件的数据信息存入map对象 3、String filePath：指定要上传到服务器的文件的客户端路径
     * 4、byte[] body_data：获取到要上传的文件的输入流信息，通过ByteArrayOutputStream流转成byte[]
     * 5、String charset：设置字符集
     */
    public static String doPostSubmitBody(String url, Map<String, String> map,
                                          String filePath, byte[] body_data, String charset) {
        // 设置三个常用字符串常量：换行、前缀、分界线（NEWLINE、PREFIX、BOUNDARY）；
        final String NEWLINE = "\r\n";
        final String PREFIX = "--";
        final String BOUNDARY = "#";// 取代---------------------------7df3a01e37070c
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL urlObj = new URL(url);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            httpConn = (HttpURLConnection) urlObj.openConnection();
            // 调用HttpURLConnection对象setDoOutput(true)、setDoInput(true)、setRequestMethod("POST")；
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent）
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Cache-Control", "no-cache");
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            httpConn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            httpConn.connect();

            // 调用HttpURLConnection对象的getOutputStream()方法构建输出流对象；
            dos = new DataOutputStream(httpConn.getOutputStream());
            // 获取表单中上传控件之外的控件数据，写入到输出流对象（根据HttpWatch提示的流信息拼凑字符串）；
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = map.get(key);
                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                    dos.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + key + "\"" + NEWLINE);
                    dos.writeBytes(NEWLINE);
                    dos.writeBytes(URLEncoder.encode(value.toString(), charset));
                    // 或者写成：dos.write(value.toString().getBytes(charset));
                    dos.writeBytes(NEWLINE);
                }
            }

            // 获取表单中上传控件的数据，写入到输出流对象（根据HttpWatch提示的流信息拼凑字符串）；
            if (body_data != null && body_data.length > 0) {
                dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                String fileName = filePath.substring(filePath
                        .lastIndexOf(File.separatorChar) + 1);
                dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + "uploadFile" + "\"" + "; filename=\"" + fileName
                        + "\"" + NEWLINE);
                dos.writeBytes(NEWLINE);
                dos.write(body_data);
                dos.writeBytes(NEWLINE);
            }
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE);
            dos.flush();

            // 调用HttpURLConnection对象的getInputStream()方法构建输入流对象；
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            // 调用HttpURLConnection对象的getResponseCode()获取客户端与服务器端的连接状态码。如果是200，则执行以下操作，否则返回null；
            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
            }
            // 将输入流转成字节数组，返回给客户端。
            return new String(baos.toByteArray(), charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.close();
                }
                httpConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] streamToByte(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        byte[] buffer = new byte[8 * 1024];
        try {
            while ((c = is.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //通过php返回的url获取网络图片
    public static Bitmap getImageFromNet(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间

            conn.connect(); //开始连接

            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
                return bitmap;

            } else {
                //访问失败
                Log.d("lyf--", "访问失败===responseCode：" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
        return null;
    }
}
