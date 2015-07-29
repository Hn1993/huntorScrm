package com.huntor.mscrm.app.model;

import java.util.List;

/**
 * Created by cao on 2015/5/29.
 */
public class BatchQueryFansResult extends Response{

    public List<Fans> fans;

    public class Fans{
        public int id;//粉丝ID
        public String nickName;//粉丝昵称
        public String avatar;//粉丝头像
        public int group;//所在固定分组   2普通 3高潜 4已购
        public int empId;//与其关联的导购id
        public boolean followStatus;//关注状态
        public String errMsg;//错误描述   只有当查询出错时，才有此字段  出错的粉丝信息只包含id和errMsg

        @Override
        public String toString() {
            return "BatchQueryFansResult{" +
                    "id=" + id +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", group=" + group +
                    ", empId=" + empId +
                    ", followStatus=" + followStatus +
                    ", errMsg='" + errMsg + '\'' +
                    '}';
        }
    }

}
