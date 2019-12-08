package com.tl.commerce.controller;

import cn.hutool.core.util.StrUtil;
import com.tl.commerce.domain.Resp;
import com.tl.commerce.domain.User;
import com.tl.commerce.service.UserService;
import com.tl.commerce.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user_login")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户登入，验证，生成token
     *
     * @param body
     */
    @PostMapping("/login")
    @ResponseBody
    public Resp Login(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String loginName = (String) body.get("loginname");
        String pwd = (String) body.get("pwd");
        String imageCode = (String) body.get("imageCode");
        if ("null".equals(loginName) || "null".equals(pwd) || "null".equals(imageCode))
            return Resp.fail().code(-1).msg("参数错误");

        User user = userService.getUserByLoginPhone(loginName);
        if (null == user) return Resp.fail().code(-1).msg("用户不存在");
        /**
         * Access to XMLHttpRequest at 'localhost:8089/ser_login/login' from origin 'http://localhost' has been blocked by
         * CORS policy: Cross origin requests are only supported for protocol schemes: http, data, chrome, chrome-extension, https.
         *
         * Credentials
         */
        return _Login(loginName, pwd, imageCode, user, request);

    }

    /**
     * 获取图片验证码
     *
     * @param request
     * @return
     */
    @GetMapping("/image_code")
    @ResponseBody
    public byte[] getImageCode(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        System.out.println("getImage session:" + session.getId());
        try {
            Object[] imageCode = VerifyUtil.createImage();
            session.setAttribute("imageCode", imageCode[0]);
            return (byte[]) imageCode[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 登陆逻辑
     *
     * @param loginName
     * @param Base64pwd
     * @param imageCode 前端图形验证码
     * @param request
     * @return
     */
    public static Resp _Login(String loginName, String Base64pwd, String imageCode, User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("login sessionId :" + session.getId());
        String session_imageCode = (String) session.getAttribute("imageCode");

        if (StrUtil.isBlank(session_imageCode)) {
            return Resp.fail().code(-1).msg("验证码已过期，请刷新");
        }
        if (!imageCode.trim().equals(session_imageCode)) {
            return Resp.fail().code(-1).msg("验证码错误");
        }

        String password = CharCodeUtils.decode(user.getPwd());
        System.out.println("password:" + password);
        if (Base64pwd.equals(password)) {
            /**
             * 生成token
             */
            String token = JwtUtils.geneJsonWebToken(user);
            int user_id=user.getId();
            Map<String,Object> identity=new HashMap<>(3);
            identity.put("token",token);
            identity.put("user_id",user_id);
            return Resp.ok().code(0).data(identity).msg("登陆成功");

        }
        return Resp.fail().code(-1).msg("密码错误");
    }

    /**
     * 用户登入，验证，生成token
     *
     * @param body
     */
    @PostMapping("/register")
    @ResponseBody
    public Resp register(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String loginName = body.get("loginname").toString();
            String pwd = body.get("pwd").toString();
            String mailCode = body.get("mailCode").toString();
            if ("null".equals(loginName) || "null".equals(pwd) || "null".equals(mailCode))
                return Resp.fail().code(-1).msg("参数错误");

            HttpSession session = request.getSession();
            String session_mailCode = (String) session.getAttribute("mailCode");
            System.out.println("null".equals(session_mailCode)+"ffffff"+session_mailCode);
            if (null==session_mailCode) return Resp.fail().code(-1).msg("状态异常");
            if (!mailCode.trim().toLowerCase().equals(session_mailCode.toLowerCase()))
                return Resp.fail().code(-1).msg("验证码错误");

            User user = userService.getUserByLoginPhone(loginName);
            if (null != user) return Resp.fail().code(-1).msg("用户已存在");

//            String useIp = IpUtils.getIpAddr(request);
            String useIp = "127.0.0.1";//测试
            /**
             * INSERT INTO `tl`.`user` (`loginPhone`, `pwd`, `nick`, `avatar`, `ipid`, `remark`, `status`)" +
             */
            User newuser = new User();
            newuser.setLoginname(loginName);
            newuser.setPwd(CharCodeUtils.encode(pwd));
            newuser.setNick(loginName);
            newuser.setStatus(1);
            newuser.setIpid(useIp);
            newuser.setRemark("");
            newuser.setAvatar("http://lintan123.cn/image/login.jpg");

            int save = userService.save(newuser);

            if (save == 1) return Resp.ok().code(0).msg("注册成功");
            return Resp.ok().code(-1).msg("注册失败");


        } catch (NullPointerException e) {
            e.printStackTrace();
            return Resp.fail().code(-1).msg("参数错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Resp.fail().code(-1).msg("参数错误");
        }


    }

    /**
     * 注册发送验证码给用户邮箱
     *
     * @param user_mail
     * @return
     */
    @GetMapping("/get/mailCode")
    @ResponseBody
    public Resp getMailCode(@RequestParam(value = "user_mail", required = true) String user_mail, HttpServletRequest request) {

        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(user_mail);
        if (m.matches()) {
            try {

                String code = VerifyCodeUtil.generateVerifyCode(4);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("【美美搭】美搭网验证码:").append(code).append("（20分钟内有效，如非本人操作，请忽略）");
                SendmailUtil.sendEmil465(user_mail, "欢迎关注美美哒", stringBuilder.toString());
                /**
                 * 存储验证码，后期整合redis 存储在redis 中
                 */
                HttpSession session = request.getSession(true);
                session.setAttribute("mailCode", code);
                session.setMaxInactiveInterval(3600);//一小时
            } catch (Exception e) {
                e.printStackTrace();
                  return Resp.fail().code(-1).msg("邮件发送失败");
            }
            return Resp.ok().code(0).msg("发送成功");
        } else
            return Resp.fail().code(-1).msg("邮箱格式错误");


    }


}
