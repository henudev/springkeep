package com.h3c.bigdata.zhgx.function.system.dao;

import com.h3c.bigdata.zhgx.common.persistence.BaseMapper;
import com.h3c.bigdata.zhgx.function.system.entity.MessageInfo;
import com.h3c.bigdata.zhgx.function.system.model.DeleteMsgBean;
import com.h3c.bigdata.zhgx.function.system.model.MessageQueryBean;
import com.h3c.bigdata.zhgx.function.system.model.MessageResultBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统（包含个人通知消息或系统通知公告） 数据层
 *
 * @author j16898
 * @date 2018-07-30
 */
@Repository
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {
    /**
     * 查询系统（包含个人通知消息或系统通知公告）信息
     *
     * @param id 系统（包含个人通知消息或系统通知公告）ID
     * @return 系统（包含个人通知消息或系统通知公告）信息
     */
    MessageInfo selectMessageInfoById(String id);

    /**
     * 查询系统（包含个人通知消息或系统通知公告）列表
     *
     * @param messageQueryBean 系统（包含个人通知消息或系统通知公告）信息
     * @return 系统（包含个人通知消息或系统通知公告）集合
     */
    List<MessageResultBean> selectMessageInfoList(MessageQueryBean messageQueryBean);

    /**
     * 新增系统（包含个人通知消息或系统通知公告）
     *
     * @param messageInfo 系统（包含个人通知消息或系统通知公告）信息
     * @return 结果
     */
    int insertMessageInfo(MessageInfo messageInfo);

    /**
     * 根据用户id查询出该用户所有的未读消息
     * @param userId
     * @return
     */
    List<String> selectAllNoReadMsg(@Param("userId") String userId);

    /**
     * 全部阅读消息
     * @param msgIds
     * @return
     */
    int readAllMsg(@Param("msgIds") List<String> msgIds);

    /**
     * 修改系统（包含个人通知消息或系统通知公告）
     *
     * @param messageInfo 系统（包含个人通知消息或系统通知公告）信息
     * @return 结果
     */
    int updateMessageInfo(MessageInfo messageInfo);

    /**
     * 删除系统（包含个人通知消息或系统通知公告）
     *
     * @param id 系统（包含个人通知消息或系统通知公告）ID
     * @return 结果
     */
    int deleteMessageInfoById(String id);

    /**
     * 批量删除系统（包含个人通知消息或系统通知公告）
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteMessageInfoByIds(String[] ids);

    /**
     * 批量删除系统 根据类型
     *
     * @param deleteMsgBean
     * @return
     */
    int deleteAllMsgBy(@Param("deleteMsgBean") DeleteMsgBean deleteMsgBean);

    /**
     * 分类型查询个人审批列表未审批统计值
     *
     * @param userId
     * @return
     */
    MessageQueryBean selectCountByType(@Param("userId") String userId);
    /**
     * 查询我的审批中数据申请的统计值
     *
     * @param userId
     * @return
     */
    int selectApplyCount(@Param("userId") String userId);
}