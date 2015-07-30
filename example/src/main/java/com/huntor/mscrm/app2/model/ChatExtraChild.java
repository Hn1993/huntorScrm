package com.huntor.mscrm.app2.model;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/7 0007
 * 14:49.
 */
public class ChatExtraChild {
    private int id;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatExtraTwo{" +
                "id=" + id +
                ", title=" + title +
                ", content=" + content +
                "}";
    }
}
