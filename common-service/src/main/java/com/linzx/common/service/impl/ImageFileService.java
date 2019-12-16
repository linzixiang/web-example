package com.linzx.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linzx.common.constant.Constants;
import com.linzx.common.service.IImageFileService;
import com.linzx.framework.bean.ImageBean;
import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.web.service.ImageUploadService;
import com.linzx.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linzx.framework.web.BaseService;
import com.linzx.common.mapper.ImageFileMapper;
import com.linzx.common.domain.ImageFile;
import com.linzx.utils.Convert;

/**
 * 图片  服务层实现
 * 
 * @author linzixiang
 * @date 2019_07_207
 */
@Service("imageFileService")
public class ImageFileService extends BaseService<Long, ImageFile, ImageFileMapper> implements IImageFileService, ImageUploadService {

	private static Map<String, String> thumbnailColMap = new HashMap<>();

	static {
		thumbnailColMap.put(Constants.IMAGE_ORIGINAL, "origin_info");
		thumbnailColMap.put(Constants.IMAGE_THUMBNAIL_800, "thumbnail_800");
	}

	@Autowired
	private ImageFileMapper imageFileMapper;

	@Override
	public Long addOriginImage(String name, String ext, ImageBean imageBean) {
		ImageFile imageFile = new ImageFile();
		imageFile.setName(name);
		imageFile.setImageExt(ext);
		JSONObject json = new JSONObject();
		json.put("path", imageBean.getPath());
		json.put("size", imageBean.getSize());
		json.put("width", imageBean.getWidth());
		json.put("height", imageBean.getHeight());
		json.put("storeWay", imageBean.getStoreWay());
		imageFile.setOriginInfo(json.toJSONString());
		super.insert(imageFile);
		return imageFile.getImageId();
	}

	@Override
	public void appendThumbnailImage(Long fileId, String spec, ImageBean imageBean) {
		String thumbnailColName = thumbnailColMap.get(spec);
		checkImageSpec(thumbnailColName);
		ImageFile imageFile = new ImageFile();
		imageFile.setImageId(fileId);
		imageFile.getParams().put("spcColumn", thumbnailColName);
		imageFile.getParams().put("spcVal", JSON.toJSONString(imageBean));
		super.update(imageFile);
	}

	@Override
	public Map<String, Object> getImageInfo(Long fileId, String spec) throws Exception {
		String thumbnailColName = thumbnailColMap.get(spec);
		checkImageSpec(thumbnailColName);
		Map<String, Object> imageInfoMap = imageFileMapper.getImageInfoBySpec(fileId, thumbnailColName);
		if (imageInfoMap == null) {
			throw new BizCommonException("common.file.origin.not.exist");
		}
		JSONObject jsonObject = MapUtils.getJSONObject(imageInfoMap, thumbnailColName);
		if (jsonObject == null) {
			return null;
		}
		jsonObject.put("imageExt", imageInfoMap.get("imageExt"));
		jsonObject.put("name", imageInfoMap.get("name"));
		return jsonObject;
	}

	private void checkImageSpec(String thumbnailColName) {
		if (thumbnailColName == null) {
			String[] specArr = new String[thumbnailColMap.size()];
			int index = -1;
			for (String str : thumbnailColMap.keySet()) {
				index = index + 1;
				specArr[index] = str;
			}
			String specStr = Convert.toStrConcat(specArr, ",");
			throw new BizCommonException("common.image.spec.not.support", new String[] {specStr});
		}
	}

	@Override
	public ImageFileMapper setMapper() {
		return this.imageFileMapper;
	}
}
