package cn.zl.util;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 发件人账号密码
 * @author zhangdi
 *
 */
public class MailAuthenticator extends   Authenticator{
    public static String USERNAME = "";
    public static String PASSWORD = "";
    public MailAuthenticator() {
    }
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }
}