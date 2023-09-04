package com.study;

import com.study.pojo.User;
import com.study.service.EsStudyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EslaticsearchApplicationTests {

//    记得加@Autowired 注解，不然查询结果为null
   @Autowired
    private EsStudyService esStudyService;

    @Test
    void contextLoads() {
        System.out.println("123");
    }

    @Test
    void TestConnection(){
        List<User> list = esStudyService.list();
        for (User esStudy : list) {
            System.out.println(esStudy);
        }
    }

}
