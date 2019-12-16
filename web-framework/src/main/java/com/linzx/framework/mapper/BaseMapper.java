package com.linzx.framework.mapper;

import com.linzx.common.base.BaseEntity;

import java.util.List;

public interface BaseMapper<PK, E extends BaseEntity<PK>> {

    /**
     * 主键查询
     * @param id
     * @return
     */
    E getById(PK id);

    /**
     * 复杂查询列表
     * @param dept
     * @return
     */
    List<E> selectList(E dept);

    /**
     * 新增
     * @param entity
     * @return
     */
    PK insert(E entity);

    /**
     * 更新
     * @param dept
     * @return
     */
    int update(E dept);

    /**
     * 根据主键删除记录
     * @param deptId
     * @return
     */
    int deleteById(PK deptId);

    /**
     * 根据主键批量删除记录
     * @param deptIds
     * @return
     */
    int deleteByIds(PK[] deptIds);

}
