package com.linzx.framework.web.service;

import com.linzx.framework.bean.DictBean;
import com.linzx.framework.web.vo.DictOptionVo;

import java.util.List;

public interface DictCodeService {

    List<DictOptionVo> queryDictOptions(DictBean dictBean);

}
