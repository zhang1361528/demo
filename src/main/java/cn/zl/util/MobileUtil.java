package cn.zl.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
public class MobileUtil {
    private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

    public static int smsUtil(String phone) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");

        int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", "C43570458"),
                // new NameValuePair("password", "8e47f1cd55aba8c188efdda722c92b8d"),
                new NameValuePair("password", StringUtil.MD5Encode("8e47f1cd55aba8c188efdda722c92b8d")),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);
        try {
            client.executeMethod(method);
            String SubmitResult = method.getResponseBodyAsString();
            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();
            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");
            if ("2".equals(code)) {
                System.out.println("短信提交成功");
            } else {
                System.out.println("短信提交失败");
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return mobile_code;
    }

}
