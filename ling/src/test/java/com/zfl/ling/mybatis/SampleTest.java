package com.zfl.ling.mybatis;

import com.zfl.ling.dao.UserMapper;
import com.zfl.ling.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName SampleTest
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/5/15 15:24
 * @Version 1.0
 **/

@RunWith(SpringRunner.class)
@SpringBootTest

public class SampleTest {
    @Autowired
    private UserMapper userMapper;
    
    @Test
    public void testSelect(){
        System.out.println("-----selectAll method test-----");
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }
}
