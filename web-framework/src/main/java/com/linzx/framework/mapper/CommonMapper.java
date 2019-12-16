package com.linzx.framework.mapper;

import com.linzx.framework.bean.CodeBean;
import com.linzx.framework.bean.DictBean;
import com.linzx.framework.web.vo.TableCodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonMapper {

    List<TableCodeVo> selectCodeList(@Param("codeBean") CodeBean codeBean, @Param("searchKeyword") String searchKeyword);

}
