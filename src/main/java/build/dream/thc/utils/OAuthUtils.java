package build.dream.thc.utils;

import build.dream.thc.beans.WebResponse;
import build.dream.thc.domains.Token;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OAuthUtils {
    public static Token oauthToken(String clientId, String clientSecret) throws IOException {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("grant_type", "client_credentials");
        requestParameters.put("client_id", clientId);
        requestParameters.put("client_secret", clientSecret);
        requestParameters.put("scope", "all");
        WebResponse webResponse = WebUtils.doGetWithRequestParameters("", requestParameters);
        Map<String, Object> resultMap = JacksonUtils.readValueAsMap(webResponse.getResult(), String.class, Object.class);
        Token token = new Token();
        token.setAccessToken(MapUtils.getString(resultMap, "access_token"));
        token.setTokenType(MapUtils.getString(resultMap, "token_type"));
        token.setExpiresIn(MapUtils.getLongValue(resultMap, "expires_in"));
        token.setScope(MapUtils.getString(resultMap, "scope"));
        token.setFetchTime(new Date(System.currentTimeMillis()));
        return token;
    }

    public static Token obtainToken(String clientId, String clientSecret) throws IOException {
        Token token = SqliteUtils.findToken();
        if (Objects.isNull(token) || (System.currentTimeMillis() - token.getFetchTime().getTime()) / 1000 < token.getExpiresIn()) {
            SqliteUtils.deleteToken();
            token = oauthToken(clientId, clientSecret);
            SqliteUtils.insertToken(token);
        }
        return token;
    }
}
