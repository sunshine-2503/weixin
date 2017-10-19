package com.shaoming.weixin.util;

import com.shaoming.weixin.model.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 17-10-11.
 */
public class MessageUtil {
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_EVENT_LOCATION = "LOCATION";
    public static final String MESSAGE_EVENT_CLICK = "CLICK";
    public static final String MESSAGE_EVENT_VIEW = "VIEW";

    /**
     * xml转map集合
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream in = request.getInputStream();
        Document doc = reader.read(in);
        Element root = doc.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements){
            map.put(element.getName(), element.getText());
        }
        in.close();
        return map;
    }

    /**
     * textMessage转xml
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    /**
     * 拼接文本消息
     */
    public static String initText(String toUserName, String fromUserName, String content){
        TextMessage textMessage = new TextMessage();
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
        textMessage.setContent(content);
        textMessage.setCreateTime(new Date().getTime());
        return textMessageToXml(textMessage);
    }

    /**
     * 主菜单
     */
    public static String menuText(){
        StringBuffer str = new StringBuffer();
        str.append("欢迎您的关注！\n请按照菜单提示操作：\n\n");
        str.append("1、课程介绍！\n");
        str.append("2、慕课网介绍！\n\n");
        str.append("回复？调出此菜单。");
        return str.toString();
    }

    /**
     * 图文消息转xml
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", newsMessage.getClass());
        xStream.alias("item", News.class);
        return xStream.toXML(newsMessage);
    }

    /**
     * 拼接图文消息
     */
    public static String initNews(String toUserName, String fromUserName){
        News news1 = new News();
        news1.setTitle("慕课网介绍");
        news1.setPicUrl("http://sunshine2503.free.ngrok.cc/images/imooc.jpg");
        news1.setUrl("http://www.imooc.com");
        news1.setDescription("慕课网官网！");
        News news2 = new News();
        news2.setTitle("慕课网介绍");
        news2.setPicUrl("http://sunshine2503.free.ngrok.cc/images/imooc.jpg");
        news2.setUrl("http://www.imooc.com");
        news2.setDescription("慕课网官网！");
        News news3 = new News();
        news3.setTitle("慕课网介绍");
        news3.setPicUrl("http://sunshine2503.free.ngrok.cc/images/imooc.jpg");
        news3.setUrl("http://www.imooc.com");
        news3.setDescription("慕课网官网！");
        News news4 = new News();
        news4.setTitle("慕课网介绍");
        news4.setPicUrl("http://sunshine2503.free.ngrok.cc/images/imooc.jpg");
        news4.setUrl("http://www.imooc.com");
        news4.setDescription("慕课网官网！");

        NewsMessage newsMessage = new NewsMessage();
        newsMessage.setToUserName(toUserName);
        newsMessage.setFromUserName(fromUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticleCount(4);
        newsMessage.setArticles(new News[]{news1, news2, news3, news4});
        return newsMessageToXml(newsMessage);
    }

    /**
     * 音乐消息转xml
     */
    public static String musicMessageToXml(MusicMessage musicMessage){
        XStream xStream = new XStream();
        xStream.alias("xml", musicMessage.getClass());
        return xStream.toXML(musicMessage);
    }

    /**
     * 拼接音乐消息
     */
    public static String initMusic(String toUserName, String fromUserName){
        Music music = new Music();
        music.setTitle("如果我变成回忆-Tank");
        music.setHQMusicUrl("http://sunshine2503.free.ngrok.cc/music/如果我变成回忆-Tank.mp3");
        music.setMusicUrl("http://sunshine2503.free.ngrok.cc/music/如果我变成回忆-Tank.mp3");
        music.setThumbMediaId("");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setToUserName(toUserName);
        musicMessage.setFromUserName(fromUserName);
        musicMessage.setCreateTime(new Date().getTime());
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setMusic(music);
        return musicMessageToXml(musicMessage);
    }
}
