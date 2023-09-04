package com.study;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.study.pojo.User;
import com.study.service.EsStudyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EasyExcelTest {
    @Autowired
    private EsStudyService esStudyService;

    @Test
    void testExcelUser(){
        String path = "excel\\user.xls";
        List<User> userList = esStudyService.list();
        ExcelWriter excelWriter = EasyExcel.write(path, User.class).build();
        WriteSheet sheet = EasyExcel.writerSheet("test").build();
        excelWriter.write(userList,sheet);
        excelWriter.finish();
        System.out.println("导出成功");
    }
}
