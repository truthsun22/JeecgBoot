package org.jeecg.modules.animal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("animal")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "野生动物")
public class Animal extends JeecgEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "动物名称", width = 15)
    @Schema(description = "动物名称")
    private String animalName;

    @Excel(name = "动物类别", width = 15)
    @Schema(description = "动物类别")
    private String animalType;

    @Excel(name = "备注", width = 30)
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "删除状态(0-正常,1-已删除)")
    private Integer delFlag;

    @Schema(description = "所属部门")
    private String sysOrgCode;

    @Schema(description = "租户id")
    private String tenantId;
}
