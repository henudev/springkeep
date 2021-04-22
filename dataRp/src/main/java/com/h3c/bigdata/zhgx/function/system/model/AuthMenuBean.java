package com.h3c.bigdata.zhgx.function.system.model;

import com.h3c.bigdata.zhgx.function.system.entity.AuthMenuInfoEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单操作Bean.
 * @Author J16898
 * @Date 2018/8/27
 * @Version 1.0
 */
public class AuthMenuBean implements Serializable {

    private List<AuthMenuInfoEntity> pageList;

    private AuthMenuInfoEntity authMenuInfoEntity;

    public AuthMenuInfoEntity getAuthMenuInfoEntity() {
        return authMenuInfoEntity;
    }

    public void setAuthMenuInfoEntity(AuthMenuInfoEntity authMenuInfoEntity) {
        this.authMenuInfoEntity = authMenuInfoEntity;
    }

    public List<AuthMenuInfoEntity> getPageList() {
        return pageList;
    }

    public void setPageList(List<AuthMenuInfoEntity> pageList) {
        this.pageList = pageList;
    }
}
