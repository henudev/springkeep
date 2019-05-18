package com.zfl.ling;

import com.zfl.ling.dao.UserMapper;
import com.zfl.ling.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TestOrder
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/4/2 16:52
 * @Version 1.0
 **/
@Component
@Order(value = 2)
public class TestOrder implements CommandLineRunner {
    @Resource
    private UserMapper userMapper;
    @Override
    public void run(String[] args) {

        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        System.out.println("order2");
    }
}
