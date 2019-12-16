package com.linzx.utils.pdf.builder;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilder extends PdfPageEventHelper {

	/** 基础字体对象 **/
	private BaseFont bf;

	/** 中文字体 **/
	private Font fontPro;

	/** 页眉页脚生成接口 **/
	private PdfBuilderListener builderListener;

	/** 字体文件路径 **/
	private String fontSourcePath;

	/** 文档字体大小 **/
	private int fontSize;

	/** 模板 **/
	private PdfTemplate template;

	public PDFBuilder(PdfBuilderListener builderListener, String fontSourcePath, int fontSize) {
		this.builderListener = builderListener;
		this.fontSourcePath = fontSourcePath;
		this.fontSize = fontSize;
	}

	public PDFBuilder(PdfBuilderListener builderListener, FontConfig fontConfig) {
		this.builderListener = builderListener;
		this.fontSourcePath = fontConfig.getSourcePath();
		this.fontSize = fontConfig.getFontSize();
	}

	public PDFBuilder(String fontSourcePath, int fontSize) {
		this.fontSourcePath = fontSourcePath;
		this.fontSize = fontSize;
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		template = writer.getDirectContent().createTemplate(50, 50);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		this.addPage(writer, document);
	}

	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		if (builderListener != null) {
			template.beginText();
			template.setFontAndSize(bf, fontSize);
			String replace = builderListener.getReplaceOfTemplate(writer, document);
			template.showText(replace);
			template.endText();
			template.closePath();
		}
	}

	/**
	 * 加分页
	 */
	private void addPage(PdfWriter writer, Document document) {
		if (builderListener != null) {
			// 1.初始化字体
			initFront();
			// 2.写入页眉
			builderListener.writeHeader(writer, document, fontPro, template);
			// 3.写入前半部分页脚
			builderListener.writeFooter(writer, document, fontPro, template);
		}
	}

	/**
	 * 初始化字体
	 */
	private void initFront() {
		try {
			if (bf == null) {
				bf = BaseFont.createFont(fontSourcePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			}
			if (fontPro == null) {
				fontPro = new Font(bf, fontSize, Font.NORMAL);// 数据体字体
            }
		} catch (Exception e) {
			throw new RuntimeException("pdf字体初始化失败", e);
		}
	}
	
	public void setPresentFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

	public int getFontSize() {
		return this.fontSize;
	}

	public String getFontSourcePath() {
		return this.fontSourcePath;
	}

	public class FontConfig {

		/** 字体路径 **/
		private String sourcePath;

		/** 字体大小 **/
		private int fontSize;

		public String getSourcePath() {
			return sourcePath;
		}

		public void setSourcePath(String sourcePath) {
			this.sourcePath = sourcePath;
		}

		public int getFontSize() {
			return fontSize;
		}

		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}
	}

}
