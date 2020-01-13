package cn.zl.controller;

import cn.zl.dao.UserDao;
import cn.zl.pojo.User;
import cn.zl.util.MobileUtil;
import cn.zl.util.TransApi;
import cn.zl.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static cn.zl.util.MailUtils.sendMail;

/**
 * @author Administrator
 */
@Controller
public class UserController {
    @Autowired
    private UserDao userDao;

    @GetMapping("user")
    @ResponseBody
    public Object uu(String name) {
        boolean b = false;
        if (!Util.isEmpty(name)) {
            User user = userDao.getUserByName(name);
            if (!Util.isEmpty(user)) {
                b = true;
                return b;
            }
            return b;
        }
        return b;
    }

    @PostMapping("log")
    @ResponseBody
    public Object log(HttpServletRequest request, HttpServletResponse response, User user) {
        List<User> list = userDao.findAll();
        boolean b = false;
        for (User u : list) {
            if (!user.getUserName().equals(u.getUserName())) {
                b = true;
                return b;
            }
        }
        return b;
    }

    @RequestMapping("/sign")
    public Object index() throws IOException {
        return "sign";
    }

    @RequestMapping("list")
    public Object list() {
        ModelAndView view = new ModelAndView();
        List<User> all = userDao.findAll();
        view.addObject("list", all);
        view.setViewName("list");
        return view;
    }

    @PostMapping("addUser")
    public Object add(User user, BindingResult err) throws IOException {
        ModelAndView view = new ModelAndView();
        userDao.save(user);
        view.setViewName("ok");
        return view;
    }

    /**
     * @return
     */
    @GetMapping("index")
    public Object index1() {
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }

    @GetMapping("login")
    public Object login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    @GetMapping("log1")
    public Object log1(String userName, int userPwd, HttpServletResponse response, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        User user = userDao.getuser(userName, userPwd);
        if (Util.isEmpty(user)) {
            view.addObject("err", "用户名或密码错误");
            view.setViewName("login");
            return view;
        }
        request.getSession().setAttribute("user", user);
        Cookie cookie = new Cookie("user", "user");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        view.setViewName("index");
        return view;
    }

    @GetMapping("msm")
    @ResponseBody
    public void msm(String mobile, HttpSession session) {
        int i = MobileUtil.smsUtil(mobile);
        session.setAttribute("msm", i);
    }

    @GetMapping("msm1")
    @ResponseBody
    public Object msm1(int s, HttpSession session) {
        if (!Util.isEmpty(s) || s != 0) {
            try {
                int msm = (int) session.getAttribute("msm");
                if (!Util.isEmpty(msm)) {
                    if (msm == s) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    @GetMapping("email")
    public Object email(String email, HttpServletRequest request) throws Exception {
        String user = "18638783140@163.com";
        //163授权码，不是登录密码
        String password = "a124850";
        String host = "smtp.163.com";
        //发件人
        String from = "18638783140@163.com";
        // 收件人
        String to = email;
        String subject = "激活邮箱";
        //邮箱内容
        StringBuffer sb = new StringBuffer();
        User user1 = (User) request.getSession().getAttribute("user");
        String url = "http://localhost:8080/update?id=" + user1.getUserId();
        sb.append("<!DOCTYPE>" + "点击下面链接激活" + "<a href=" + url + ">点击激活</a>");
        sendMail(user, password, host, from, to,
                subject, sb.toString());
        return "email";
    }
    @GetMapping("update")
    @Transactional
    public Object update(int id) {
        int i = userDao.update(id);
        if (i > 0) {
            return "succeed";
        }
        return "error";
    }

    public static void main(String[] args) {
        String APP_ID = "20190418000289341";
       String SECURITY_KEY = "oJ9EwzQG2344l4_l8kL1";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String s = api.getTransResult("你好吗", "zh", "en");
        System.out.println(s);


    }
}