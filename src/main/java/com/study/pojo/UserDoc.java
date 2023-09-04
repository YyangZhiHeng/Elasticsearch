package com.study.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDoc {
    private Integer id;
    private String name;
    private Integer age;
    private String address;

    public  UserDoc(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.address = user.getLongitude()+","+user.getLatitude();
    }
}


