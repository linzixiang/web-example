package com.linzx.system.service.impl;

import java.util.List;

import com.linzx.framework.bean.DictBean;
import com.linzx.framework.web.BaseService;
import com.linzx.framework.web.service.DictCodeService;
import com.linzx.framework.web.vo.DictOptionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.system.mapper.DictOptionsMapper;
import com.linzx.system.domain.DictOptions;
import com.linzx.system.service.IDictOptionsService;
import com.linzx.utils.Convert;

/**
 * 系统字典选项  服务层实现
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
@Service("dictOptionsService")
public class DictOptionsService extends BaseService<Long, DictOptions, DictOptionsMapper> implements IDictOptionsService, DictCodeService {

	@Autowired
	private DictOptionsMapper dictOptionsMapper;

	@Override
	public List<DictOptionVo> queryDictOptions(DictBean dictBean) {
		return dictOptionsMapper.selectDictOptionList(dictBean);
	}

	@Override
	public DictOptionsMapper setMapper() {
		return dictOptionsMapper;
	}
}
