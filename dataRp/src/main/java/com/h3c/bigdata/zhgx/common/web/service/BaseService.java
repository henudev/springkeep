package com.h3c.bigdata.zhgx.common.web.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.StringUtil;

import java.util.List;

/**
 * 基础service.
 *
 * @author Mingchao.Ji
 * @version 1.0
 * @date 2018/4/23
 */
public class BaseService {

    /**
     * 设置请求分页数据.
     * @param pageNum 当前记录起始索引
     * @param pageSize 每页显示记录数
     * @param orderBy 排序列
     * @param dir 排序的方向 "desc" 或者 "asc"
     */
    protected void startPage(Integer pageNum, Integer pageSize,String orderBy,String dir){

        if (StringUtil.isNotNull(pageNum) && StringUtil.isNotNull(pageSize)){

            if (StringUtil.isEmpty(orderBy)||StringUtil.isEmpty(dir)) {
                PageHelper.startPage(pageNum, pageSize);
            } else {
                //下划线转驼峰命名.
                orderBy = StringUtil.humpToLine(orderBy) + " " + dir;
                PageHelper.startPage(pageNum,pageSize,orderBy);
            }

        }
    }

    protected void startPageWithoutHumpToLine(Integer pageNum, Integer pageSize,String orderBy,String dir)
    {
        if (StringUtil.isNotNull(pageNum) && StringUtil.isNotNull(pageSize)){
            orderBy = orderBy + " " + dir;
            PageHelper.startPage(pageNum,pageSize,orderBy);
        }
    }

    /**
     * 响应请求分页数据.
     * @param list 分页查询结果.
     * @return 分页查询结果.
     */
    public PageResult getDataList(List<?> list){

        PageResult result = new PageResult();
        result.setData(list);
        result.setTotal(new PageInfo(list).getTotal());
        return result;
    }
}