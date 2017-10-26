package com.shaoming.weixin.util;

/**
 * 新浪微博工具类
 * Created by jerry on 17-10-24.
 */
public class WeiboUtil {

    public static final String APP_KEY= "2761030161";
    public static final String APP_SECRE="cf1552767e17ba5bcfd9200308efebb6";

    // 授权url地址
    public static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI";
    // 获取ACCESS_TOKEN地址
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE";
    // 获取用户信息地址
    public static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json?access_token=ACCESS_TOKEN&uid=UID";
    // 授权回调地址
    public static final String REDIRECT_URI = "http://shaoming.free.ngrok.cc/user/weiboCallBack";

}
