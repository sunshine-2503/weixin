package com.shaoming.access.controller;

import com.shaoming.weixin.util.MessageUtil;
import com.shaoming.core.util.Sha1Util;
import com.shaoming.weixin.model.TextMessage;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 微信接入
 *      接入链接： http://sunshine2503.ngrok.cc/weixin/access, 该链接是通过本地映射到公网的
 * Created by jerry on 17-10-11.
 */
@Controller
@RequestMapping("weixin")
public class AccessController {
    private static final String token = "shaoming";

    /**
     * 微信接入
     */
    @ResponseBody
    @GetMapping(value = "access")
    public String weixinAccess(String signature, String timestamp, String nonce, String echostr){
        // 校验
        if (this.checkSignature(signature, timestamp, nonce)){
            return echostr;
        }
        return "";
    }

    /**
     * 检验方法
     */
    public boolean checkSignature(String signature, String timestamp, String nonce){
        /**
         * 排序
         */
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        // 排序后拼装成字符串
        StringBuffer content = new StringBuffer();
        for (String str : arr){
            content.append(str);
        }
        /**
         * 加密
         */
        String temp = Sha1Util.SHA1(content.toString());
        return temp.equals(signature);
    }

    /**
     * 处理消息
     */
    @ResponseBody
    @PostMapping(value = "access")
    public String message(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        try {
            Map<String, String> map = MessageUtil.xmlToMap(request);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)){
                TextMessage textMessage = new TextMessage();
                textMessage.setFromUserName(toUserName);
                textMessage.setToUserName(fromUserName);
                textMessage.setMsgType(msgType);
                textMessage.setCreateTime(new Date().getTime());
                if ("1".equals(content)){
                    textMessage.setContent("你选择的是：课程介绍！");
                    message = MessageUtil.textMessageToXml(textMessage);
                } else if ("2".equals(content)){
                    message = MessageUtil.initNews(fromUserName, toUserName);
                } else if ("3".equals(content)){
                    message = MessageUtil.initMusic(fromUserName, toUserName);
                    System.out.println(message);
                } else if ("?".equals(content) || "？".equals(content)){
                    message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.menuText());
                } else {
                    textMessage.setContent("你发送的消息：" + content);
                    message = MessageUtil.textMessageToXml(textMessage);
                }
            } else if (MessageUtil.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                if (MessageUtil.MESSAGE_EVENT_SUBSCRIBE.equals(eventType)){
                    message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.menuText());
                } else if (MessageUtil.MESSAGE_EVENT_CLICK.equals(eventType)){
                    String eventKey = map.get("EventKey");
                    if ("m1".equals(eventKey)){
                        message = MessageUtil.initNews(fromUserName, toUserName);
                        System.out.println(message);
                    } else if("m3-2".equals(eventKey)){
                        message = MessageUtil.initNews(fromUserName, toUserName);
                    }
                }
            }
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (DocumentException e) {
            e.printStackTrace();
            return "";
        }
    }
}
