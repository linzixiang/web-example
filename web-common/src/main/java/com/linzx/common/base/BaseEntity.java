package com.linzx.common.base;

import com.linzx.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库实体对象基类
 */
public abstract class BaseEntity<PK> implements Serializable, IPrimaryKey<PK> {

    /** excel导出列，用逗号分隔 **/
    public static final String EXCEL_EXPORT_COLUMNS = "exportColumns";

    /** 是否停用 （0正常 1停用） */
    private Integer status;
    /** 删除标志 （0代表存在 2代表删除） */
    private Integer delFlag;
    /** 创建者 */
    private Long createdBy;
    /** 创建时间 */
    private Date createdTime;
    /** 更新者 */
    private Long updatedBy;
    /** 更新时间 */
    private Date updatedTime;
    /** 乐观锁 */
    private Integer revision;
    /** 备注 */
    private String remark;
    ///////////////////////////////////////////////////查询条件参数////////////////////////////////////////////////////////////
    /** 创建起始时间，例如：2019-07-24 **/
    private String createdTimeGte = "";
    /** 创建结束时间 **/
    private String createdTimeLte = "";
    /** 更新起始时间 **/
    private String updatedTimeGte = "";
    /** 更新结束时间 **/
    private String updatedTimeLte = "";
    /** 参数 */
    private Map<String, Object> params;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRevision() {
        return revision;
    }
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreatedTimeGte() {
        if (StringUtils.isNotEmpty(createdTimeGte) && createdTimeGte.length() == 10) {
            createdTimeGte = createdTimeGte + " 00:00:00";
        }
        return createdTimeGte;
    }

    public void setCreatedTimeGte(String createdTimeGte) {
        this.createdTimeGte = createdTimeGte;
    }

    public String getCreatedTimeLte() {
        if (StringUtils.isNotEmpty(createdTimeLte) && createdTimeLte.length() == 10) {
            createdTimeLte = createdTimeLte + " 23:59:59";
        }
        return createdTimeLte;
    }

    public void setCreatedTimeLte(String createdTimeLte) {
        this.createdTimeLte = createdTimeLte;
    }

    public String getUpdatedTimeGte() {
        if (StringUtils.isNotEmpty(updatedTimeGte) && updatedTimeGte.length() == 10) {
            updatedTimeGte = updatedTimeGte + " 00:00:00";
        }
        return updatedTimeGte;
    }

    public void setUpdatedTimeGte(String updatedTimeGte) {
        this.updatedTimeGte = updatedTimeGte;
    }

    public String getUpdatedTimeLte() {
        if (StringUtils.isNotEmpty(updatedTimeLte) && updatedTimeLte.length() == 10) {
            updatedTimeLte = updatedTimeLte + " 23:59:59";
        }
        return updatedTimeLte;
    }

    public void setUpdatedTimeLte(String updatedTimeLte) {
        this.updatedTimeLte = updatedTimeLte;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
