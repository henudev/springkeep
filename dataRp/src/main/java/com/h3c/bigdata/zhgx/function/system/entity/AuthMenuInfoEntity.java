package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "auth_menu_info")
public class AuthMenuInfoEntity extends BaseEntity{

    @Transient
    List<AuthMenuInfoEntity> childMenus;

    public List<AuthMenuInfoEntity> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<AuthMenuInfoEntity> childMenus) {
        this.childMenus = childMenus;
    }

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 权限code
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 父级权限code
     */
    @Column(name = "parent_menu_code")
    private String parentMenuCode;

    /**
     * 状态，0启用，1停用
     */
    private String status;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 权限类型，1菜单，2按钮
     */
    @Column(name = "menu_type")
    private String menuType;

    /**
     * 显示顺序
     */
    @Column(name = "order_number")
    private Integer orderNumber;

    /**
     * 创建者
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 变更者
     */
    @Column(name = "update_user")
    private String updateUser;

    /**
     * 变更时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 功能类型，如：ADD,DELETE,QUERY,UPDATE等
     */
    @Column(name = "function_type")
    private String functionType;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取权限code
     *
     * @return menu_code - 权限code
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * 设置权限code
     *
     * @param menuCode 权限code
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    /**
     * 获取父级权限code
     *
     * @return parent_menu_code - 父级权限code
     */
    public String getParentMenuCode() {
        return parentMenuCode;
    }

    /**
     * 设置父级权限code
     *
     * @param parentMenuCode 父级权限code
     */
    public void setParentMenuCode(String parentMenuCode) {
        this.parentMenuCode = parentMenuCode == null ? null : parentMenuCode.trim();
    }

    /**
     * 获取状态，0启用，1停用
     *
     * @return status - 状态，0启用，1停用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，0启用，1停用
     *
     * @param status 状态，0启用，1停用
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取请求地址
     *
     * @return url - 请求地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取权限类型，1菜单，2按钮
     *
     * @return menu_type - 权限类型，1菜单，2按钮
     */
    public String getMenuType() {
        return menuType;
    }

    /**
     * 设置权限类型，1菜单，2按钮
     *
     * @param menuType 权限类型，1菜单，2按钮
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType == null ? null : menuType.trim();
    }

    /**
     * 获取显示顺序
     *
     * @return order_number - 显示顺序
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置显示顺序
     *
     * @param orderNumber 显示顺序
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 获取创建者
     *
     * @return create_user - 创建者
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建者
     *
     * @param createUser 创建者
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取变更者
     *
     * @return update_user - 变更者
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置变更者
     *
     * @param updateUser 变更者
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    /**
     * 获取变更时间
     *
     * @return update_time - 变更时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置变更时间
     *
     * @param updateTime 变更时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取菜单名称
     *
     * @return menu_name - 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单名称
     *
     * @param menuName 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }
}