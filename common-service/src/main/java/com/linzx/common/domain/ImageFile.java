package com.linzx.common.domain;

import com.linzx.common.base.BaseEntity;

/**
 * 图片 表 common_image_file
 * 
 * @author linzixiang
 * @date 2019_07_207
 */
public class ImageFile extends BaseEntity<Long>{

	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long imageId;
	/** 文件名 */
	private String name;
	/** 扩展名 (例如：jpg,png等) */
	private String imageExt;
	/** 原图信息 json结构({size:'文件大小(kb)',width:'宽度',height:'高度',path:'路径',storeWay:'存储方式，1表示fastDFS，2表示文件夹'}) */
	private String originInfo;
	/** 缩略图信息 (800*800尺寸，json结构) */
	private String thumbnail800;

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	public Long getImageId() {
		return imageId;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setImageExt(String imageExt) {
		this.imageExt = imageExt;
	}
	public String getImageExt() {
		return imageExt;
	}

	public void setOriginInfo(String originInfo) {
		this.originInfo = originInfo;
	}
	public String getOriginInfo() {
		return originInfo;
	}

	public void setThumbnail800(String thumbnail800) {
		this.thumbnail800 = thumbnail800;
	}
	public String getThumbnail800() {
		return thumbnail800;
	}

	@Override
	public void setId(Long id) {
		this.setImageId(id);
	}
}
