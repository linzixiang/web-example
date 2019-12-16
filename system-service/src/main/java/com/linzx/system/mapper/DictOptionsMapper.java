package com.linzx.system.mapper;

import com.linzx.framework.bean.DictBean;
import com.linzx.framework.mapper.BaseMapper;
import com.linzx.framework.web.vo.DictOptionVo;
import com.linzx.system.domain.DictOptions;
import java.util.List;	

/**
 * 系统字典选项  数据层
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
public interface DictOptionsMapper extends BaseMapper<Long, DictOptions> {

	/**
	 * 查询返回字典基础信息
	 */
	List<DictOptionVo> selectDictOptionList(DictBean dictBean);
	
}