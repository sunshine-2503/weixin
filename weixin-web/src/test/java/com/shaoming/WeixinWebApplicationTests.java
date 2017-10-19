package com.shaoming;

import com.shaoming.weixin.util.WeiXinUtil;
import com.shaoming.weixin.model.Menu;
import com.shaoming.weixin.model.AccessToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by jerry on 17-10-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WeixinWebApplication.class)
public class WeixinWebApplicationTests {
    private final static Logger logger = LoggerFactory.getLogger(WeixinWebApplicationTests.class);

    @Test
    public void getToken(){
        try {
            AccessToken token = WeiXinUtil.getAccessToken();
            logger.error("token : "+token.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createMenu() throws IOException {
        // 获取token
        AccessToken token = WeiXinUtil.getAccessToken();

        // 初始化Menu
        Menu menu = WeiXinUtil.initMenu();

        // 创建Menu
        int n = WeiXinUtil.menuCreate(token.getToken(), menu);
        if (n == 0){
            logger.error("创建成功！");
        } else {
            logger.error("错误码："+n);
        }
    }

}
