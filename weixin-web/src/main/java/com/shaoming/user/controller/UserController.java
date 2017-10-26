package com.shaoming.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.shaoming.core.util.HttpRequestMethod;
import com.shaoming.core.util.HttpRequestUtil;
import com.shaoming.core.util.RedisUtil;
import com.shaoming.weixin.model.WeiBoUser;
import com.shaoming.weixin.util.AlipayUtil;
import com.shaoming.weixin.util.WeiXinUtil;
import com.shaoming.user.model.User;
import com.shaoming.user.service.UserService;
import com.shaoming.weixin.model.WeiXinUser;
import com.shaoming.weixin.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

/**
 * Created by jerry on 17-10-11.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

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
     * 跳转到用户信息页面
     */
    @GetMapping(value = "showUserInfo")
    public String showUserInfo(){
        return "/user/user_info";
    }

    /**
     * 微信登录
     */
    @RequestMapping(value = "weiXinLogin")
    public String weiXinLogin(){
        try {
            String url = WeiXinUtil.AUTHORIZE.replace("APPID", WeiXinUtil.APPID).replace("SCOPE", "snsapi_userinfo").replace("REDIRECT_URI", URLEncoder.encode(WeiXinUtil.REDIRECT_URI, "UTF-8"));
            System.out.println("授权URL："+url);
            return "redirect:"+url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 微信登录回调地址
     */
    @RequestMapping(value = "weixinCallBack", method = RequestMethod.GET)
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
        return "redirect:index";
    }

    /**
     * 支付宝登录
     */
    @RequestMapping(value = "alipayLogin")
    public String alipayLogin(){
        // 发起授权请求，获取auth_code
        String url = AlipayUtil.APP_AUTHORIZE.replace("APPID", AlipayUtil.APPID).replace("SCOPE", "auth_user").replace("REDIRECT_URI", AlipayUtil.REDIRECT_URI);
        return "redirect:"+url;
    }

    /**
     * 支付宝登录回调接口
     */
    @RequestMapping(value = "alipayCallBack")
    public String alipayCallBack(String app_id, String source, String scope, String auth_code, HttpSession session){
        // 通过 auth_code 获取 access_token
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayUtil.ALIPAY_URL, AlipayUtil.APPID, AlipayUtil.APP_PRIVATE_KEY, "json", AlipayUtil.CHARSET, AlipayUtil.ALIPAY_PUBLIC_KEY, AlipayUtil.SIGN_TYPE);
        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setCode(auth_code);
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        String accessToken = "";
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(alipaySystemOauthTokenRequest);
            accessToken = oauthTokenResponse.getAccessToken();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 通过 access_token 获取支付宝用户信息
        AlipayUserInfoShareRequest alipayUserInfoShareRequest = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse userinfoShareResponse = null;
        try {
            userinfoShareResponse = alipayClient.execute(alipayUserInfoShareRequest, accessToken);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        session.setAttribute("LOGIN_USER", userinfoShareResponse);
        return "redirect:index";
    }

    /**
     * 微博登录
     */
    @RequestMapping(value = "weiboLogin")
    public String weiboLogin(){
        String url = WeiboUtil.AUTHORIZE_URL.replace("YOUR_CLIENT_ID", WeiboUtil.APP_KEY).replace("YOUR_REGISTERED_REDIRECT_URI", WeiboUtil.REDIRECT_URI);
        return "redirect:"+url;
    }

    /**
     * 微博回调地址
     */
    @RequestMapping(value = "weiboCallBack")
    public String weiboCallBack(String code, HttpSession session){
        String url = WeiboUtil.ACCESS_TOKEN_URL.replace("YOUR_CLIENT_ID", WeiboUtil.APP_KEY).replace("YOUR_CLIENT_SECRET", WeiboUtil.APP_SECRE)
                                               .replace("YOUR_REGISTERED_REDIRECT_URI", WeiboUtil.REDIRECT_URI).replace("CODE", code);
        String jsonStr1 = "";
        try {
            jsonStr1 = HttpRequestUtil.doPost(url, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将json字符串转换为json对象
        JSONObject jo = JSONObject.parseObject(jsonStr1);
        String access_token = jo.getString("access_token");
        String uid = jo.getString("uid");

        String url2 = WeiboUtil.USER_INFO_URL.replace("ACCESS_TOKEN", access_token).replace("UID", uid);
        String jsonStr2 = "";
        try {
            jsonStr2 = HttpRequestUtil.doRequest(url2, null, HttpRequestMethod.GET, "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将json字符串转换为json对象
        JSONObject jo2 = JSONObject.parseObject(jsonStr2);
        try {
            WeiBoUser user = new WeiBoUser(jo2);
            session.setAttribute("LOGIN_USER", user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:index";
    }

}
