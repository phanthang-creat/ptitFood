package com.server.ptitFood.common.helper;

public class CommonHelper {

    public static String generateOTP(int length) {
        StringBuilder OTP = new StringBuilder();
        for (int i = 0; i < length; i++) {
            OTP.append((int) (Math.random() * 10));
        }
        return OTP.toString();
    }


}
