package build.dream.thc.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class ConnectionOptionWrapper {
    /**
     * 内部连接参数
     */
    private MqttConnectOptions mqttConnectOptions;

    public ConnectionOptionWrapper(String userName, String password) {
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(5000);
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }
}
