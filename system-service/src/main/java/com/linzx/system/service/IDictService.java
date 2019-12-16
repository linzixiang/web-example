package com.linzx.system.service;

import com.linzx.system.domain.Dict;
import java.util.List;
import java.util.Map;

/**
 * 系统字典  服务层
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
public interface IDictService {
	/**
	 * 检查字典code是否存在
	 * @param dictCode
	 * @param selfId
	 * @return true 表示字典编码已存在
	 */
	public Boolean isDictCodeExist(String dictCode, String selfId);

	/**
	 * 根据业务唯一键获取字典
	 * @param dictCode 唯一编码
	 * @return
	 */
	public Dict getBybBizCode(String dictCode);

}
