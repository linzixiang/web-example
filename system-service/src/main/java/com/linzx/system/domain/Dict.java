package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 系统字典 表 sys_dict
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
public class Dict extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long dictId;
	/** 字典名称 */
	private String dictName;
	/** 唯一编码 */
	private String dictCode;

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}
	public Long getDictId() {
		return dictId;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	public String getDictName() {
		return dictName;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public String getDictCode() {
		return dictCode;
	}

	@Override
	public void setId(Long id) {
		this.setDictId(id);
	}
}
