package com.shaoming.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jerry on 2017/7/18.
 */
public class HttpRequestUtil {

    // UTF-8字符编码
    public static final String CHARSET_UTF_8 = "utf-8";
    // 浏览器可接受的MIME类型
    public static final String HTTP_ACCEPT = "application/json, text/javascript, */*; q=0.01";
    // HTTP内容类型
    public static final String HTTP_CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String DEF_CHARSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;

    /**
     * 发送请求，并且返回数据
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法（GET/POST）
     * @param charset 编码格式(GBK/UTF-8/GB2312)
     * @param isEncode 是否需要转码 true：需要转码，false：不需要转码
     * @return  网络请求字符串
     * @throws IOException
     */
    public static String doRequest(String strUrl, Map<String, Object> params, HttpRequestMethod method, String charset, Boolean isEncode) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(params != null && (method==null || method.equals(HttpRequestMethod.GET))){
                strUrl = strUrl+"?"+formatParams(params, charset, isEncode);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            conn.setRequestMethod(HttpRequestMethod.GET.name());
            if(HttpRequestMethod.POST.equals(method)){
                conn.setDoOutput(true);
            }
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals(HttpRequestMethod.POST)) {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(formatParams(params, charset, isEncode));
            }
            InputStream is = conn.getInputStream();
            reader = StringUtils.isNotBlank(charset) ? new BufferedReader(new InputStreamReader(is, charset)) : new BufferedReader(new InputStreamReader(is, DEF_CHARSET));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 格式化参数
    private static String formatParams(Map<String, Object> params, String charset, Boolean isEncode){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, Object> map = iterator.next();
                if (isEncode)
                    sb.append(map.getKey()).append("=").append(URLEncoder.encode(map.getValue().toString(), StringUtils.isNotBlank(charset) ? charset : DEF_CHARSET));
                else
                    sb.append(map.getKey()).append("=").append(map.getValue().toString());
                if (iterator.hasNext()) sb.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 发送POST请求，并且返回数据
     * @param url 请求地址
     * @param content 请求参数
     * @return  网络请求字符串
     * @throws IOException
     */
    public static String doPost(String url, String content) throws IOException {
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            // 统一资源
            URL realURL = new URL(url);
            // HTTP的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) realURL.openConnection();

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(content.length()));
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置一个指定的超时值（以毫秒为单位）
            httpURLConnection.setConnectTimeout(DEF_CONN_TIMEOUT);
            // 将读超时设置为指定的超时，以毫秒为单位。
            httpURLConnection.setReadTimeout(DEF_READ_TIMEOUT);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设置浏览器可接受的MIME类型
            httpURLConnection.setRequestProperty("Accept", HTTP_ACCEPT);
            // 设置字符编码
            httpURLConnection.setRequestProperty("Accept-Charset", CHARSET_UTF_8);
            // 设置内容类型
            httpURLConnection.setRequestProperty("Content-Type", HTTP_CONTENT_TYPE);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");

            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            // 如果在已打开连接（此时 connected 字段的值为 true）的情况下调用 connect 方法，则忽略该调用。
            httpURLConnection.connect();

            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(content);
            outputStreamWriter.flush();// 刷新

            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally { // 关闭流
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }
}
