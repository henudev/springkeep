package com.h3c.bigdata.zhgx.function.report.serviceImpl;

import com.h3c.bigdata.zhgx.common.persistence.ApiResult;
import com.h3c.bigdata.zhgx.common.utils.PageResult;
import com.h3c.bigdata.zhgx.common.utils.UUIDUtil;
import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.report.dao.TableDescriptionEntityMapper;
import com.h3c.bigdata.zhgx.function.report.dao.TemplateSourceEntityMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TemplateSourceEntity;
import com.h3c.bigdata.zhgx.function.report.service.TemplateSourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 模板数据源管理实现类
 * @date 2019-10-11 17:15:39
 */
@Service
@Transactional
public class TemplateSourceServiceImpl extends BaseService implements TemplateSourceService {

    @Resource
    TableDescriptionEntityMapper tableDescriptionEntityMapper;

    @Resource
    private TemplateSourceEntityMapper templateSourceEntityMapper;

    /**
     * 统计数据库是否存在
     * @param dataBaseName
     * @return
     */
    @Override
    public int countDataBase(String dataBaseName) {
        int count = templateSourceEntityMapper.countDataBase(dataBaseName);
        return count;
    }

    /**
     * 创建数据库
     * @param dataBaseName
     * @return
     */
    @Override
    public int createDataBase(String dataBaseName) {
        String createDataBaseSql = " create database "+" `"+dataBaseName+"`";
        int count = tableDescriptionEntityMapper.execSql(createDataBaseSql);
        return count;
    }

    /**
     * 删除数据库
     * @param dataBaseName
     * @return
     */
    public int dropDataBase(String dataBaseName){
        String dropDataBaseSql = "drop database "+" `"+dataBaseName+"`";
        int count = tableDescriptionEntityMapper.execSql(dropDataBaseSql);
        return count;
    }

    /**
     * 新建模板数据源的时候确认该英文名及中文名是否可以使用
     * @param templateSourceEntity
     * @return
     */
    @Override
    public ApiResult countTemplateSource(TemplateSourceEntity templateSourceEntity) {
        int dataBaseCount = countDataBase(templateSourceEntity.getSourceNameEN());
        List<TemplateSourceEntity> templateSourceCount = templateSourceEntityMapper.selectTemplateByCNname(templateSourceEntity);
        if(dataBaseCount>0){
            return ApiResult.success("该数据库已存在！",false);
        }else if(templateSourceCount.size()>0){
            return ApiResult.success("该数据源中文名已存在！",false);
        }
        return ApiResult.success("该数据源的中文或英文名可以使用。",true);
    }

    /**
     * 根据id删除数据源
     * @param sourceId
     * @return
     */
    @Override
    public ApiResult deleteTemplateSouece(String sourceId) {
        try{
            TemplateSourceEntity templateSourceEntity = templateSourceEntityMapper.selectTemplateById(sourceId);
            if(templateSourceEntity == null){
                return ApiResult.fail("未查到该条数据源记录");
            }
            int dataBaseCount = countDataBase(templateSourceEntity.getSourceNameEN());
            if(dataBaseCount>0){
                //查询数据库里是否存在表
                int countTables = templateSourceEntityMapper.coutTablesByDataBaseName(templateSourceEntity.getSourceNameEN());
                if(countTables>0){
                    return ApiResult.fail("该数据源下存在数据表，请先删除数据表！");
                }
                //如果存在数据库，将数据库删除
                dropDataBase(templateSourceEntity.getSourceNameEN());
            }
            //删除表中的数据
            templateSourceEntityMapper.deleteSourceById(sourceId);
            return ApiResult.success("数据源删除成功",sourceId);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.fail("删除数据源时出现异常，请联系系统管理员！");
        }
    }


    /**
     * 更新模板数据源
     * @param templateSourceEntity
     * @return
     */
    @Override
    public ApiResult updateTemplateSource(TemplateSourceEntity templateSourceEntity) {
        try{
            templateSourceEntity.setUpdateTime(new Date());
            templateSourceEntityMapper.updateTemplateSource(templateSourceEntity);
            return ApiResult.success("模板数据源更新成功");

        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.fail("模板数据源更新失败!");
        }
    }

    /**
     * 查询模板数据源列表
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @param dir
     * @param templateSourceEntity
     * @return
     */
    @Override
    public ApiResult queryTemplateSourceList(int pageNum, int pageSize,String orderBy,String dir, TemplateSourceEntity templateSourceEntity) {
        try{
            startPage(pageNum,pageSize,orderBy,dir);
            List<TemplateSourceEntity> templateSourceEntities = templateSourceEntityMapper.selectTemplateList(templateSourceEntity.getSourceNameCN(),templateSourceEntity.getSourceNameEN());
            PageResult result = getDataList(templateSourceEntities);
            return ApiResult.success("查询模板数据源成功",result);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.fail("查询模板数据源失败，请联系管理员！");
        }
    }

    /**
     * 插入数据源信息
     * @param templateSourceEntity
     * @return
     */
    public ApiResult insertTemplateSource(TemplateSourceEntity templateSourceEntity){
        try{
            int dataBaseCount = countDataBase(templateSourceEntity.getSourceNameEN());
            List<TemplateSourceEntity> templateSourceCount = templateSourceEntityMapper.selectTemplateByCNname(templateSourceEntity);
            if(dataBaseCount>0){
                return ApiResult.fail("该数据库已存在！");
            }else if(templateSourceCount.size()>0){
                return ApiResult.fail("该数据源中文名已存在已存在！");
            }
            templateSourceEntity.setId(UUIDUtil.absNumUUID());
            templateSourceEntity.setUpdateTime(new Date());
            //执行创建数据库语句
            createDataBase(templateSourceEntity.getSourceNameEN());
            //向数据库里插入一条数据源记录
            templateSourceEntityMapper.insertTemplateSource(templateSourceEntity);
            return ApiResult.success("数据源添加成功",templateSourceEntity);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.fail("数据源添加失败，请联系管理员！");
        }
    }


}
