package com.zfl.ling.entity;

import lombok.Data;

/**
 * @ClassName User
 * @Description 测试用户类.
 * @Author zzzzitai
 * @Date 2019/5/15 14:49
 * @Version 1.0
 **/
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
