package com.tanhua.dubbo.server.api;

import com.tanhua.dubbo.server.pojo.Publish;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuanZi {

    @Autowired
    private  QuanZiApi quanZiApi;
    @Test
    public void testSavePublish(){
        Publish publish = new Publish();
        publish.setUserId(1L);
        publish.setLocationName("西安市");
        publish.setSeeType(1);
        publish.setText("不过如此");
        publish.setMedias(Arrays.asList("https://tanhua11207.oss-cn-shanghai.aliyuncs.com/images/2021/01/03/16096816486076234.jpg"));
        this.quanZiApi.savePublish(publish);


    }
}
