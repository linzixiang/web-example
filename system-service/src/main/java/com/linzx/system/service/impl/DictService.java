package com.linzx.system.service.impl;

import java.util.List;
import java.util.Map;

import com.linzx.framework.web.BaseService;
import com.linzx.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.DictMapper;
import com.linzx.system.domain.Dict;
import com.linzx.system.service.IDictService;
import com.linzx.utils.Convert;

/**
 * 系统字典  服务层实现
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
@Service("dictService")
public class DictService extends BaseService<Long, Dict, DictMapper> implements IDictService {

	@Autowired
	private DictMapper dictMapper;

	/**
	 * 检查字典code是否已存在
	 */
	@Override
	public Boolean isDictCodeExist(String dictCode, String selfId) {
		Map<String, Object> paramMap = MapUtils.genHashMap("dictCode", dictCode, "excludeId", selfId);
		Dict dict = dictMapper.getByBizCode(paramMap);
		if (dict != null) {
			return true;
		}
		return false;
	}

	@Override
	public Dict getBybBizCode(String dictCode) {
		Map<String, Object> paramMap = MapUtils.genHashMap("dictCode", dictCode);
		Dict dict = dictMapper.getByBizCode(paramMap);
		return dict;
	}

	@Override
	public DictMapper setMapper() {
		return this.dictMapper;
	}
}
