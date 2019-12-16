package com.linzx.utils.imgae;

import com.linzx.utils.FileUtils;
import com.linzx.utils.UnitConvertUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class ImagesUtils {

    /**
     *
     */
    public static ImageInfo getImageInfo() {
        return null;
    }


    /**
     * 图片居中裁剪，
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param targetWidth 目标文件宽度
     * @param targetHeight 目标文件高度
     */
    public static ImageInfo cropCenter(File sourceFile, File targetFile, int targetWidth, int targetHeight) throws IOException {
        FileUtils.createParentDirIfNotExist(targetFile);
        BufferedImage srcImage = null;
        int s_height = 0;
        int s_width = 0;
        srcImage = ImageIO.read(sourceFile);
        // 获取源文件的宽高
        s_height = srcImage.getHeight();
        s_width = srcImage.getWidth();
        // 判断是否需要压缩
        if (targetWidth != s_width || targetHeight != s_height) {// 需要压缩
            BigDecimal n = new BigDecimal(s_height).divide(new BigDecimal(s_width), 10, BigDecimal.ROUND_UP);// 源图片文件高/宽  的比例
            BigDecimal m = new BigDecimal(targetHeight).divide(new BigDecimal(targetWidth), 10, BigDecimal.ROUND_UP);// 目标图片文件高/宽 的比例
            int flag = n.compareTo(m); //比较源图片文件和目标图片 的高/宽 比
            if (flag == 0) {// n == m，高/宽 比一致, 无需裁减, 直接压缩
                System.out.println("flag： " + flag);
                Thumbnails.of(sourceFile).forceSize(targetWidth, targetHeight).toFile(targetFile);
            } else { // 高/宽 比不一致，需要裁减
                if (flag > 0) {// 高的比例 > 宽的比例，以宽的比例进行缩放， 需要裁减高
                    srcImage = Thumbnails.of(sourceFile).width(targetWidth).asBufferedImage();//以宽的比例进行缩放
                } else if (flag < 0) {// n < m 高的比例 < 宽的比例，以高的比例进行缩放， 需要裁减宽
                    srcImage = Thumbnails.of(sourceFile).height(targetHeight).asBufferedImage();//以高的比例进行缩放
                }
                // 居中裁减
                Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(srcImage)
                        .sourceRegion(Positions.CENTER, targetWidth, targetHeight).size(targetWidth, targetHeight);
                builder.toFile(targetFile);
            }
        }
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setHeight(targetHeight);
        imageInfo.setWidth(targetWidth);
        imageInfo.setSize(UnitConvertUtils.byteToOther(targetFile.length()));
        return imageInfo;
    }

    public static void main(String[] args) throws IOException {
//        File sourceFile = new File("C:/Users/LZX/Desktop/微信图片_20190725093129.jpg");
//        String targetFile = "C:/Users/LZX/Desktop/cropCenter.jpg";
//        ImagesUtils.cropCenter(sourceFile,targetFile, 800, 800);
    }
}
