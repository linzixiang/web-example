package com.linzx.utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.linzx.utils.pdf.builder.PDFBuilder;
import com.linzx.utils.pdf.builder.PdfBuilderListener;
import com.linzx.utils.pdf.builder.PDFBuilder.FontConfig;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PdfReportUtils {

    public static void htmlToA4PdfFile(String html, String pdfSavePath, FontConfig fontConfig) throws Exception {
        htmlToA4PdfFile(html, pdfSavePath, fontConfig, null);
    }

    /**
     * 将html转pdf保存到指定路径
     * @param html html数据
     * @param pdfSavePath 生成pdf的保存数据
     * @param fontConfig pdf字体相关信息
     * @param builderListener 实现该接口，定制页眉页脚的生成
     * @throws Exception
     */
    public static void htmlToA4PdfFile(String html, String pdfSavePath, FontConfig fontConfig, PdfBuilderListener builderListener) throws Exception {
        File file = new File(pdfSavePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            PDFBuilder builder = new PDFBuilder(builderListener, fontConfig);
            htmlToA4PdfOutStream(html, outputStream, builder);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 将html转pdf输出流
     * @param html html数据
     * @param outputStream 生成pdf的保存数据
     * @param builder
     * @throws Exception
     */
    public static void htmlToA4PdfOutStream(String html, OutputStream outputStream, PDFBuilder builder) throws Exception {
        // 设置文档大小
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setPageEvent(builder);
        // 获取字体路径
        String fontPath = builder.getFontSourcePath();
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()),
                    XMLWorkerHelper.class.getResourceAsStream("/default.css"), Charset.forName("UTF-8"),
                    new XMLWorkerFontProvider(fontPath));
        } finally {
            document.close();
        }
    }

}
