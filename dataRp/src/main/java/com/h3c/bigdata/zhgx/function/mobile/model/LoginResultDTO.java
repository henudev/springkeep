package com.h3c.bigdata.zhgx.function.mobile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class LoginResultDTO {

    private String userName;

    private String name;

    private String status;

    private String id;

    private String avatar;

    private String telephone;

    private String email;

    private String firstLoginFlag;

    private String departmentId;


    private String departmentName;

    //登录后的token
    private String token;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
