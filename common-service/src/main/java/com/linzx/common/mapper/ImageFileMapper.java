package com.linzx.common.mapper;

import com.linzx.common.domain.ImageFile;
import com.linzx.framework.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图片  数据层
 * 
 * @author linzixiang
 * @date 2019_07_207
 */
public interface ImageFileMapper extends BaseMapper<Long, ImageFile> {
	/**
     * 查询图片 信息
     * 
     * @param imageId 图片 ID
     * @return 图片 信息
     */
	public ImageFile getImageFileById(Long imageId);
	
	/**
     * 查询图片 列表
     * 
     * @param imageFile 图片 信息
     * @return 图片 集合
     */
	public List<ImageFile> selectImageFileList(ImageFile imageFile);
	
	/**
     * 新增图片 
     * 
     * @param imageFile 图片 信息
     * @return 结果
     */
	public Long insertImageFile(ImageFile imageFile);
	
	/**
     * 修改图片 
     * 
     * @param imageFile 图片 信息
     * @return 结果
     */
	public int updateImageFile(ImageFile imageFile);
	
	/**
     * 删除图片 
     * 
     * @param imageId 图片 ID
     * @return 结果
     */
	public int deleteImageFileById(Long imageId);
	
	/**
     * 批量删除图片 
     * 
     * @param imageIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteImageFileByIds(Long[] imageIds);

	/**
	 * 根据图片规格获取
	 */
	public Map<String, Object> getImageInfoBySpec(@Param("imageId") Long fileId, @Param("imageSpec") String spec);
	
}