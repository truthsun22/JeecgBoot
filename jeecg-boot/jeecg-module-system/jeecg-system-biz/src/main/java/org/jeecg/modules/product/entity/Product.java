package org.jeecg.modules.product.entity;

import java.io.Serializable;
import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @Description: 商品表
 * @Author: jeecg-boot
 * @Date: 2026-04-27
 * @Version: V1.0
 */
@Data
@TableName("product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String id;

    /**创建人*/
    private java.lang.String createBy;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**删除状态*/
    private java.lang.Integer delFlag;

    /**修改人*/
    private java.lang.String updateBy;

    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

    /**所属部门*/
    private java.lang.String sysOrgCode;

    /**商品名称*/
    @Excel(name="商品名称",width=40)
    private java.lang.String name;

    /**商品类型(1：家电，2：饮食：3：其它)*/
    @Excel(name="商品类型",width=15,dicCode="product_type")
    @Dict(dicCode = "product_type")
    private java.lang.Integer productType;

    /**备注*/
    @Excel(name="备注",width=50)
    private java.lang.String remarks;

}
