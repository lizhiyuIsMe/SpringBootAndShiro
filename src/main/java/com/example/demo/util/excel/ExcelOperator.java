package com.example.demo.util.excel;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ExcelOperator {

    private Logger logger = LoggerFactory.getLogger(ExcelOperator.class);

    //从第几行开始写数据
    private int fromRow;
    private boolean appned = false;

    /**
     * 只写一个sheet页
     */
    public boolean writeExcel(String writeExcelPath,String sheetName,String[][] content) throws BiffException {
        //获得第一页内容
        String[][][] conents = new String[1][][];
        conents[0] = content;
        return writeExcels(writeExcelPath,new String[]{sheetName},conents);
    }

    /**
     * 将 content 信息写入到多个sheet页
     * @param write_excel_path  要写入的文件路径
     * @param sheet_names    数组,每个sheet页的名字
     * @param content   要写入的内容，三维数组,
     *                  第一个[]指定存储的sheet页,
     *                  第二个[]指定存储的行,
     *                  第三个[]中存储一个一维数组,数组中存储着要存放的值
     * @return
     * @throws BiffException
     */
    public boolean writeExcels(String write_excel_path, String[] sheet_names,String[][][] content) throws BiffException {
        boolean result = true;
        if (write_excel_path != null && content != null) {
            WritableWorkbook wwb = null;
            try {
                //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
                if(appned){
                    File file = new File(write_excel_path);
                    if(file.exists()){
                        Workbook wb = Workbook.getWorkbook(file);
                        wwb = Workbook.createWorkbook(file,wb);
                    }else{
                        wwb = Workbook.createWorkbook(new File(write_excel_path));
                    }
                }else{
                    wwb = Workbook.createWorkbook(new File(write_excel_path));
                }
                String sheet_name = "sheet";
                //循环每个sheet页中的数据
                for(int i=0;i<content.length;i++){
                    String[][] sheet_data = content[i];
                    if(i<sheet_names.length){
                        sheet_name = sheet_names[i];
                    }else{
                        sheet_name += i;
                    }
                    if(!this.writeOneSheet(wwb, sheet_data, sheet_name, i)){
                        System.out.println("写入第"+(i+1)+"个sheet时出错了");
                        result = false;
                        break;
                    }
                }
                wwb.write();
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            }finally{
                try {
                    wwb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private boolean writeOneSheet(WritableWorkbook wwb, String[][] content, String sheetName, int index){
        boolean result = true;
        if(wwb!=null&&content!=null&&sheetName!=null){
            //创建一个可写入的工作表
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = null;
            if(appned){
                ws = wwb.getSheet(index);
            }else{
                ws = wwb.createSheet(sheetName, index);
            }
            //下面开始添加单元格
            for (int i = 0;i < content.length; i++) {
                System.out.println("第"+i+"行，共有"+content[i].length+"列");
                for (int j = 0; j < content[i].length; j++) {
                    String write_content = content[i][j];
                    //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
					/*Label labelC = new Label(j, i, "这是第" + (i + 1) + "行，第"
							+ (j + 1) + "列");*/
                    Label labelC = new Label(j, i, write_content);
                    try {
                        //将生成的单元格添加到工作表中
                        writeCellContent(ws,labelC,i,j);
                    } catch (RowsExceededException e) {
                        result = false;
                        e.printStackTrace();
                        break;
                    } catch (WriteException e) {
                        result = false;
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }else{
            System.out.println("writeOneSheet传入参数错误！");
        }
        return result;
    }

    public void writeCellContent(WritableSheet ws,Label labelC,int rowNum,int colNum) throws WriteException {
        //是否可以写入表格
        boolean canWrite = true;
        //首先判断是否在写入起始行后
        if(rowNum<(fromRow-1)){
            canWrite = false;
        }
        if(canWrite){
            //将生成的单元格添加到工作表中
            ws.addCell(labelC);
        }
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public boolean isAppned() {
        return appned;
    }

    public void setAppned(boolean appned) {
        this.appned = appned;
    }

    public static boolean checkIsNullOrEmpty(Object obj) {
        if (obj instanceof String) {
            return (obj == null || (((String) obj).equals("")) || (((String) obj).equals("null")) || ((String) obj).equals("undefined"));
        } else {
            return obj == null;
        }
    }
}
