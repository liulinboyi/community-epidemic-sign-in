package com.companysign.interceptor;

import com.companysign.po.Result;
import com.companysign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@CrossOrigin
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // 执行完毕，返回前拦截
        System.out.println("Interceptor complete");

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // 在处理过程中，执行拦截
        System.out.println("Interceptor ing");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
//        // 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
//        // 返回false则不执行拦截
//        HttpSession session = request.getSession();
//        //String uri = request.getRequestURI(); // 获取登录的uri，这个是不进行拦截的
//        //if(session.getAttribute("LOGIN_USER")!=null || uri.indexOf("system/login")!=-1) {// 说明登录成功 或者 执行登录功能
//        if (session.getAttribute("LOGIN_USER") != null) {
//            // 登录成功不拦截
//            return true;
//        } else {
//            // 拦截后进入登录页面
//            response.sendRedirect(request.getContextPath() + "/system/login");
//            return false;
//        }

//        ReqBodyParamsRequestWrapper myRequestWrapper = new ReqBodyParamsRequestWrapper((HttpServletRequest) request);
//        String reqBodyStr = myRequestWrapper.getBody(); // 获取body

//        response.setHeader("Access-Control-Allow-Origin", "*"); // 解决跨域
        response.setContentType("application/json; charset=utf-8");

        Long timestamp = null; // 时间戳
//        HttpSession session = request.getSession(); // session是一个结构，存储着用户的信息
//        if (session.getAttribute("user") == null) {
//            session = request.getSession(true);
//        }

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
                        if (value != null) {
                            long value1 = Long.parseLong(value);
                            System.out.println(value1 + "," + new Date().getTime());
                            if (!(value1 > new Date().getTime())) { // 超时
                                username = "";
                                break;
                            }
                        }
                    }
                    if ("username".equals(c.getName())) {
                        String value = null;
                        try {
                            value = URLDecoder.decode(c.getValue(), "UTF-8"); // 通过session，唯一的id获取username
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
            return true;
        }

        Result res = new Result();
        res.setStatus(Result.STATUS_Fail);
        res.setCode(Result.CODE_Fail);
//        res.setMsg("Cookies没有'time'或者'username'字段，已被拦截");
        res.setMsg("未登录，已被拦截");
        response.setStatus(500);
//        writer.print(JSONObject.toJSONString((Map<String, ? extends Object>) res));
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(res);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
//        response.flushBuffer();
        return false;
        // 读取cookies，进行判断，保持登录状态
    }
}