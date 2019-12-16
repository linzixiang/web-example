package com.linzx.system.mapper;

import com.linzx.framework.bean.DictBean;
import com.linzx.framework.mapper.BaseMapper;
import com.linzx.framework.web.vo.DictOptionVo;
import com.linzx.system.domain.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统字典  数据层
 * 
 * @author linzixiang
 * @date 2019_05_133
 */
public interface DictMapper extends BaseMapper<Long, Dict> {

	/**
	 * 根据字典唯一编码获取，参数可以是dictCode，excludeId
	 * @return
	 */
	Dict getByBizCode(Map<String, Object> params);

}