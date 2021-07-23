package com.tanhua.server.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.server.api.QuanZiApi;
import com.tanhua.dubbo.server.api.VisitorsApi;
import com.tanhua.dubbo.server.pojo.Movements;
import com.tanhua.dubbo.server.pojo.Publish;
import com.tanhua.dubbo.server.pojo.Visitors;
import com.tanhua.dubbo.server.vo.PageInfo;
import com.tanhua.server.pojo.User;
import com.tanhua.server.pojo.UserInfo;
import com.tanhua.server.utils.RelativeDateFormat;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.server.vo.PageResult;
import com.tanhua.server.vo.PicUploadResult;
import com.tanhua.server.vo.VisitorsVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MovementsService {

    @Reference(version = "1.0.0")
    private QuanZiApi quanZiApi;
    @Reference(version = "1.0.0")
    private VisitorsApi visitorsApi;
    @Autowired
    private PicUploadService picUploadService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 发布动态
     *
     * @param textContent
     * @param location
     * @param longitude
     * @param latitude
     * @param multipartFiles
     * @return
     */
    public String saveMovements(String textContent, String location, String longitude, String latitude, MultipartFile[] multipartFiles) {
        User user = UserThreadLocal.get();
        Publish publish = new Publish();
        publish.setUserId(user.getId());
        publish.setText(textContent);
        publish.setLocationName(location);
        publish.setLatitude(latitude);
        publish.setLongitude(longitude);

        //图片上传
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            PicUploadResult uploadResult = this.picUploadService.upload(file);
            imageUrls.add(uploadResult.getName());
        }
        publish.setMedias(imageUrls);

        return this.quanZiApi.savePublish(publish);
    }

    public PageResult queryPublishList(Integer page, Integer pageSize, boolean isRecommend) {
        PageResult pageResult = new PageResult();
        //获取当前的登录信息
        User user = UserThreadLocal.get();
        PageInfo<Publish> pageInfo = null;
        if (isRecommend) { //推荐动态逻辑处理
            // 查询Redis
            String value = this.redisTemplate.opsForValue().get("QUANZI_PUBLISH_RECOMMEND_" + user.getId());
            if (StringUtils.isNotEmpty(value)) {
                String[] pids = StringUtils.split(value, ',');
                int startIndex = (page - 1) * pageSize;
                if(startIndex < pids.length){
                    int endIndex = startIndex + pageSize - 1;
                     if (endIndex >= pids.length) {
                        endIndex = pids.length - 1;
                    }

                    List<Long> pidList = new ArrayList<>();
                    for (int i = startIndex; i <= endIndex; i++) {
                        pidList.add(Long.valueOf(pids[i]));
                    }

                    List<Publish> publishList = this.quanZiApi.queryPublishByPids(pidList);
                    pageInfo = new PageInfo<>();
                    pageInfo.setRecords(publishList);
                }
            }
        }

        if (null == pageInfo) {
            //默认查询逻辑
            Long userId = isRecommend ? null : user.getId();
            pageInfo = this.quanZiApi.queryPublishList(userId, page, pageSize);
        }

        pageResult.setPagesize(pageSize);
        pageResult.setPage(page);
        pageResult.setCounts(0);
        pageResult.setPages(0);

        List<Publish> records = pageInfo.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            //没有动态信息
            return pageResult;
        }

        List<Movements> movementsList = new ArrayList<>();
        for (Publish record : records) {
            Movements movements = new Movements();

            movements.setId(record.getId().toHexString());
            movements.setImageContent(record.getMedias().toArray(new String[]{}));
            movements.setTextContent(record.getText());
            movements.setUserId(record.getUserId());
            movements.setCreateDate(RelativeDateFormat.format(new Date(record.getCreated())));

            movementsList.add(movements);
        }

        List<Long> userIds = new ArrayList<>();
        for (Movements movements : movementsList) {
            if (!userIds.contains(movements.getUserId())) {
                userIds.add(movements.getUserId());
            }

        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<UserInfo> userInfos = this.userInfoService.queryUserInfoList(queryWrapper);
        for (Movements movements : movementsList) {
            for (UserInfo userInfo : userInfos) {
                if (movements.getUserId().longValue() == userInfo.getUserId().longValue()) {
                    fillValueToMovements(movements, userInfo);
                    break;
                }
            }
        }

        pageResult.setItems(movementsList);
        return pageResult;
    }

    private void fillValueToMovements(Movements movements, UserInfo userInfo) {
        User user = UserThreadLocal.get();
        movements.setAge(userInfo.getAge());
        movements.setAvatar(userInfo.getLogo());
        movements.setGender(userInfo.getSex().name().toLowerCase());
        movements.setNickname(userInfo.getNickName());
        movements.setTags(StringUtils.split(userInfo.getTags(), ','));

        Long commentCount = this.quanZiApi.queryCommentCount(movements.getId(), 2);
        if(null == commentCount){
            movements.setCommentCount(0); //评论数
        }else{
            movements.setCommentCount(commentCount.intValue()); //评论数
        }

        movements.setDistance("1.2公里"); //TODO 距离

        String userKey = "QUANZI_COMMENT_LIKE_USER_" + user.getId() + "_" + movements.getId();
        movements.setHasLiked(this.redisTemplate.hasKey(userKey) ? 1 : 0); //是否点赞（1是，0否）

        String key = "QUANZI_COMMENT_LIKE_" + movements.getId();
        String value = this.redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(value)) {
            movements.setLikeCount(Integer.valueOf(value)); //点赞数
        } else {
            movements.setLikeCount(0);
        }

        String userLoveKey = "QUANZI_COMMENT_LOVE_USER_" + user.getId() + "_" + movements.getId();
        movements.setHasLoved(this.redisTemplate.hasKey(userLoveKey) ? 1 : 0); //是否喜欢（1是，0否）

        key = "QUANZI_COMMENT_LOVE_" + movements.getId();
        value = this.redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(value)) {
            movements.setLoveCount(Integer.valueOf(value)); //喜欢数
        } else {
            movements.setLoveCount(0);
        }
    }

    public Long likeComment(String publishId) {
        User user = UserThreadLocal.get();
        boolean bool = this.quanZiApi.saveLikeComment(user.getId(), publishId);
        if(!bool){
            //保存失败
            return null;
        }
        //保存成功,获取点赞数
        Long likeCount = 0L;
        String likeCommentKey = "QUANZI_COMMENT_LIKE_" + publishId;
        //如果不存在
        if (!this.redisTemplate.hasKey(likeCommentKey)) {
            Long count = this.quanZiApi.queryCommentCount(publishId, 1);
            likeCount = count;
            this.redisTemplate.opsForValue().set(likeCommentKey, String.valueOf(likeCount));
        }else{
            likeCount = this.redisTemplate.opsForValue().increment(likeCommentKey);
        }
        // 记录当前用户已经点赞
        String userKey = "QUANZI_COMMENT_LIKE_USER_" + user.getId() + "_" + publishId;
        this.redisTemplate.opsForValue().set(userKey, "1");

        return likeCount;
    }

    public Long cancelLikeComment(String publishId) {
        User user = UserThreadLocal.get();
        boolean bool = this.quanZiApi.removeComment(user.getId(), publishId, 1);
        if(!bool){
            return null;
        }
        //redis中的点赞数需要减少1
        String likeCommentKey = "QUANZI_COMMENT_LIKE_" + publishId;
        Long count = this.redisTemplate.opsForValue().decrement(likeCommentKey);
        // 记录当前用户已经取消点赞
        String dislikeKey = "QUANZI_COMMENT_LIKE_USER_" + user.getId() + "_" + publishId;
        this.redisTemplate.delete(dislikeKey);
        return count;

    }

    public Long loveComment(String publishId) {
        User user = UserThreadLocal.get();
        boolean bool = this.quanZiApi.saveLoveComment(user.getId(), publishId);
        if (!bool) {
            return null;
        }

        Long loveCount = 0L;

        //保存喜欢数到redis
        String key = "QUANZI_COMMENT_LOVE_" + publishId;
        if (!this.redisTemplate.hasKey(key)) {
            Long count = this.quanZiApi.queryCommentCount(publishId, 3);
            loveCount = count ;
            this.redisTemplate.opsForValue().set(key, String.valueOf(loveCount));
        } else {
            loveCount = this.redisTemplate.opsForValue().increment(key);
        }

        //记录已喜欢
        String userKey = "QUANZI_COMMENT_LOVE_USER_" + user.getId() + "_" + publishId;
        this.redisTemplate.opsForValue().set(userKey, "1");

        return loveCount;
    }

    public Long cancelLoveComment(String publishId) {
        User user = UserThreadLocal.get();
        boolean bool = this.quanZiApi.removeComment(user.getId(), publishId, 3);
        if (bool) {
            String key = "QUANZI_COMMENT_LOVE_" + publishId;
            //数量递减
            Long loveCount = this.redisTemplate.opsForValue().decrement(key);

            //删除已点赞
            String userKey = "QUANZI_COMMENT_LOVE_USER_" + user.getId() + "_" + publishId;
            this.redisTemplate.delete(userKey);

            return loveCount;
        }
        return null;
    }

    public Movements queryMovementsById(String publishId) {
        Publish publish = this.quanZiApi.queryPublishById(publishId);
        if (null == publish) {
            return null;
        }

        Movements movements = new Movements();

        movements.setId(publish.getId().toHexString());
        movements.setImageContent(publish.getMedias().toArray(new String[]{}));
        movements.setTextContent(publish.getText());
        movements.setUserId(publish.getUserId());
        movements.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));

        UserInfo userInfo = this.userInfoService.queryUserInfoById(publish.getUserId());
        if (null == userInfo) {
            return null;
        }
        this.fillValueToMovements(movements, userInfo);

        return movements;
    }

    public List<VisitorsVo> queryVisitorsList() {
        User user =UserThreadLocal.get();
        //如果redis中存在上一次查询的时间点的话就按照时间查询,否则就按照数量查询
        String redisKey = "visitors_" + user.getId();
        String data = this.redisTemplate.opsForValue().get(redisKey);
        List<Visitors> visitors = null;
        if(StringUtils.isEmpty(data)){
            //按照数量查询
            visitors = this.visitorsApi.topVisitor(user.getId(), 6);
        }else{
            //按照时间查询
            visitors = this.visitorsApi.topVisitor(user.getId(), Long.valueOf(data));
        }
        if(CollectionUtils.isEmpty(visitors)){
            return Collections.emptyList();
        }
        List<Long> userIds = new ArrayList<>();
        for (Visitors visitor : visitors) {
            userIds.add(visitor.getVisitorUserId());
        }
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<UserInfo> userInfoList = this.userInfoService.queryUserInfoList(queryWrapper);
        List<VisitorsVo> result = new ArrayList<>();
        for (Visitors visitor : visitors) {
            for (UserInfo userInfo : userInfoList) {
                if(visitor.getVisitorUserId().longValue() == userInfo.getUserId().longValue()){
                    VisitorsVo visitorsVo = new VisitorsVo();
                    visitorsVo.setAge(userInfo.getAge());
                    visitorsVo.setAvatar(userInfo.getLogo());
                    visitorsVo.setGender(userInfo.getSex().name().toLowerCase());
                    visitorsVo.setId(userInfo.getUserId());
                    visitorsVo.setNickname(userInfo.getNickName());
                    visitorsVo.setTags(StringUtils.split(userInfo.getTags(), ','));
                    visitorsVo.setFateValue(visitor.getScore().intValue());
                    result.add(visitorsVo);
                    break;
                }
            }
        }

        return result;
    }

    public PageResult queryAlbumList(Long userId, Integer page, Integer pageSize) {
        PageResult pageResult = new PageResult();
        pageResult.setPage(page);
        pageResult.setPagesize(pageSize);

        PageInfo<Publish> albumPageInfo = this.quanZiApi.queryAlbumList(userId, page, pageSize);
        List<Publish> records = albumPageInfo.getRecords();

        if(CollectionUtils.isEmpty(records)){
            return pageResult;
        }

        List<Movements> movementsList = new ArrayList<>();
        for (Publish record : records) {
            Movements movements = new Movements();

            movements.setId(record.getId().toHexString());
            movements.setImageContent(record.getMedias().toArray(new String[]{}));
            movements.setTextContent(record.getText());
            movements.setUserId(record.getUserId());
            movements.setCreateDate(RelativeDateFormat.format(new Date(record.getCreated())));
            movementsList.add(movements);
        }

        List<Long> userIds = new ArrayList<>();
        for (Movements movements : movementsList) {
            if(!userIds.contains(movements.getUserId())){
                userIds.add(movements.getUserId());
            }
        }
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<UserInfo> userInfos = this.userInfoService.queryUserInfoList(queryWrapper);
        for (Movements movements : movementsList) {
            for (UserInfo userInfo : userInfos) {
                if (movements.getUserId().longValue() == userInfo.getUserId().longValue()) {
                    this.fillValueToMovements(movements, userInfo);
                    break;
                }
            }
        }

        pageResult.setItems(movementsList);

        return pageResult;
    }
}
