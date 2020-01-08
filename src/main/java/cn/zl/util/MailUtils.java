package cn.zl.util;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties; /**
 * 邮件发送操作类
 *
 * @author zhangdi
 *
 */
public class MailUtils {
    /**
     * 发送邮件
     * @param user 发件人邮箱
     * @param password 授权码（注意不是邮箱登录密码）
     * @param host
     * @param from 发件人
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return success 发送成功 failure 发送失败
     * @throws Exception
     */
    public static String sendMail(String user, String password, String host,
                                  String from, String to, String subject, String content)
            throws Exception {
        if (to != null){
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            MailAuthenticator auth = new MailAuthenticator();
            MailAuthenticator.USERNAME = user;
            MailAuthenticator.PASSWORD = password;
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                if (!to.trim().equals("")){
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(to.trim()));
                }
                message.setSubject(subject);
                // 正文
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setContent(content, "text/html;charset=utf-8");
                // 整个邮件：正文+附件
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                message.setContent(mp);
                message.setSentDate(new Date());
                message.saveChanges();
                Transport trans = session.getTransport("smtp");
                Transport.send(message);
                System.out.println(message.toString());
            } catch (Exception e){
                e.printStackTrace();
                return "failure";
            }
            return "success";
        }else{
            return "failure";
        }
    }

    public static void main(String[] args) {
        //需要你的163邮箱账户
        String user = "18638783140@163.com";
        //163授权码，不是登录密码
        String password = "a124850";
        String host = "smtp.163.com";
        //发件人
        String from = "18638783140@163.com";
        // 收件人
        String to = "1248505157@qq.com";
        //邮件标题
        String subject = "邮件主题";
        //邮箱内容
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE>"+"点击下面链接激活"+"<a href=update?id="+" >点击激活</a>"
               );
        try {
            String res = sendMail(user, password, host, from, to,
                    subject, sb.toString());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

