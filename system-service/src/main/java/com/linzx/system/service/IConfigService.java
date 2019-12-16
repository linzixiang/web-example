package com.linzx.system.service;

import com.linzx.system.domain.Config;
import java.util.List;

/**
 * 系统配置 服务层
 * 
 * @author linzixiang
 * @date 2019_07_203
 */
public interface IConfigService {
	
	/**
     * 查询系统配置列表
     * 
     * @param config 系统配置信息
     * @return 系统配置集合
     */
	public List<Config> selectConfigList(Config config, boolean ignorePage);
	
}
