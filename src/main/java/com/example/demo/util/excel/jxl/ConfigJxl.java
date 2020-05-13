package com.example.demo.util.excel.jxl;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigJxl {
    //jxl api方便简单易操作 可以替代poi
    //效率比poi慢，比poi消耗内存少一些
    //不能导出大量数据 poi能
    //jxl 表格中可以存储图片,poi不能
    public ExcelOperator excelOperator(){
         return new ExcelOperator();
    }
}
