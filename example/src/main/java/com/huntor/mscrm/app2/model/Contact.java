package com.huntor.mscrm.app2.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by IDEA
 * User : SL
 * on 2015/5/14 0014
 * 18:16.
 */
public class Contact {
    public int numberUnread = 0;
    public int fan_id;
    public String imgHead;
    public String name;
    public String lastWord;
    public long lastTime;
    public String type;
    /**
     * 所在固定分组   2普通 3高潜 4已购
     */
    public int grade;


    @Override
    public String toString() {
        return "Contact:{" +
                ",fan_id = " + fan_id +
                ",imgHead = " + imgHead +
                ",name = " + name +
                ",type = " + type +
                ",lastWord = " + lastWord +
                ",lastTime = " + lastTime +
                ",grade = " + grade +
                "}";
    }

}
