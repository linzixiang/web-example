package com.linzx.system.service.impl;

import java.util.List;

import com.linzx.framework.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.framework.web.BaseService;
import com.linzx.system.mapper.ConfigMapper;
import com.linzx.system.domain.Config;
import com.linzx.system.service.IConfigService;
import com.linzx.utils.Convert;

/**
 * 系统配置 服务层实现
 * 
 * @author linzixiang
 * @date 2019_07_203
 */
@Service
public class ConfigService extends BaseService<Long, Config, ConfigMapper> implements IConfigService {

	@Autowired
	private ConfigMapper configMapper;
	
	/**
     * 查询系统配置列表
     * 
     * @param config 系统配置信息
     * @return 系统配置集合
     */
	@Override
	public List<Config> selectConfigList(Config config, boolean ignorePage) {
		if (!ignorePage) {
			PageHelper.startPage();
		}
	    return configMapper.selectList(config);
	}

	@Override
	public ConfigMapper setMapper() {
		return configMapper;
	}
}
