package com.example.demo.util.excel;

import jxl.read.biff.BiffException;


public class Testjxl {
    public static void main(String[] args) throws BiffException {
         //创建导入poi的自定义对象
         ExcelOperator excelOperator = new ExcelOperator();

         //从第几行开始写数据  不算标题, FromRow之前的数据是写不进去的
         excelOperator.setFromRow(2);
         //是否是将数据再次写入excel中
         //excelOperator.setAppned(true);

         //给数据赋值
         String[][][] totalData=getContent();

        //第一个参数是 文件名全路径+扩展名
        //第二个参数是 sheet页名称
        //第三个是 ArrayList<String[]> 内容
        //excelOperator.writeExcel("d://报表模板2.xls", "sheet页名", totalData);
        excelOperator.writeExcels("d://报表模板222.xls",
                new String[]{"第一个sheet名字","第二个sheet名字"}, totalData);
    }

    private static String[][][] getContent() {
        //总共有几个sheet页
        int SheetNum=2;
        //返回的数据
        String[][][] totalData = new String[SheetNum][][];
        //处理第一个sheet页
        processFirstSheet(totalData);
        //处理第二个sheet页
        processSecondSheet(totalData);
        return totalData;
    }

    private static void processSecondSheet(String[][][] sheets) {
        //这个sheet页有几行
        int firstSheetRow=2;
        String[][] sheet=new String[firstSheetRow][];
        sheets[1]=sheet;
        //这个sheet的第一行数据为空,第一行是标题在模板中
        sheet[0]=new String[]{""};
        //sheet的第二行数据
        sheet[1]=new String[]{"1","2"};
    }

    private static void processFirstSheet(String[][][] sheets) {
        //这个sheet页有几行
        int firstSheetRow=3;
        String[][] sheet=new String[firstSheetRow][];
        sheets[0]=sheet;
        //这个sheet的第一行数据为空,第一行是标题在模板中
        sheet[0]=new String[]{""};
        //sheet的第二行数据
        sheet[1]=new String[]{"1","2"};
        sheet[2]=new String[]{"1","2"};
    }
}
