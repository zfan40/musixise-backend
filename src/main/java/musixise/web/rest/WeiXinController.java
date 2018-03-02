package musixise.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import musixise.config.Constants;
import musixise.web.rest.dto.JsTicketDTO;
import musixise.web.rest.dto.OutputDTO;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhaowei on 2018/3/1.
 */
@RestController
@RequestMapping("/wechat")
public class WeiXinController {

    @Value("${spring.social.wechat.clientId}") String weixinAppId;
    @Value("${spring.social.wechat.clientSecret}") String weixinAppSecret;

    private final Logger log = LoggerFactory.getLogger(WeiXinController.class);

    @RequestMapping(value = "/getTicket", method = RequestMethod.POST)
    public ResponseEntity<?> getJsTicket(@RequestParam(value = "url") String url) {

        String accessToken = getAccessToken(weixinAppId, weixinAppSecret);

        if (accessToken != null) {
            String jsTicket = getTicket(accessToken);
            if (jsTicket != null) {
                Map<String, String> ret = sign(jsTicket, url);

                return ResponseEntity.ok(new OutputDTO<>(0, "success", new JsTicketDTO(ret.get("nonceStr"),
                    ret.get("jsapi_ticket"), ret.get("timestamp"), ret.get("url"),
                    ret.get("signature"), weixinAppId)));
            }
        }

        return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_CODE_WECHAT_GET_TICKET_FAIL, "获取TICKET失败"));
    };

    public String getTicket(String accessToken) {

        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, String.format(url, accessToken));
        Response response = oAuthRequest.send();
        String responceBody = response.getBody();
        log.error("getTicket {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") != null) {
            if (JSONPath.eval(result, "$.errcode").toString().equals("0")) {
                return JSONPath.eval(result, "$.ticket").toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getAccessToken(String appId, String appSecret) {

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, String.format(url, appId, appSecret));
        Response response = oAuthRequest.send();
        String responceBody = response.getBody();
        log.error("getAccessToken {}", responceBody);
        Object result = JSON.parse(responceBody);
        if (((JSONObject) result).get("errcode") == null) {
            return JSONPath.eval(result, "$.access_token").toString();
        } else {
            return null;
        }
    }


    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
            "&noncestr=" + nonce_str +
            "&timestamp=" + timestamp +
            "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
