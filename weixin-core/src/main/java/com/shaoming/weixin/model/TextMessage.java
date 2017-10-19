package com.shaoming.weixin.model;

/**
 * Created by jerry on 17-10-11.
 */
public class TextMessage extends Message{
    private String Content;
    private String MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }
}
