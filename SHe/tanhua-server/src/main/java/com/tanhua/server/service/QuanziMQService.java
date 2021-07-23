package com.tanhua.server.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tanhua.dubbo.server.api.QuanZiApi;
import com.tanhua.dubbo.server.pojo.Publish;
import com.tanhua.server.pojo.User;
import com.tanhua.server.utils.UserThreadLocal;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuanziMQService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Reference(version = "1.0.0")
    private QuanZiApi quanZiApi;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuanziMQService.class);

    /**
     * 新增动态消息
     *
     * @param publishId
     */
    public Boolean sendSavePublishMsg(String publishId){
        return this.sendMsg(1, publishId);
    }

    /**
     * 浏览动态消息
     *
     * @param publishId
     */
    public Boolean querySavePublishMsg(String publishId){
        return this.sendMsg(2, publishId);
    }

    /**
     * 点赞动态消息
     *
     * @param publishId
     * @return
     */
    public Boolean likePublishMsg(String publishId) {
        return this.sendMsg(3, publishId);
    }

    /**
     * 取消点赞动态消息
     *
     * @param publishId
     * @return
     */
    public Boolean disLikePublishMsg(String publishId) {
        return this.sendMsg(6, publishId);
    }

    /**
     * 喜欢动态消息
     *
     * @param publishId
     * @return
     */
    public Boolean lovePublishMsg(String publishId) {
        return this.sendMsg(4, publishId);
    }

    /**
     * 取消喜欢动态消息
     *
     * @param publishId
     * @return
     */
    public Boolean disLovePublishMsg(String publishId) {
        return this.sendMsg(7, publishId);
    }

    /**
     * 评论动态消息
     *
     * @param publishId
     * @return
     */
    public Boolean commentPublishMsg(String publishId) {
        return this.sendMsg(5, publishId);
    }
    /**
     * 发送消息
     *
     * @param type  1-发动态，2-浏览动态， 3-点赞， 4-喜欢， 5-评论，6-取消点赞，7-取消喜欢
     * @param publishId
     */
    private Boolean sendMsg(Integer type, String publishId){
        try {
            Publish publish = this.quanZiApi.queryPublishById(publishId);
            User user = UserThreadLocal.get();
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", type);
            msg.put("publishId", publishId);
            msg.put("userId", user.getId());
            msg.put("pid", publish.getPid());
            this.rocketMQTemplate.convertAndSend("tanhua-quanzi", msg);
        } catch (MessagingException e) {
            LOGGER.error("圈子消息发送失效! type = " + type,", publishId = " + publishId, e);
            return false;
        }
        return true;
    }
}
