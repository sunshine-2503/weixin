package com.shaoming.weixin.model;

/**
 * Created by jerry on 17-10-12.
 */
public class MusicMessage extends Message {
    private Music Music;

    public com.shaoming.weixin.model.Music getMusic() {
        return Music;
    }

    public void setMusic(com.shaoming.weixin.model.Music Music) {
        this.Music = Music;
    }
}
