package com.example.demo.util.excel.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class Test {

    //在创建样式的时候切记不要将创建的样式代码放到for循环中
    //边框样式
    private static CellStyle cellBorderStyle;
    //日期样式yyyy-MM-dd HH:mm:ss加边框
    private static CellStyle dateCellStyleForSS;
    //日期样式yyyy-MM-dd加边框
    private static CellStyle dateCellStyleForDD;
    //数字样式加边框
    private static CellStyle dateCellStyleForNumber;
    //百分比格式加边框
    private static CellStyle dateCellStyleForPer;

    //日期格式转换类
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws IOException {
        //用于后面计数
        short index = 0;

        FileOutputStream fileOut = null;
        //如果想要导出大量数据导excel中要使用SXSSFWorkbook这个类
        SXSSFWorkbook hssfWorkbook = new SXSSFWorkbook(20000);
        //创建sheet
        SXSSFSheet sheet = hssfWorkbook.createSheet("表1");
        //设置每个cell的宽度
        sheet.setDefaultColumnWidth(30);


        //声明样式
        cellBorderStyle = hssfWorkbook.createCellStyle();
        dateCellStyleForSS = hssfWorkbook.createCellStyle();
        dateCellStyleForDD = hssfWorkbook.createCellStyle();
        dateCellStyleForNumber = hssfWorkbook.createCellStyle();
        dateCellStyleForPer = hssfWorkbook.createCellStyle();
        //设置日期样
        DataFormat format = hssfWorkbook.createDataFormat();
        //边框样式
        cellBorderStyle = hssfWorkbook.createCellStyle();
        // 设置单元格边框
        cellBorderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellBorderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellBorderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellBorderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);

        //声明日期样式
        dateCellStyleForDD.setDataFormat(format.getFormat("yyyy-MM-dd"));

        // 设置单元格边框
        dateCellStyleForDD.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForDD.setBorderRight(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForDD.setBorderTop(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForDD.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForDD.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //声明日期样式
        dateCellStyleForSS.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));

        // 设置单元格边框
        dateCellStyleForSS.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForSS.setBorderRight(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForSS.setBorderTop(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForSS.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForSS.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //声明百分比格式
        dateCellStyleForPer.setDataFormat(format.getFormat("0.00%"));

        // 设置单元格边框
        dateCellStyleForPer.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForPer.setBorderRight(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForPer.setBorderTop(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForPer.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForPer.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //声明数字格式
        dateCellStyleForNumber.setDataFormat(format.getFormat("0.00"));

        // 设置单元格边框
        dateCellStyleForNumber.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForNumber.setBorderRight(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForNumber.setBorderTop(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForNumber.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        dateCellStyleForNumber.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        CellStyle titleStyle2 = hssfWorkbook.createCellStyle();
        // 设置单元格边框
        titleStyle2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        titleStyle2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        titleStyle2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        titleStyle2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        // 居中
        titleStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        CellStyle titleStyle = hssfWorkbook.createCellStyle();
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        // 居中
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        Font font = hssfWorkbook.createFont();
        font.setFontName("宋体");
        // 设置字体大小
        font.setFontHeightInPoints((short) 22);
        // 粗体显示
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(font);

        //创建第一行
        SXSSFRow title = sheet.createRow(0);
        SXSSFCell titleCell = title.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue("标题");

        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,4));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0,4));
        //创建第二行
        SXSSFRow titleTime = sheet.createRow(1);
        for(int i=0;i<5;i++){
            //第一个格式格式已经设置好数据了
            if(i!=0){
                //给第一行设置样式
                SXSSFCell titleCells = title.createCell(i);
                titleCells.setCellStyle(titleStyle);
            }
            SXSSFCell createCellSeconds = titleTime.createCell(i);
            if(i==0){
                //给第二行设置值
                createCellSeconds.setCellValue("开始：");
            }
            //给第二行设置样式
            createCellSeconds.setCellStyle(cellBorderStyle);
        }
        //创建第三行
        SXSSFRow titleRow = sheet.createRow(2);
        // 单元格赋值
        SXSSFCell createCell = titleRow.createCell( index++);
        createCell.setCellValue("String");
        createCell.setCellStyle(titleStyle2);
        SXSSFCell createCell2 = titleRow.createCell( index++);
        createCell2.setCellValue("date");
        createCell2.setCellStyle(titleStyle2);
        SXSSFCell createCell3=titleRow.createCell( index++);
        createCell3.setCellValue("百分号");
        createCell3.setCellStyle(titleStyle2);
        SXSSFCell createCell4=titleRow.createCell( index++);
        createCell4.setCellValue("数字");
        createCell4.setCellStyle(titleStyle2);
        SXSSFCell createCell6 = titleRow.createCell( index++);
        createCell6.setCellValue("date");
        createCell6.setCellStyle(titleStyle2);
        try {
            //之前从数据库中获得数据首先放到List<Map<String,Object>>中 数据过多会占用内存较多
            //所以建议从数据库遍历出来的数据直接进行放到excel中，当导入过多数据会减轻服务起压力
            for(int i=3;i<50000;i++){
                SXSSFRow row = sheet.createRow(i);
                for (int j = 1; j <= 6; j++) {
                    if (j==1) {
                        setCellValue("我是字符串",row.createCell((short) 0),"String");
                    }
                    if (j==2) {
                        setCellValue("2012-12-12 12:12:12wwww",row.createCell((short) 1),"DateDD");
                    }
                    if (j==3) {
                        setCellValue("12",row.createCell((short) 2),"Per");
                    }
                    if (j==4) {
                        setCellValue("12.45",row.createCell((short) 3),"Number");
                    }
                    if (j==5) {
                        setCellValue("2012-12-12 12:12:12wwwww",row.createCell((short) 4),"DateSS");
                    }
                }
            }

            //遍历数据赋值
            // 设置单元格样式 如果使用的非SXSSFWorkbook类则合并单元格方式如下
            /*sheet.addMergedRegion(new Region(3 + rsList.size(), (short) 0,3 + rsList.size(), (short) 5));*/
            //最后一行合并单元格
            sheet.addMergedRegion(new CellRangeAddress(50000, 50000, 0, 4));
            SXSSFRow endRow = sheet.createRow(50000);
            for(int i=0;i<5;i++){
                SXSSFCell endCell = endRow.createCell(i);
                if(i==0){
                    endCell.setCellValue("提示:这里是最后一行");
                }
                endCell.setCellStyle(cellBorderStyle);
            }
            String filePath = "d://";
            File o = new File(filePath);
            if (!o.exists()) {
                o.mkdirs();
            }
            //扩展名为这个可以存一百万多点数据，超过后会报错
            //使用xls为扩展名最大数据为65536行
            String fileType = ".xlsx";
            String fileName="222";
            //这种方法会直接将文件进行覆盖，文件中数据始终是最新的
            File file = new File(filePath+fileName+fileType);
            fileOut = new FileOutputStream(file);
            hssfWorkbook.write(fileOut);
        } finally {
            try {
                if(fileOut != null){
                    fileOut.close();
                }
                //这个也是个流要关闭
                if(hssfWorkbook != null){
                    hssfWorkbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void setCellByNumer(Double value, SXSSFCell hssfCell) {
        hssfCell.setCellValue(value);
        hssfCell.setCellStyle(dateCellStyleForNumber);
    }
    private static void setCellValues(String value, SXSSFCell hssfCell) {
        hssfCell.setCellValue((String) value);
        hssfCell.setCellStyle(cellBorderStyle);
    }
    private static void setCellNumberPer(Double value, SXSSFCell hssfCell) {
        hssfCell.setCellValue(value);
        hssfCell.setCellStyle(dateCellStyleForPer);
    }
    private static void setCellValue(String value, SXSSFCell hssfCell, String type){
        try{
            //如果是null的Object使用 String.valueOf进行转换会转换成一个 "null"的字符串
            if (null != value && !"".equals(value)) {
                if("String".equals(type)){
                    //将数据设置成字符串格式
                    setCellValues(value, hssfCell);
                }else if("Number".equals(type)){
                    //将数据设置成数字格式
                    setCellByNumer(Double.parseDouble(value), hssfCell);
                }else if("DateDD".equals(type)){
                    //将数据设置成日期格式
                    if(StringUtils.isNotEmpty(value) && value.length()>20){
                        String substring = value.substring(0,19);
                        Date paresDate = sdf.parse(substring);
                        hssfCell.setCellValue(paresDate);
                        hssfCell.setCellStyle(dateCellStyleForDD);
                    }
                }else if("DateSS".equals(type)){
                    //将数据设置成日期格式
                    if(StringUtils.isNotEmpty(value) && value.length()>20){
                        String substring = value.substring(0,19);
                        Date paresDate;
                        paresDate = sdf.parse(substring);
                        hssfCell.setCellValue(paresDate);
                        hssfCell.setCellStyle(dateCellStyleForSS);
                    }
                }else if("Per".equals(type)){
                    //将数字设置成百分号格式
                    setCellNumberPer(Double.parseDouble(value)/100, hssfCell);
                }
            } else {
                //设置边框
                hssfCell.setCellStyle(cellBorderStyle);
                hssfCell.setCellValue("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}