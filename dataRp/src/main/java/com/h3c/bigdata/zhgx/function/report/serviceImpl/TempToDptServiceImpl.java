package com.h3c.bigdata.zhgx.function.report.serviceImpl;

import com.h3c.bigdata.zhgx.common.web.service.BaseService;
import com.h3c.bigdata.zhgx.function.report.dao.TempToDptMapper;
import com.h3c.bigdata.zhgx.function.report.entity.TempToDpt;
import com.h3c.bigdata.zhgx.function.report.service.TempToDptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempToDptServiceImpl extends BaseService implements TempToDptService {

    @Autowired
    private TempToDptMapper tempToDptMapper;

    TempToDpt entity = null;

    /**
     * 删除部门时，删除模板跟部门的关系
     *
     * @param dptId
     * @return
     */
    @Override
    public int deleteTempToDptByDptId(String dptId) {
        entity = new TempToDpt();
        entity.setDepartmentId(dptId);
        return tempToDptMapper.delete(entity);
    }

    /**
     * 删除模板时，删除模板跟部门的关系
     *
     * @param templateId
     * @return
     */
    @Override
    public int deleteTempToDptByTempId(String templateId) {
        entity = new TempToDpt();
        entity.setTemplateId(templateId);
        return tempToDptMapper.delete(entity);
    }

    @Override
    public int insert(Integer templateId, String departmentId) {
        entity = new TempToDpt();
        entity.setTemplateId(String.valueOf(templateId));
        entity.setDepartmentId(departmentId);
        return tempToDptMapper.insert(entity);
    }

    @Override
    public List<TempToDpt> selectList(Integer templateId) {
        entity = new TempToDpt();
        entity.setTemplateId(String.valueOf(templateId));
        return tempToDptMapper.select(entity);
    }

    @Override
    public int selectCountByDptId(String departmentId) {
        entity = new TempToDpt();
        entity.setDepartmentId(departmentId);
        return tempToDptMapper.selectCount(entity);
    }
}
