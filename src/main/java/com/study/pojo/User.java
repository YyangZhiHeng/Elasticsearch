package com.study.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class User {
    @ExcelProperty(index = 0,value = "id")
    private Integer id;
    @ExcelProperty(index = 1,value = "name")
    private String name;
    @ExcelProperty(index = 2,value = "age")
    private Integer age;
    @ExcelProperty(index = 3,value = "经度")
    private String longitude;
    @ExcelProperty(index = 4,value = "纬度")
    private String latitude;
}
