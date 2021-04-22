package com.h3c.bigdata.zhgx.function.report.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.function.report.entity.TableDescriptionEntity;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateEntity;
import com.h3c.bigdata.zhgx.function.report.model.TemplateAddBean;
import com.h3c.bigdata.zhgx.function.report.model.TemplateItemExcelModel;
import com.h3c.bigdata.zhgx.function.report.model.TemplateModel;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TemplateManageService
 * @Description 模板管理.
 * @Author qinzheng
 * @Date 2018/11/16 9:17
 * @Version 1.0
 **/

public interface TemplateManageService {
    
    /**
     * 功能描述: 持久化新增模板<br>
     * 
     * @param 
     * @return: 
     */
    void addTemplate(TemplateAddBean templateBean,String userId)throws Exception;
    
    /**
     * 功能描述: 删除模板<br>
     * 
     * @param 
     * @return: 
     */
   void deleteTemplate(List<Integer> templateIdList)throws Exception;

    /**
     * 
     * @param templateDesName
     * @param templateName
     * @param templateId
     * @return
     */
    void modifyTemplate(String templateDesName, String templateName, int templateId) throws Exception;

    /**
     * 功能描述: 查询模板列表<br>
     * @param page
     * @param keyword
     * @return
     */

    PageResult getTemplateList(Page<TemplateEntity> page, String keyword,
                               String departmentId,String userId,String searchDepId,String isUsed);

    /**
     * 设置模板更新人的名称
     */
  void setTemplateUserName(TemplateModel modelTemp);

   List<TemplateEntity> getTemplateList(String tag, String name, String departmentCode);

    Boolean createTable(TemplateAddBean templateAddBean);

    /**
     * 功能描述: 模板导出
     *
     * @param templateId 模板ID
     * @return:
     */
    void exportToExcel(HttpServletRequest request,Integer templateId, HttpServletResponse response);

	TemplateAddBean queryTempByNumber(String tempNumber,String templateSourceName);

    String getTagName(String tag);

    List<TableDescriptionEntity> queryItemsByNumber(Integer templateId);

	void updateTempAndDescription(TemplateAddBean template,String userId) throws Exception;


    /**
     * @param
     * @return
     * @Description: 检查表字段是否存在
     * @Author: w15112
     * @Date: 2019/8/22 10:49
     */
    Map checkItemExist(String templateSourceName,String tableName, String item);

    /**
     * @param
     * @return
     * @Description: 检查模板/表名内是否存在待写入的具体数值
     */
    Map checkTemOrTabExist(String temOrTabName, String item);

    /**
     * @Description: 批量导入模板字段
     * @Param:
     * @Author: l17503
     * @Date: 2019/9/2 14:13
     */
    List<TemplateItemExcelModel> batchImportField(MultipartFile file) throws Exception;

    /**
     * 批量停用/启用表格是否使用状态
     */
    Map updateTemplateStatus(TemplateAddBean templateAddBean) ;

    /**
     * 功能描述: 检查模板中是否存在数据
     *
     * @param
     * @return:
     */
    Map checkTemplateDataExist(List<Integer> templateIdList)throws Exception;

    /**
     *
     * @param templateId
     * @param updateTime
     * @param dir
     * @param request
     * @param response
     * @throws Exception
     */
    void exportData(Integer templateId, String updateTime, String dir, HttpServletRequest request, HttpServletResponse response) throws Exception;

     /**
      * 是否含有枚举类型包括(pic, other_annex)
      * @param templateId templateId
      * @return 含有个数（0,1,2）
      */
     boolean countAnnex(Integer templateId)  throws Exception;

     /**
      * 根据模板名称获取附件字段列表，用于大屏展示附件
      * @param templateName
      * @return
      * @throws Exception
      */
     List<Map> getAnnexWordByTemplateName(String templateName) throws Exception;

 /**
  * 模板插入
  * @throws Exception
  */
 void move() throws Exception;

 /**
  * 数据插入
  * @throws Exception
  */
     void move2() throws Exception;
 void move3() throws Exception;
}
