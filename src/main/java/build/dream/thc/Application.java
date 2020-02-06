package build.dream.thc;

import build.dream.thc.utils.MqttUtils;

import java.util.UUID;

public class Application {
    public static void main(String[] args) {
        String deviceId = UUID.randomUUID().toString();
        MqttUtils.mqttConnect();
    }
}
