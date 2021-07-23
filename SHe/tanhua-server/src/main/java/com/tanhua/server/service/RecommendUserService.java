package com.tanhua.server.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tanhua.dubbo.server.api.RecommendUserApi;
import com.tanhua.dubbo.server.pojo.RecommendUser;
import com.tanhua.dubbo.server.vo.PageInfo;
import com.tanhua.server.vo.PageResult;
import com.tanhua.server.vo.TodayBest;
import org.springframework.stereotype.Service;

@Service
public class RecommendUserService {

    @Reference(version = "1.0.0")
    private RecommendUserApi recommendUserApi;

    public TodayBest queryTodayBest(Long userId) {
        RecommendUser recommendUser = this.recommendUserApi.queryWithMaxScore(userId);
        if(null==recommendUser){
            return null;
        }
        TodayBest todayBest = new TodayBest();
        double score = Math.floor(recommendUser.getScore());
        todayBest.setFateValue(Double.valueOf(score).longValue());
        todayBest.setId(recommendUser.getUserId());

        return todayBest;
    }

    public PageInfo<RecommendUser> queryRecommendUserList(Long id, Integer page, Integer pagesize) {
        return this.recommendUserApi.queryPageInfo(id, page, pagesize);
    }

    /**
     *
     * @param userId
     * @param toUserId
     * @return
     */
    public double queryScore(Long userId, Long toUserId) {
        return this.recommendUserApi.queryScore(userId, toUserId);
    }
}
