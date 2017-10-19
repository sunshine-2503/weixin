package com.shaoming.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.shaoming.core.util.HttpRequestMethod;
import com.shaoming.core.util.HttpRequestUtil;
import com.shaoming.core.util.RedisUtil;
import com.shaoming.weixin.util.WeiXinUtil;
import com.shaoming.user.model.User;
import com.shaoming.user.service.UserService;
import com.shaoming.weixin.model.WeiXinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by jerry on 17-10-11.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @ResponseBody
    @GetMapping("queryAllUsers")
    public List<User> queryAllUsers(){
        return userService.queryAllUsers();
    }

    /**
     * 跳转到首页
     */
    @RequestMapping(value = "index")
    public String showPhoto(){
        return "index";
    }

    /**
     * 微信登录
     */
    @GetMapping("login")
    public String login(){
        try {
            String callBack = "http://sunshine2503.free.ngrok.cc/user/callBack";
            String url = WeiXinUtil.AUTHORIZE.replace("APPID", WeiXinUtil.APPID).replace("SCOPE", "snsapi_userinfo").replace("REDIRECT_URI", URLEncoder.encode(callBack, "UTF-8"));
            System.out.println(url);
            return "redirect:"+url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "redirect:index";
        }
    }

    /**
     * 微信登录回调地址
     */
    @RequestMapping(value = "callBack", method = RequestMethod.GET)
    public String callBack(@RequestParam(name = "code") String code, HttpSession session){
        String url = WeiXinUtil.AUTH_TOKEN.replace("APPID", WeiXinUtil.APPID).replace("SECRET", WeiXinUtil.APPSECRET).replace("CODE", code);
        String jsonStr = null;
        try {
            jsonStr = HttpRequestUtil.doRequest(url, null, HttpRequestMethod.GET, "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将json字符串转换为json对象
        JSONObject jo = JSONObject.parseObject(jsonStr);
        String token = jo.getString("access_token");
        String openId = jo.getString("openid");
        // 获取用户信息
        String url2 = WeiXinUtil.USER_INFO.replace("ACCESS_TOKEN", token).replace("OPENID", openId);
        String jsonStr2 = "";
        try {
            jsonStr2 = HttpRequestUtil.doRequest(url2, null, HttpRequestMethod.GET, "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将json字符串转换为json对象
        JSONObject jo2 = JSONObject.parseObject(jsonStr2);
        WeiXinUser user = new WeiXinUser();
        user.setOpenid(jo2.getString("openid"));
        user.setNickname(jo2.getString("nickname"));
        user.setSex(jo2.getInteger("sex"));
        user.setProvince(jo2.getString("province"));
        user.setCity(jo2.getString("city"));
        user.setCountry(jo2.getString("country"));
//        String headImgUrl = jo2.getString("headimgurl");
//        headImgUrl = headImgUrl.substring(0, headImgUrl.lastIndexOf("/0"))+"/46";
        user.setHeadimgurl(jo2.getString("headimgurl"));
        user.setPrivilege(jo2.getString("privilege"));
        user.setUnionid(jo2.getString("unionid"));
        session.setAttribute("LOGIN_USER", user);
        System.out.println(jsonStr2);
        return "index";
    }

}
