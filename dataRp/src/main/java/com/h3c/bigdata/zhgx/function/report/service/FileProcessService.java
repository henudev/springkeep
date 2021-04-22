package com.h3c.bigdata.zhgx.function.report.service;

import com.baomidou.mybatisplus.service.IService;
import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.function.report.config.ResponseModel;
import com.h3c.bigdata.zhgx.function.report.entity.DataAnnexEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import com.h3c.bigdata.zhgx.function.report.model.DelData;
import com.h3c.bigdata.zhgx.function.report.model.QueryData;
import com.h3c.bigdata.zhgx.function.report.model.ReportData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DataProcessService
 * @Description 数据
 * @Author zzzzitai
 * @Date 2018/11/14 12:11
 * @Version 1.0
 **/
public interface FileProcessService {
    /**
     * 上传文件
     * @param file
     * @param httpRequest
     * @return
     */
    ResponseModel uploadFile(MultipartFile file, HttpServletRequest httpRequest) ;

    /**
     * 文件列表
     * @param annexIds
     * @param httpRequest
     * @return
     */
    List<Map> getAnnexList(String annexIds, HttpServletRequest httpRequest);


    /**
     * 下载文件列表
     * @param annexIds
     * @param httpRequest
     * @return
     */
    void downLoadAnnexByGroupId(String annexIds, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException;

    /**
     * 删除文件
     * @param annexIds
     * @return
     */
    String deleteAnnex(String annexIds);

    /**
     * 删除文件
     * @param annexIds
     * @return
     */
    String delUpdate(String annexIds, String number, String dataId, String key, String tableName) throws Exception;


    /**
     * 删除文件
     * @param groupId
     * @param httpRequest
     * @return
     */
    String deleteAnnexByGroupId(String groupId, HttpServletRequest httpRequest);

    /**
     * 根据模板中文名称获取附件列表
     * @param tempalteName
     * @param page
     * @param pageSize
     * @return
     */
    ApiResult getAnnexListByTempalteName(String tempalteName, int page, int pageSize);
}
