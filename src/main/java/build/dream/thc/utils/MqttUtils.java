package build.dream.thc.utils;

import build.dream.thc.mqtt.MqttInfo;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;

public class MqttUtils {
    private static MqttClient mqttClient;

    public static void mqttConnect() {
        try {
            MqttInfo mqttInfo = null;
            String internalEndPoint = mqttInfo.getInternalEndPoint();
            String endPoint = mqttInfo.getEndPoint();
            String clientId = mqttInfo.getClientId();
            String userName = mqttInfo.getUserName();
            String password = mqttInfo.getPassword();
            String topic = mqttInfo.getTopic();
            Date expireTime = mqttInfo.getExpireTime();

            MemoryPersistence memoryPersistence = new MemoryPersistence();
            mqttClient = new MqttClient("tcp://" + endPoint + ":1883", clientId, memoryPersistence);
            mqttClient.setTimeToWait(5000);
            mqttClient.setCallback(buildMqttCallbackExtended());
            mqttClient.connect(buildMqttConnectOptions(userName, password));
            mqttClient.subscribe(topic, 0);
        } catch (Exception e) {
            ThreadUtils.sleepSafe(2000);
            mqttConnect();
        }
    }

    public static MqttCallbackExtended buildMqttCallbackExtended() {
        return new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                System.out.println("connect success");
            }

            @Override
            public void connectionLost(Throwable throwable) {
                ThreadUtils.sleepSafe(2000);
                mqttConnect();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("receive msg from topic " + s + " , body is " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        };
    }

    private static MqttConnectOptions buildMqttConnectOptions(String userName, String password) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(90);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        mqttConnectOptions.setConnectionTimeout(5000);
        return mqttConnectOptions;
    }
}
