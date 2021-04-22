package com.h3c.bigdata.zhgx.common.utils;

import com.h3c.bigdata.zhgx.function.report.dao.TemplateEntityMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 研发版本迁移测试类.
 * @author
 */
@Component
public class ToMoveTest {

    @Resource
    private TemplateEntityMapper templateEntityMapper;

    /*** 第一，获取所有模板信息，必要信息*/
    public List<Map<String, String>> getYfTemplateWords(){
        List<Map<String, String>> result = templateEntityMapper.getYfTempalteWords();
        return result;
    }
    /*** 获取模板的字段信息*/
    public List<Map<String, String>> getYfTemplateDes(){
        List<Map<String, String>> result = templateEntityMapper.getYfTempalteDes();
        return result;
    }
}
