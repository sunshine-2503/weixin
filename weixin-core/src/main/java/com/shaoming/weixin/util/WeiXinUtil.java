package com.shaoming.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shaoming.core.util.HttpRequestMethod;
import com.shaoming.core.util.HttpRequestUtil;
import com.shaoming.weixin.model.Button;
import com.shaoming.weixin.model.ClickButton;
import com.shaoming.weixin.model.Menu;
import com.shaoming.weixin.model.ViewButton;
import com.shaoming.weixin.model.AccessToken;

import java.io.IOException;

/**
 * Created by jerry on 17-10-11.
 */
public class WeiXinUtil {

    public static final String APPID = "wx9b13c90b1af7e3ef";
    public static final String APPSECRET = "0f6cbac318c02e1a5dd9233523face0d";

    // 获取 ACCESS_TOKEN 链接
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 自定义菜单创建
    public static final String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 用户授权链接
    public static final String AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    // 获取授权的access_token
    public static final String AUTH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户信息
    public static final String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 获取 ACCESS_TOKEN
     */
    public static AccessToken getAccessToken() throws IOException {
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        String jsonStr = HttpRequestUtil.doRequest(url, null, HttpRequestMethod.GET, "UTF-8", false);
        // 将json字符串转换为json对象
        JSONObject jo = JSONObject.parseObject(jsonStr);
        token.setToken(jo.getString("access_token"));
        token.setExpiresIn(jo.getString("expires_in"));
        return token;
    }

    /**
     * 初始化Menu
     */
    public static Menu initMenu(){
        ClickButton button1 = new ClickButton();
        button1.setType("click");
        button1.setName("今日歌曲");
        button1.setKey("m1");

        ClickButton button2_1 = new ClickButton();
        button2_1.setType("scancode_waitmsg");
        button2_1.setName("扫码带提示");
        button2_1.setKey("m2-1");

        ClickButton button2_2 = new ClickButton();
        button2_2.setType("scancode_push");
        button2_2.setName("扫码推事件");
        button2_2.setKey("m2-2");

        Button button2 = new Button();
        button2.setName("扫码");
        button2.setSub_button(new Button[]{button2_1, button2_2});

        ViewButton button3_1 = new ViewButton();
        button3_1.setType("view");
        button3_1.setName("搜一搜");
        button3_1.setUrl("http://www.baidu.com");

        ClickButton button3_2 = new ClickButton();
        button3_2.setType("click");
        button3_2.setName("每日一笑");
        button3_2.setKey("m3-2");

        ViewButton button3_3 = new ViewButton();
        button3_3.setType("view");
        button3_3.setName("弘承持信商城");
        button3_3.setUrl("http://sunshine2503.free.ngrok.cc/user/index");

        Button button3 = new Button();
        button3.setName("菜单");
        button3.setSub_button(new Button[]{button3_1, button3_2, button3_3});

        Menu menu = new Menu();
        menu.setButton(new Button[]{button1, button2, button3});
        return menu;
    }

    /**
     * 创建菜单
     */
    public static int menuCreate(String token, Menu menu) throws IOException {
        String url = MENU_CREATE.replace("ACCESS_TOKEN", token);
        String content = JSON.toJSONString(menu);
        String jsonStr = HttpRequestUtil.doPost(url, content);
        // 将json字符串转换为json对象
        JSONObject jo = JSONObject.parseObject(jsonStr);
        return jo.getInteger("errcode");
    }
}
