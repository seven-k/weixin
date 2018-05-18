package com.ycl.weixin.weixintoken.util;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ycl.weixin.weixinnet.util.WeixinNetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AccessTokenUtil {

    private Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.appsecret}")
    private String appsecret;

    @Autowired
    private WeixinNetUtil weixinNetUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appsecret;

    private String accessToken;

    public String getAccessToken() throws Exception {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        accessToken = valueOperations.get(appId + "_access_token");
        if (StringUtils.isEmpty(accessToken)) {
            Map<String, Object> resultMap = getAccessTokenFromService();
            String result = resultMap.get("result").toString();
            switch (result) {
                case "success":
                    accessToken = resultMap.get("access_token").toString();
                    break;
                case "fail":
                    throw new Exception("getAccessTokenFromService Fail");
                case "error":
                    throw new Exception("getAccessTokenFromService Error");
            }
        }
        return accessToken;
    }

    private Map<String, Object> getAccessTokenFromService() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        logger.info("getAccessTokenFromService start...");
        Map<String, Object> resultMap = Maps.newHashMap();
        String result = weixinNetUtil.get(GET_ACCESS_TOKEN_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.containsKey("access_token")) {
            accessToken = jsonObject.getString("access_token");
            resultMap.put("access_token", accessToken);
            resultMap.put("expires_in", jsonObject.getString("expires_in"));
            resultMap.put("result", "success");
            valueOperations.set(appId + "_access_token", accessToken, 7100, TimeUnit.SECONDS);
        } else if (jsonObject.containsKey("errcode")) {
            resultMap.put("errcode", jsonObject.getString("errcode"));
            resultMap.put("errmsg", jsonObject.getString("errmsg"));
            resultMap.put("result", "fail");
        } else
            resultMap.put("result", "error");
        return resultMap;
    }

    public static void main(String[] args) {
        String a="23";
        StringBuilder sb=new StringBuilder(a);
        StringBuilder sb2=new StringBuilder(12);
        System.out.println(sb);
        System.out.println(sb2);
    }
}
