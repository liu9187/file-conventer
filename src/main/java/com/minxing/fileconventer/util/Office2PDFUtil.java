package com.minxing.fileconventer.util;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * office转为pdf工具类
 */
@Component
public class Office2PDFUtil {
    private static final Logger log = LoggerFactory.getLogger(Office2PDFUtil.class);

    /**
     * office转为pdf工具类
     *
     * @param inputFilePath
     * @return
     */
    public static File openOfficeToPDF(String inputFilePath, String fileName, String soffice_home, String tmp_root) {
        return office2pdf(inputFilePath, fileName,soffice_home,tmp_root);
    }


    /**
     * 链接OpenOffice
     *
     * @return
     */
    private static OfficeManager getOfficeManager(String soffice_home) {
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
        //获取openoffice安装路径
        config.setOfficeHome(soffice_home);
        log.info("获取openOffice安装路径");
        //config.setOfficeHome("C:/Program Files (x86)/OpenOffice 4");
        //启动服务
        OfficeManager officeManager = config.buildOfficeManager();
        log.info("openOffice服务开启....");
        officeManager.start();
        return officeManager;
    }

    /**
     * 转换文件
     *
     * @param inputFile
     * @param outputFilePath_end
     * @param converter
     * @return
     */
    private static File converterFile(File inputFile, String outputFilePath_end,
                                      OfficeDocumentConverter converter) {
        File outputFile = new File(outputFilePath_end);
        //假设目标路径不存在，新建路径
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        converter.convert(inputFile, outputFile);
        log.info("文件:" + inputFile + "转换pdf文件" + outputFile + "成功");
        return outputFile;
    }

    /**
     * 文件转换
     *
     * @param inputFilePath 文件路径
     * @return
     */
    private static File office2pdf(String inputFilePath, String fileName, String soffice_home, String tmp_root) {
        OfficeManager officeManager = null;
        File inputFile = null;
        try {
            if (StringUtils.isEmpty(inputFilePath)) {
                log.error("输入参数地址为null，转换终止");
                return null;
            }
            inputFile = new File(inputFilePath);
            //转换后的文件路径
            String outputFilePath_end = tmp_root + File.separator + getOutputFilePath(fileName);
            log.info("转换后的文件路径：" + outputFilePath_end);
            File newFile = new File(outputFilePath_end);
            //判断是否已经存在转化后的文件
//            if (newFile.exists()) {
//                return newFile;
//            }
            if (!inputFile.exists()) {
                log.info("输入文件不存在，转换终止");
                return null;
            }
            //获取安装路径并且启动服务
            officeManager = getOfficeManager(soffice_home);
            //链接openoffice
            OfficeDocumentConverter officeDocumentConverter = new OfficeDocumentConverter(officeManager);
            //转换文件
            return converterFile(inputFile, outputFilePath_end, officeDocumentConverter);
        } catch (Exception e) {
            //清理文件
            inputFile.delete();
            log.error("<<<<<<<转换出现异常");
        } finally {
            //停止服务
              log.info("openOffice服务停止........");
            if (officeManager != null) {
                officeManager.stop();
            }

        }
        return null;
    }

    /**
     * 获取输出文件地址
     *
     * @param fileName
     * @return
     */
    private static String getOutputFilePath(String fileName) {
        String outputFilePath = fileName.replaceAll("." + getPostfix(fileName), ".pdf");
        return outputFilePath;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getPostfix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void main(String[] args) {
//     File s=   Office2PDFUtil.office2pdf("D:/liu/定价和研发.xlsx");
//        System.out.println(s);
        String s = getOutputFilePath("定价和研发.xlsx");
        System.out.println(s);
    }
}
