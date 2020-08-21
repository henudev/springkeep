package com.zfl.ling.controller;

import com.zfl.ling.model.TestParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName Test
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/11/3 16:41
 * @Version 1.0
 **/

@RestController
@RequestMapping("/test")
public class TestController {
    
    @PostMapping("formater")
    public void testFormater(TestParam testParam) {
        return;
    }
    
    @GetMapping("gest")
    public void testHello() {
        System.out.println("Hello");
        return;
    }
}
