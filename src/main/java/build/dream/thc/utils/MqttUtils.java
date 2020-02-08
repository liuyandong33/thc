package build.dream.thc.utils;

import build.dream.thc.Application;
import build.dream.thc.beans.WebResponse;
import build.dream.thc.constants.Constants;
import build.dream.thc.domains.MqttInfo;
import org.apache.commons.collections4.MapUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MqttUtils {
    private static MqttClient mqttClient;

    public static MqttInfo obtainMqttInfo() throws IOException {
        MqttInfo mqttInfo = SqliteUtils.findMqttInfo();
        if (Objects.nonNull(mqttInfo)) {
            return mqttInfo;
        }
//        Token token = OAuthUtils.obtainToken(Application.clientId, Application.clientSecret);
        Map<String, String> requestParameters = new HashMap<String, String>();
//        requestParameters.put("access_token", token.getAccessToken());
        requestParameters.put("deviceCode", Application.DEVICE_CODE);
        WebResponse webResponse = WebUtils.doGetWithRequestParameters("http://localhost:9000/device/obtainMqttInfo", requestParameters);
        Map<String, Object> resultMap = JacksonUtils.readValueAsMap(webResponse.getResult(), String.class, Object.class);
        Map<String, Object> data = (Map<String, Object>) resultMap.get("data");

        mqttInfo = new MqttInfo();
        mqttInfo.setInternalEndPoint(MapUtils.getString(data, "internalEndPoint"));
        mqttInfo.setEndPoint(MapUtils.getString(data, "endPoint"));
        mqttInfo.setClientId(MapUtils.getString(data, "clientId"));
        mqttInfo.setUserName(MapUtils.getString(data, "userName"));
        mqttInfo.setPassword(MapUtils.getString(data, "password"));
        mqttInfo.setTopic(MapUtils.getString(data, "topic"));
        SqliteUtils.insertMqttInfo(mqttInfo);
        return mqttInfo;
    }

    public static void mqttConnect() {
        try {
            MqttInfo mqttInfo = obtainMqttInfo();
            String internalEndPoint = mqttInfo.getInternalEndPoint();
            String endPoint = mqttInfo.getEndPoint();
            String clientId = mqttInfo.getClientId();
            String userName = mqttInfo.getUserName();
            String password = mqttInfo.getPassword();
            String topic = mqttInfo.getTopic();

            MemoryPersistence memoryPersistence = new MemoryPersistence();
            mqttClient = new MqttClient("tcp://" + endPoint + ":1883", clientId, memoryPersistence);
            mqttClient.setTimeToWait(5000);
            mqttClient.setCallback(buildMqttCallbackExtended());
            mqttClient.connect(buildMqttConnectOptions(userName, password));
            mqttClient.subscribe(topic, 0);
        } catch (Exception e) {
            e.printStackTrace();
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
                System.out.println("receive msg from topic " + s + " , body is " + new String(mqttMessage.getPayload(), Constants.CHARSET_NAME_GBK));
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
