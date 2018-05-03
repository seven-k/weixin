package com.ycl.weixin.weixintoken.controller;


import com.ycl.weixin.weixintoken.util.AccessTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @GetMapping("/token")
    public Map<String, String> getAccessToken() {
        String accessToken = null;
        try {
            accessToken = accessTokenUtil.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("token", accessToken);
    }

}
