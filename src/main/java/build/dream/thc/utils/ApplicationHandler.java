package build.dream.thc.utils;

import build.dream.thc.functions.SuppressThrowFunction;
import build.dream.thc.functions.SuppressThrowNoReturnFunction;

/**
 * Created by liuyandong on 2017/3/24.
 */
public class ApplicationHandler {
    public static <T> T callMethodSuppressThrow(SuppressThrowFunction<T> suppressThrowFunction) {
        try {
            return suppressThrowFunction.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void callMethodSuppressThrow(SuppressThrowNoReturnFunction suppressThrowNoReturnFunction) {
        try {
            suppressThrowNoReturnFunction.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}