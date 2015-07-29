package com.huntor.mscrm.app.model;

/**
 * Created by cao_cgq on 2015/5/6.
 */
public class Qrcode extends Response {

    public String qrPicUrl;//二维码图片地址

    @Override
    public String toString() {
        return "Qrcode{" +
                "qrPicUrl='" + qrPicUrl + '\'' +
                '}';
    }
}
