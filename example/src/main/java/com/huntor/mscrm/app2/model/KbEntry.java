package com.huntor.mscrm.app2.model;

/**
 * Created by Administrator on 2015/5/7.
 */
public class KbEntry {
    public int id;//知识库条目id
    public String title;//标题
    public String content;//内容

    public int categorieId;//类别ID

    @Override
    public String toString() {
        return "KbEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categorieId=" + categorieId +
                '}';
    }
}
