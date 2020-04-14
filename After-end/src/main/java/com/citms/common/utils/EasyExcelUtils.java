package com.citms.common.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class EasyExcelUtils {
    /**
     *
     * @param is            输入文件的流对象
     * @param clazz         每行数据转换成的对象类
     * @param sheetNo       第几个sheet
     * @param excelListener excel的监听类
     * @return              是否成功,如果成功可以再监听类里面取数据
     */
    public static Boolean readExcel(InputStream is, Class clazz, int sheetNo, AnalysisEventListener excelListener){
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            // 解析每行结果在listener中处理
            ExcelReader excelReader = EasyExcelFactory.getReader(bis, excelListener);
            excelReader.read(new Sheet(sheetNo, 1, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     *
     * @param os 文件输出流
     * @param clazz Excel实体映射类
     * @param data 导出数据
     * @return
     */
    public static Boolean writeExcel(OutputStream os, Class clazz, List<? extends BaseRowModel> data, int seetNo){
        BufferedOutputStream bos= null;
        try {
            bos = new BufferedOutputStream(os);
            ExcelWriter writer = new ExcelWriter(bos, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(seetNo, 0,clazz);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     *
     * @param os 文件输出流
     * @param clazz Excel实体映射类
     * @param data 导出数据
     * @return
     */
    public static Boolean writeEasyExcel(OutputStream os, Class clazz, List<? extends BaseRowModel> data, String name){
        BufferedOutputStream bos= null;
        try {

            bos = new BufferedOutputStream(os);
            ExcelWriter writer = new ExcelWriter(bos, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0,clazz);
            sheet1.setSheetName(name);
            Map columnWidth = new HashMap();
            columnWidth.put(0,3000);columnWidth.put(1,3000);columnWidth.put(2,3000);columnWidth.put(3,3000);
            sheet1.setColumnWidthMap(columnWidth);

            //设置列宽 设置每列的宽度
            //sheet1.setAutoWidth(Boolean.TRUE);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 生成excle
     * @param filePath 绝对路径, 如：/home/chenmingjian/Downloads/aaa.xlsx
     * @param data 数据源
     * @param sheet excle页面样式
     * @param head 表头
     */
    public static Boolean writeSimpleBySheet(String filePath, List<List<Object>> data, List<String> head, Sheet sheet){
        boolean flag = false;
        sheet = (sheet != null) ? sheet : new Sheet(1, 0);

        if(head != null){
            List<List<String>> list = new ArrayList<>();
            head.forEach(h -> list.add(Collections.singletonList(h)));
            sheet.setHead(list);
        }

        OutputStream outputStream = null;
        ExcelWriter writer = null;
        try {
            outputStream = new FileOutputStream(filePath);
            writer = EasyExcelFactory.getWriter(outputStream);
            writer.write1(data,sheet);
            flag = true;
        } catch (FileNotFoundException e) {
            log.error("找不到文件或文件路径错误, 文件：{}", filePath);
        }finally {
            try {
                if(writer != null){
                    writer.finish();
                }

                if(outputStream != null){
                    outputStream.close();
                }

            } catch (IOException e) {
                log.error("excel文件导出失败, 失败原因：{}", e);
            }
            return flag;
        }

    }

    public static String userAttachPath() {
        String path = System.getProperties().getProperty("user.home") + "\\hscl";
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        return path;
    }
}
