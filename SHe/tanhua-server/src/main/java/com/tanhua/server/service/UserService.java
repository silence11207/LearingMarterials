package com.tanhua.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.server.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${tanhua.sso.url}")
    private String url;

    private static final ObjectMapper MAPPER= new ObjectMapper();
    /**
     * 调用SSO系统的接口服务进行查询
     *
     * @param token
     * @return 如果查询到, 返回User对象, 如果未查询到就返回null
     */
    public User queryUserByToken(String token) {
        String jsonData = this.restTemplate.getForObject(url + "/user/{token}", String.class,token);
        if(StringUtils.isNotEmpty(jsonData)){
            try {
                return MAPPER.readValue(jsonData, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
