package com.server.ptitFood.common.helper.encoding;

import com.google.common.hash.Hashing;

public class EncodingHelper {

    public static String hashPassword(String password) {
        return Hashing.sha256().hashString(password, java.nio.charset.StandardCharsets.UTF_8).toString();
    }
}
