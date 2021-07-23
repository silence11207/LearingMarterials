package com.tanhua.dubbo.server.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 原理:使用Redis的自增长类型, 实现自增长id
 */
@Service
public class IdService {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * 生成自增长id
     * @return
     */
    public Long createId(String type, String strId) {
        type = StringUtils.upperCase(type);

        String idHashKey = "TANHUA_ID_HASH_" + type;
        if (this.redisTemplate.opsForHash().hasKey(idHashKey, strId)) {
            return Long.valueOf(this.redisTemplate.opsForHash().get(idHashKey, strId).toString());
        }

        String idKey = "TANHUA_ID_" + type;
        Long id = this.redisTemplate.opsForValue().increment(idKey);

        this.redisTemplate.opsForHash().put(idHashKey, strId, id.toString());

        return id;
    }
}
