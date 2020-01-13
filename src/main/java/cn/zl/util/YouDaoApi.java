package cn.zl.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
public class YouDaoApi {
    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    private static final String APP_KEY = "3b2c33cc9cd2b1c2";

    private static final String APP_SECRET = "BLBJYqChArbdJez2dztB4FvBO6vlvPl7";

    public static void main(String[] args) throws IOException {
        String s = translateTxt("我的名字叫小红", "auto", "en");
        System.out.println(s);
    }
public static String translateTxt(String txt,String form,String to) throws IOException {
        String s=null;
    Map<String, String> params = new HashMap<String, String>();
    String salt = String.valueOf(System.currentTimeMillis());
    params.put("from", form);
    params.put("to", to);
    params.put("signType", "v3");
    String curtime = String.valueOf(System.currentTimeMillis() / 1000);
    params.put("curtime", curtime);
    String signStr = APP_KEY + truncate(txt) + salt + curtime + APP_SECRET;
    String sign = getDigest(signStr);
    params.put("appKey", APP_KEY);
    params.put("q", txt);
    params.put("salt", salt);
    params.put("sign", sign);
    /** 处理结果 */
    s=requestForHttp(YOUDAO_URL, params);
    JSONObject jsonObject = JSON.parseObject(s);
    JSONArray array =  jsonObject.getJSONArray("translation");
    int length=array.size();
    for(int i=0;i<length;i++){
       s=array.get(i).toString();
    }
    return s;
}
    public static String requestForHttp(String url,Map<String,String> params) throws IOException {
        String json=null;
        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();
        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key,value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try{
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            if("audio/mp3".equals(contentType[0].getValue())){
                //如果响应是wav
                HttpEntity httpEntity = httpResponse.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(baos);
                byte[] result = baos.toByteArray();
                EntityUtils.consume(httpEntity);
                if(result != null){//合成成功
                    String file = "F:\\"+System.currentTimeMillis() + ".mp3";
                    byte2File(result,file);
                }
            }else{
                /** 响应不是音频流，直接显示结果 */
                HttpEntity httpEntity = httpResponse.getEntity();
                 json = EntityUtils.toString(httpEntity,"UTF-8");
            }
        }finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }else if(httpResponse.getEntity()!=null){
                    EntityUtils.consume(httpResponse.getEntity());
                }
            }catch(IOException e){
            }
        }
        return json;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     *
     * @param result 音频字节流
     * @param file 存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        }catch (Exception e){
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}