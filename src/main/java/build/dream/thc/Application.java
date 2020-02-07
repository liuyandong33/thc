package build.dream.thc;

import build.dream.thc.beans.WebResponse;
import build.dream.thc.utils.JacksonUtils;
import build.dream.thc.utils.MqttUtils;
import build.dream.thc.utils.WebUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {
    public static void main(String[] args) throws IOException {
        String deviceId = UUID.randomUUID().toString();
        Map<String, Object> deviceInfo = obtainDeviceInfo(deviceId);
        MqttUtils.mqttConnect();
    }

    public static Map<String, Object> obtainDeviceInfo(String deviceId) throws IOException {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("deviceId", deviceId);

        WebResponse webResponse = WebUtils.doPostWithRequestParameters("", requestParameters);
        return JacksonUtils.readValueAsMap(webResponse.getResult(), String.class, Object.class);
    }
}
