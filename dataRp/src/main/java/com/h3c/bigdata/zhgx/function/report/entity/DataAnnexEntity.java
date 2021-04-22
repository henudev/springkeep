package com.h3c.bigdata.zhgx.function.report.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.h3c.bigdata.zhgx.common.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.sql.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("data_annex")
public class DataAnnexEntity extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     *模板编号，可能作为表名
     */
    @ApiModelProperty("附件id")
    @Id
    private String id;

    @ApiModelProperty("附件原名")
    @TableField(value = "old_name")
    private String oldName;

    @ApiModelProperty("附件新名")
    @TableField(value = "new_name")
    private String newName;

    @ApiModelProperty("附件组id")
    @TableField(value = "groupid")
    private String groupId;

    @ApiModelProperty("文件类型")
    @TableField(value = "file_type")
    private String fileType;

    @ApiModelProperty("文件大小")
    @TableField(value = "file_size")
    private String fileSize;

    @ApiModelProperty("附件绝对路径")
    @TableField(value = "file_path")
    private String filePath;

    @ApiModelProperty("附件网络地址")
    @TableField(value = "url")
    private String url;

    @ApiModelProperty("模板id")
    @TableField(value = "template_id")
    private String templateId;

    @ApiModelProperty("md5")
    @TableField(value = "md5")
    private String md5;

    @ApiModelProperty("上传人id")
    @TableField(value = "user_id")
    private String userId;

    @ApiModelProperty("创建日期")
    @TableField(value = "import_date")
    private Date importDate;
}