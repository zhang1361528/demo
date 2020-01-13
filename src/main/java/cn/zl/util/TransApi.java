package cn.zl.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度翻译接口
 *
 * @author Administrator
 */
public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    // 10S
    protected static final int SOCKET_TIMEOUT = 10000;
    protected static final String GET = "GET";
    private static String appid="20190418000289341";
    private static String securityKey="oJ9EwzQG2344l4_l8kL1";

    /**
     * @param host
     * @param params
     * @return
     */
    public static String get(String host, Map<String, String> params) {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
            String sendUrl = getUrlWithQueryString(host, params);
            // 创建URL对象
            URL uri = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(sslcontext.getSocketFactory());
            }
            // 设置相应超时
            conn.setConnectTimeout(SOCKET_TIMEOUT);
            conn.setRequestMethod(GET);
            int statusCode = conn.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Http错误码：" + statusCode);
            }

            // 读取服务器的数据
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            String text = builder.toString();
            // 关闭数据流
            close(br);
            // 关闭数据流
            close(is);
            // 断开连接
            conn.disconnect();
            return text;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            // 过滤空的key
            if (value == null) {
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    protected static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对输入的字符串进行URL编码, 即转换为%20这种形式
     *
     * @param input 原文
     * @return URL编码. 如果编码失败, 则返回原文
     */
    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };

    /**
     * 开发者信息验证
     *
     * @param appid
     * @param securityKey
     */
    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return get(TRANS_API_HOST, params);
    }
    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        // 加密前的原文
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }

    /**
     * 翻译
     * @param query
     * @return
     */
    public static String translate(String query,String from,String to){
        TransApi api = new TransApi(appid, securityKey);
        String result=api.getTransResult(query, from, to);
        String date="";
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray array =  jsonObject.getJSONArray("trans_result");
        int length=array.size();
        for(int i=0;i<length;i++){
            JSONObject params=JSON.parseObject(array.getString(i));
            String str=params.getString("dst");
            try {
                str= URLDecoder.decode(str,"utf-8");
                date=str;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
    public static void main(String[] args){
        String s = translate("三人行,必有我师焉","wyw","en");
        System.out.println(s);
    }
}
