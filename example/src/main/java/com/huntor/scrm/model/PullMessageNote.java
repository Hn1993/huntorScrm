package com.huntor.scrm.model;

import java.io.Serializable;

/****
 * 站内信
 */
public class PullMessageNote implements Serializable {
    public int fromUser;
    public int dest;
    public String type;
    public String content;
    public long time;

    @Override
    public String toString() {
        return "PullMessageNote{" +
                "fromUser=" + fromUser +
                ", dest=" + dest +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
