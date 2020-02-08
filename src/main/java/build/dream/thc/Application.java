package build.dream.thc;

import build.dream.thc.beans.WebResponse;
import build.dream.thc.utils.JacksonUtils;
import build.dream.thc.utils.MqttUtils;
import build.dream.thc.utils.SqliteUtils;
import build.dream.thc.utils.WebUtils;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static final String DEVICE_CODE = "0000";
    public static String deviceId;
    public static String clientId;
    public static String clientSecret;

    public static void main(String[] args) throws IOException {
        SqliteUtils.execute("DROP TABLE IF EXISTS mqtt_info");
        SqliteUtils.execute("CREATE TABLE mqtt_info(id INTEGER PRIMARY KEY AUTOINCREMENT, internal_end_point TEXT, end_point TEXT, client_id TEXT, user_name TEXT, password TEXT, topic TEXT)");
        Map<String, Object> deviceInfo = obtainDeviceInfo();

        deviceId = MapUtils.getString(deviceInfo, "deviceId");
        clientId = MapUtils.getString(deviceInfo, "clientId");
        clientSecret = MapUtils.getString(deviceInfo, "clientSecret");
        MqttUtils.mqttConnect();
    }

    public static Map<String, Object> obtainDeviceInfo() throws IOException {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("deviceCode", DEVICE_CODE);

        WebResponse webResponse = WebUtils.doPostWithRequestParameters("http://localhost:9000/client/obtainDeviceInfo", requestParameters);
        Map<String, Object> resultMap = JacksonUtils.readValueAsMap(webResponse.getResult(), String.class, Object.class);
        return (Map<String, Object>) resultMap.get("data");
    }
}
