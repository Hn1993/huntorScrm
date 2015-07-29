package com.huntor.mscrm.app.model;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/6 0006
 * 18:07.
 */

import java.util.List;

/**
 * 知识库二级列表
 */
public class Category {
    private String code;
    private String msg;
    private List<Entry> list;

    public class Entry{
        private int id;
        private String title;
        private String content;

    }
}
