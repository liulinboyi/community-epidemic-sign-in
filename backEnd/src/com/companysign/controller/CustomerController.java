package com.companysign.controller;

import com.companysign.po.*;
import com.companysign.service.RegistrationService;
import com.companysign.service.UserService;
import com.companysign.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.companysign.service.CustomerService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    private final int life = 24 * 60; // cookies生存周期

    /**
     * 设置cookies
     *
     * @param response
     */
    public void setCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        for (Cookie c : cookies) {
            if ("username".equals(c.getName())) {
                String value = null;
                try {
                    value = URLDecoder.decode(c.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                username = value;
                break;
            }
        }
        Long timestamp = null;
        int minute = 1000 * life * 60;// 1分钟
        timestamp = (long) (new Date().getTime() + minute);
        Cookie cookie = null;
        try {
            if (username != null) {
                cookie = new Cookie("username", URLEncoder.encode(username, "UTF-8"));
                cookie.setMaxAge(3600 * 24 * 7);
            } else {
                cookie = new Cookie("username", URLEncoder.encode("", "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cookie cookie1 = new Cookie("time", timestamp.toString());
        cookie1.setMaxAge(3600 * 24 * 7);
        response.addCookie(cookie1);
        response.addCookie(cookie);
    }

    /**
     * 转换时间
     *
     * @param date
     * @return
     */
    public Date translateDate(Date date) {
        String last = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(last);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 测试
     * 根据id查询客户详情
     */
    @RequestMapping("/findCustomerById")
    public String findCustomerById(Integer id, Model model) {
//		Customer customer = customerService.findCustomerById(id);
//		model.addAttribute("customer", customer);
        Customer res = new Customer();
        res.setId(1);
        res.setUsername("21312");
        res.setPhone("211");
        res.setJobs("1131");
        model.addAttribute("customer", res);
        //返回客户信息展示页面
        return "customer";
    }

    /**
     * 测试
     * 通过Id查询用户
     *
     * @return Result
     */
    @RequestMapping("/findRegistrationById")
    @ResponseBody
    public Result findRegistrationById(Integer id, HttpServletResponse response) {
        Result res = new Result();
        if (id == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            return res;
        }
        res.setStatus(Result.STATUS_OK);
        res.setCode(Result.CODE_OK);
        res.setData(registrationService.findRegistrationById(id));
        return res;
    }

    /**
     * 通过Id查询用户
     *
     * @return Result
     */
    @RequestMapping("/findUserById")
    @ResponseBody
    public Result findUserById(Integer id, HttpServletResponse response, HttpServletRequest request) {
        Result res = new Result();
        if (id == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            return res;
        }
        res.setStatus(Result.STATUS_OK);
        res.setCode(Result.CODE_OK);
        User user = userService.findUserById(id);
        user.setPassword(null);
        res.setData(user);
        setCookies(request, response);
        return res;
    }

    /**
     * 查询用户签到信息
     *
     * @return Result
     */
    @RequestMapping("/findUserSign")
    @ResponseBody
    public Result findUserSign(User user, HttpServletResponse response, HttpServletRequest request) {
        Result res = new Result();
        if (user == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            return res;
        }
        res.setStatus(Result.STATUS_OK);
        res.setCode(Result.CODE_OK);
        User user1 = userService.findUserSign(user);
        user1.setPassword(null);
        res.setData(userService.findUserSign(user1));
        // 当用户重新获取信息时会刷新cookies的值
        setCookies(request, response);

        return res;
    }

    /**
     * 登录
     *
     * @return Result
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestBody(required = false) User user, HttpServletRequest request, HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*"); // 解决跨域
        Long timestamp = null; // 时间戳

        // 读取cookies，进行判断，保持登录状态
        String username = "";
        boolean hasTime = false; // cookies是否存在time
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if ("time".equals(c.getName())) { // 判断cookies中是否存在time
                    hasTime = true;
                }
            }
            if (hasTime) { // 如果有time
                for (Cookie c : cookies) {
                    if ("time".equals(c.getName())) { // 有效期
                        String value = c.getValue(); // 写入的时间戳
                        Long value1 = Long.parseLong(value);
                        if (!(value1 - new Date().getTime() > 0)) { // 超时
                            username = "";
                            break;
                        }
                    }
                    if ("username".equals(c.getName())) {
                        String value = null;
                        try {
                            value = URLDecoder.decode(c.getValue(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // 将value转成对象，即购物车
//                        String j = JsonPath.read(value, "$.username");
//                        username = j;
//                        System.out.println(j);
                        username = value;
                    }
                }
            }

        }

        if (hasTime && !username.equals("")) { // 如果cookies，有username，并且有time
            Result res = new Result();
            User user1 = new User();
            user1.setUsername(username);
            User curUser = userService.login(user1);
            // 登录成功
            res.setStatus(Result.STATUS_OK);
            res.setCode(Result.CODE_OK);
            res.setMsg("登录成功！");
            curUser.setPassword(null);
            Cookie cookie = null;
            try {
                cookie = new Cookie("username", URLEncoder.encode(curUser.getUsername(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            int minute = 1000 * life * 60;// 1分钟
            timestamp = (long) (new Date().getTime() + minute);
            Cookie cookie1 = new Cookie("time", timestamp.toString());
            assert cookie != null;
            cookie1.setMaxAge(3600 * 24 * 7);
            cookie.setMaxAge(3600 * 24 * 7);// 3600 * 24 => 一天 这里测试用的是1分钟
            cookie.setPath("/");
            response.addCookie(cookie);
            response.addCookie(cookie1);

            res.setData(curUser);
            return res;
        }
        // 读取cookies，进行判断，保持登录状态

        Result res = new Result();
        if (user == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            res.setMsg("Cookies没有'time'或者'username'字段");
            response.setStatus(500);
            return res;
        }

        User curUser = userService.login(user);

        if (user.getPassword().equals(DESUtil.getDecryptString(curUser.getPassword()))) {
            // 登录成功
            res.setStatus(Result.STATUS_OK);
            res.setCode(Result.CODE_OK);
            res.setMsg("登录成功！");
            curUser.setPassword(null);
            Cookie cookie = null;
            try {
                cookie = new Cookie("username", URLEncoder.encode(curUser.getUsername(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            cookie.setPath("/");

            int minute = 1000 * life * 60;// 1分钟
            timestamp = (long) (new Date().getTime() + minute);
            Cookie cookie1 = new Cookie("time", timestamp.toString());
            cookie1.setMaxAge(3600 * 24 * 7);
            cookie.setMaxAge(3600 * 24 * 7);// 3600 * 24 => 一天 这里测试用的是1分钟
            response.addCookie(cookie1);
            response.addCookie(cookie);
            res.setData(curUser);
        } else {
            // 登录失败
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            res.setMsg("登录失败!");
        }
        return res;
    }

    /**
     * 注册
     *
     * @return Result
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        Result res = new Result();
        if (user == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            res.setMsg("没有body");
            return res;
        }

        User u = userService.findUserByName(user); // 只验证用户名，就不验证身份证号码是否 重复了
        if (u != null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            res.setMsg("用户名重复！");
            return res;
        } else {
            // nickname
            try {
                // 程序代码
                user.setNickname(user.getUsername()); // 由于未传nickname默认username与nickname一致
                user.setPassword(DESUtil.getEncryptString(user.getPassword())); // 密码加密
                userService.addUser(user);
            } catch (Error e1) {
                //Catch 块
                res.setStatus(Result.STATUS_Fail);
                res.setCode(Result.CODE_Fail);
                response.setStatus(500);
                res.setMsg("添加失败，请稍后重试！");
                return res;
            }

            User user1 = userService.findUserByName(user);
            user1.setPassword(null);
            res.setStatus(Result.STATUS_OK);
            res.setCode(Result.CODE_OK);
            res.setData(user1);
            res.setMsg("注册成功！");
            Cookie cookie = null;
            try {
                cookie = new Cookie("username", URLEncoder.encode(user1.getUsername(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            assert cookie != null;
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 7);// 3600 * 24 => 一天 这里测试用的是1分钟
            response.addCookie(cookie);


            int minute = 1000 * life * 60;// 1分钟
            long timestamp = (long) (new Date().getTime() + minute);
            Cookie cookie1 = new Cookie("time", Long.toString(timestamp));
            cookie1.setMaxAge(3600 * 24 * 7);
            response.addCookie(cookie1);

            return res;
        }
    }

    /**
     * 签到
     *
     * @return Result
     */
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ResponseBody
    public Result sign(@RequestBody Sign sign, HttpServletRequest request, HttpServletResponse response) {
        Result res = new Result();
        if (sign == null) {
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            res.setMsg("没有body");
            response.setStatus(500);
            return res;
        }
        try {
            // 程序代码

            // 客户端发来的时间不能比服务器日期大或者小
            if (!(translateDate(sign.getTime()).getTime() == translateDate(new Date()).getTime())) {
                res.setStatus(Result.STATUS_Fail);
                res.setCode(Result.CODE_Fail);
                response.setStatus(500);
                res.setMsg("只能签到今日！");
                return res;
            }

            User user = userService.findUserById(sign.getUserId());
            Sign sign1 = userService.getRecentSignById(user); // sign表中用户最新的一条签到信息
            if (sign1 == null) {
                user.setTodayIsSign(0); // 未签到
            } else {
                if (translateDate(sign1.getTime()).getTime() == translateDate(new Date()).getTime()) { // sign表中用户最新的一条签到信息与服务器同一天，说明用户今日已签到了
                    user.setTodayIsSign(1);
                } else {
                    user.setTodayIsSign(0);
                }
            }


            if (user.getTodayIsSign() == 1) { // 已签到
                res.setStatus(Result.STATUS_Fail);
                res.setCode(Result.CODE_Fail);
                response.setStatus(500);
                res.setMsg("今日已签到！");
                return res;
            } else if (user.getTodayIsSign() == 0) { // 未签到
                userService.sign(sign);
            } else if (user.getTodayIsSign() == null) { // 未签到
                userService.sign(sign);
            }
        } catch (Error e1) {
            //Catch 块
            res.setStatus(Result.STATUS_Fail);
            res.setCode(Result.CODE_Fail);
            response.setStatus(500);
            res.setMsg("签到失败，请稍后重试！");
            return res;
        }
        res.setStatus(Result.STATUS_OK);
        res.setCode(Result.CODE_OK);
        res.setMsg("签到成功！");
        return res;
    }

    /**
     * 退出登录
     *
     * @return Result
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        Result res = new Result();

        Cookie cookie = null;
        cookie = new Cookie("username", "");
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 7);// 3600 * 24 => 一天 这里测试用的是1分钟
        response.addCookie(cookie);

        Cookie cookie1 = new Cookie("time", "");
        cookie1.setPath("/");
        cookie1.setMaxAge(3600 * 24 * 7);
        response.addCookie(cookie1);

        res.setStatus(Result.STATUS_OK);
        res.setCode(Result.CODE_OK);
        res.setMsg("退出登录成功！");
        request.getSession().invalidate(); // 使session失效
        return res;
    }


    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/json")
    public String getJson(Model model) {
        return "json";
    }

    @RequestMapping(value = "/testJson", method = RequestMethod.POST)
    @ResponseBody
    public Registration json(@RequestBody Registration registration) {
        System.out.println(registration);
        Integer id = registration.getId();
        return registrationService.findRegistrationById(id);
    }
}
