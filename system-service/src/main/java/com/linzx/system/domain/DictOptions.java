package com.linzx.system.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 系统字典选项 表 sys_dict_options
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
public class DictOptions extends BaseEntity<Long>{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long dictOptionId;
	/** 唯一编码 */
	private String dictCode;
	/** 选项标签 */
	private String optionLabel;
	/** 字典键值 */
	private Integer dictValue;
	/** 是否默认 （0否 1是） */
	private Integer isDefault;
	/** 选项排序 （降序） */
	private Integer orderNum;
	/** 样式属性 （其他样式扩展） */
	private String cssClass;
	/** 回显样式 （表格回显样式） */
	private String listClass;

	public void setDictOptionId(Long dictOptionId) {
		this.dictOptionId = dictOptionId;
	}
	public Long getDictOptionId() {
		return dictOptionId;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	public String getDictCode() {
		return dictCode;
	}

	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}
	public String getOptionLabel() {
		return optionLabel;
	}

	public void setDictValue(Integer dictValue) {
		this.dictValue = dictValue;
	}
	public Integer getDictValue() {
		return dictValue;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getCssClass() {
		return cssClass;
	}

	public void setListClass(String listClass) {
		this.listClass = listClass;
	}
	public String getListClass() {
		return listClass;
	}

	@Override
	public void setId(Long id) {
		this.setDictOptionId(id);
	}
}
