package com.linzx.framework.web;

import com.linzx.common.base.BaseEntity;
import com.linzx.common.constant.Constants;
import com.linzx.framework.mapper.BaseMapper;
import com.linzx.framework.shiro.bean.UserLoginInfo;
import com.linzx.framework.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<PK,E extends BaseEntity<PK>, DAO extends BaseMapper<PK, E>> implements InitializingBean {

    protected DAO baseDAO;

    public Optional<E> getById(@Nonnull PK id) {
        return Optional.ofNullable(baseDAO.getById(id));
    }

    public Optional<List<E>> selectList(E dept) {
        return Optional.of(baseDAO.selectList(dept));
    }

    public Optional<PK> insert(E entity) {
        initDefaultBeforeCreated(entity);
        return Optional.of(baseDAO.insert(entity));
    }

    public int update(E dept) {
        initDefaultBeforeUpdated(dept);
        return baseDAO.update(dept);
    }

    public int deleteById(PK id) {
        return baseDAO.deleteById(id);
    }

    public int deleteByIds(PK[] deptIds) {
        return baseDAO.deleteByIds(deptIds);
    }

    public abstract DAO setMapper();

    /**
     * 创建前初始化实体类的默认值
     * @param baseEntity
     */
    protected void initDefaultBeforeCreated(E baseEntity) {
        baseEntity.setCreatedTime(new Date());
        baseEntity.setUpdatedTime(new Date());
        if (baseEntity.getDelFlag() == null) {
            baseEntity.setDelFlag(Constants.STATUS_NORMAL);
        }
        if (baseEntity.getStatus() == null) {
            baseEntity.setStatus(Constants.STATUS_NORMAL);
        }
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        if (userLoginInfo != null) {
            baseEntity.setCreatedBy(userLoginInfo.getUserId());
            baseEntity.setUpdatedBy(userLoginInfo.getUserId());
        } else {
            baseEntity.setCreatedBy(new Long(0));
            baseEntity.setUpdatedBy(new Long(0));
        }
    }

    /**
     * 更新前初始化实体类的默认值
     * @param baseEntity
     */
    protected void initDefaultBeforeUpdated(E baseEntity) {
        baseEntity.setUpdatedTime(new Date());
        UserLoginInfo userLoginInfo = ShiroUtils.getUserLoginInfo();
        if (userLoginInfo == null) {
            baseEntity.setUpdatedTime(new Date());
        }
    }

    public void afterPropertiesSet() {
        this.baseDAO = this.setMapper();
        afterBeanInit();
    }

    protected void afterBeanInit() {
    }

}
