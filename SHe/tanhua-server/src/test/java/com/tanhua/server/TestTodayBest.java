package com.tanhua.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.server.pojo.UserInfo;
import com.tanhua.server.service.TodayBestService;
import com.tanhua.server.service.UserInfoService;
import com.tanhua.server.vo.PageResult;
import com.tanhua.server.vo.RecommendUserQueryParam;
import com.tanhua.server.vo.TodayBest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTodayBest {
    @Autowired
    private TodayBestService todayBestService;

    @Autowired
    private UserInfoService userInfoService ;

    @Test
    public void testQueryTodayBest(){
        String token="eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxODYyOTM1NjA5MyIsImlkIjo2MX0.zjbi0s2HXds0QDEhduCUlPPWYl0AXOMwBtakoiPkE7g";
        TodayBest todayBest = this.todayBestService.queryTodayBest(token);
        System.out.println(todayBest);
    }
    @Test
    public void testQueryTodayBestList(){
        PageResult pageResult = this.todayBestService.queryRecommendUserList(new RecommendUserQueryParam());
        System.out.println(pageResult);
    }

    @Test
    public void testQueryUserInfo() {
        QueryWrapper<Long> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.in("user_id" , Arrays.asList(13 , 45)) ;
        List<UserInfo> userInfos = userInfoService.queryUserInfoList(queryWrapper);
        userInfos.forEach( userInfo -> System.out.println(userInfo));
    }
}
