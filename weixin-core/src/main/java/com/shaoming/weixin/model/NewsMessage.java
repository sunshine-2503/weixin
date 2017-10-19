package com.shaoming.weixin.model;

/**
 * Created by jerry on 17-10-12.
 */
public class NewsMessage extends Message {
    private Integer ArticleCount; //图文消息数量
    private News[] Articles; // 图文消息数组

    public Integer getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(Integer ArticleCount) {
        this.ArticleCount = ArticleCount;
    }

    public News[] getArticles() {
        return Articles;
    }

    public void setArticles(News[] Articles) {
        this.Articles = Articles;
    }
}
