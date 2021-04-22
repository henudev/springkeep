package com.h3c.bigdata.zhgx.function.system.entity;

import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_s_menu")
public class MenuEntity extends BaseEntity {
    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 菜单英文名称
     */
    @Column(name = "menu_name_en")
    private String menuNameEn;

    /**
     * 类型：1-接口URL（权限码）、2-菜单码；
     */
    private String type;

    /**
     * eg:el-icon-tickets 前端需要的属性字段
     */
    @Column(name = "icon_cls")
    private String iconCls;

    /**
     * eg:#24807D 前端需要的属性字段
     */
    @Column(name = "icon_col")
    private String iconCol;

    /**
     * 组件名称，前端需要
     */
    private String component;

    /**
     * 菜单对应的路径
     */
    @Column(name = "menu_url")
    private String menuUrl;

    /**
     * 菜单级别
     */
    @Column(name = "menu_level")
    private String menuLevel;

    /**
     * 上级菜单编码
     */
    @Column(name = "parent_menu_code")
    private String parentMenuCode;

    /**
     * 是否是菜单
     */
    @Column(name = "is_menu")
    private String isMenu;

    /**
     * 菜单更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 备用字段01,值为1-标注为待处理菜单
     */
    @Column(name = "reserve_str1")
    private String reserveStr1;

    /**
     * 备用字段02
     */
    @Column(name = "reserve_str2")
    private String reserveStr2;

    /**
     * 备用字段03
     */
    @Column(name = "reserve_str3")
    private String reserveStr3;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取菜单编码
     *
     * @return menu_code - 菜单编码
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * 设置菜单编码
     *
     * @param menuCode 菜单编码
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
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

    /**
     * 获取菜单英文名称
     *
     * @return menu_name_en - 菜单英文名称
     */
    public String getMenuNameEn() {
        return menuNameEn;
    }

    /**
     * 设置菜单英文名称
     *
     * @param menuNameEn 菜单英文名称
     */
    public void setMenuNameEn(String menuNameEn) {
        this.menuNameEn = menuNameEn == null ? null : menuNameEn.trim();
    }

    /**
     * 获取类型：1-接口URL（权限码）、2-菜单码；
     *
     * @return type - 类型：1-接口URL（权限码）、2-菜单码；
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型：1-接口URL（权限码）、2-菜单码；
     *
     * @param type 类型：1-接口URL（权限码）、2-菜单码；
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取eg:el-icon-tickets 前端需要的属性字段
     *
     * @return icon_cls - eg:el-icon-tickets 前端需要的属性字段
     */
    public String getIconCls() {
        return iconCls;
    }

    /**
     * 设置eg:el-icon-tickets 前端需要的属性字段
     *
     * @param iconCls eg:el-icon-tickets 前端需要的属性字段
     */
    public void setIconCls(String iconCls) {
        this.iconCls = iconCls == null ? null : iconCls.trim();
    }

    /**
     * 获取eg:#24807D 前端需要的属性字段
     *
     * @return icon_col - eg:#24807D 前端需要的属性字段
     */
    public String getIconCol() {
        return iconCol;
    }

    /**
     * 设置eg:#24807D 前端需要的属性字段
     *
     * @param iconCol eg:#24807D 前端需要的属性字段
     */
    public void setIconCol(String iconCol) {
        this.iconCol = iconCol == null ? null : iconCol.trim();
    }

    /**
     * 获取组件名称，前端需要
     *
     * @return component - 组件名称，前端需要
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置组件名称，前端需要
     *
     * @param component 组件名称，前端需要
     */
    public void setComponent(String component) {
        this.component = component == null ? null : component.trim();
    }

    /**
     * 获取菜单对应的路径
     *
     * @return menu_url - 菜单对应的路径
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * 设置菜单对应的路径
     *
     * @param menuUrl 菜单对应的路径
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    /**
     * 获取菜单级别
     *
     * @return menu_level - 菜单级别
     */
    public String getMenuLevel() {
        return menuLevel;
    }

    /**
     * 设置菜单级别
     *
     * @param menuLevel 菜单级别
     */
    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel == null ? null : menuLevel.trim();
    }

    /**
     * 获取上级菜单编码
     *
     * @return parent_menu_code - 上级菜单编码
     */
    public String getParentMenuCode() {
        return parentMenuCode;
    }

    /**
     * 设置上级菜单编码
     *
     * @param parentMenuCode 上级菜单编码
     */
    public void setParentMenuCode(String parentMenuCode) {
        this.parentMenuCode = parentMenuCode == null ? null : parentMenuCode.trim();
    }

    /**
     * 获取是否是菜单
     *
     * @return is_menu - 是否是菜单
     */
    public String getIsMenu() {
        return isMenu;
    }

    /**
     * 设置是否是菜单
     *
     * @param isMenu 是否是菜单
     */
    public void setIsMenu(String isMenu) {
        this.isMenu = isMenu == null ? null : isMenu.trim();
    }

    /**
     * 获取菜单更新时间
     *
     * @return update_time - 菜单更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置菜单更新时间
     *
     * @param updateTime 菜单更新时间
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
     * 获取备用字段01,值为1-标注为待处理菜单
     *
     * @return reserve_str1 - 备用字段01,值为1-标注为待处理菜单
     */
    public String getReserveStr1() {
        return reserveStr1;
    }

    /**
     * 设置备用字段01,值为1-标注为待处理菜单
     *
     * @param reserveStr1 备用字段01,值为1-标注为待处理菜单
     */
    public void setReserveStr1(String reserveStr1) {
        this.reserveStr1 = reserveStr1 == null ? null : reserveStr1.trim();
    }

    /**
     * 获取备用字段02
     *
     * @return reserve_str2 - 备用字段02
     */
    public String getReserveStr2() {
        return reserveStr2;
    }

    /**
     * 设置备用字段02
     *
     * @param reserveStr2 备用字段02
     */
    public void setReserveStr2(String reserveStr2) {
        this.reserveStr2 = reserveStr2 == null ? null : reserveStr2.trim();
    }

    /**
     * 获取备用字段03
     *
     * @return reserve_str3 - 备用字段03
     */
    public String getReserveStr3() {
        return reserveStr3;
    }

    /**
     * 设置备用字段03
     *
     * @param reserveStr3 备用字段03
     */
    public void setReserveStr3(String reserveStr3) {
        this.reserveStr3 = reserveStr3 == null ? null : reserveStr3.trim();
    }
}