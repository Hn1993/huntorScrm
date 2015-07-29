package com.huntor.mscrm.app.model;

/**
 * Created by Administrator on 2015/5/5.
 */
public class VerifyResult extends Response {
    public String token;//用于最终修改密码，32位字符串

    @Override
    public String toString() {
        return "VerifyResult{" +
                "token='" + token + '\'' +
                '}';
    }
}
