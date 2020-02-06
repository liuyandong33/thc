package build.dream.thc.utils;

import build.dream.thc.constants.Constants;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class UrlUtils {
    public static String decode(String encryptedString) {
        return decode(encryptedString, Constants.CHARSET_NAME_UTF_8);
    }

    public static String decode(String encryptedString, String charsetName) {
        try {
            return URLDecoder.decode(encryptedString, charsetName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String originalString) {
        return encode(originalString, Constants.CHARSET_NAME_UTF_8);
    }

    public static String encode(String originalString, String charsetName) {
        try {
            return URLEncoder.encode(originalString, charsetName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String originalString, Charset charset) {
        try {
            return URLEncoder.encode(originalString, charset.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
