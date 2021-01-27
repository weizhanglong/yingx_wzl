package com.baizhi;

import com.baizhi.dao.FeedbackMapper;
import com.baizhi.entity.Feedback;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class PoiTests {

    @Resource
    FeedbackMapper feedbackMapper;

    @Test
    void testExport() {

        //创建一个Excel文档
        Workbook workbook = new HSSFWorkbook();

        //创建工作表   参数：工作表名    默认按照：sheet1,sheet2,...
        Sheet sheet = workbook.createSheet("反馈数据");

        //设置单元格宽度(列宽)  参数：列下标，列宽
        sheet.setColumnWidth(3,256*15);
        sheet.setColumnWidth(2,256*30);

        //合并单元格  参数;int firstRow(起始行), int lastRow()(结束行), int firstCol(起始单元格), int lastCol(结束单元格)
        CellRangeAddress cellAddresses = new CellRangeAddress(0,0,0,4);
        sheet.addMergedRegion(cellAddresses); //设置基于那个工作表使用

        //创建字体对象
        Font font = workbook.createFont();
        font.setBold(true);    //加粗
        font.setColor(IndexedColors.BLUE.getIndex()); //颜色
        font.setFontHeightInPoints((short)10);  //字号
        font.setFontName("方正粗黑宋简体");  //字体
        //font.setItalic(true);    //斜体
        //font.setUnderline(FontFormatting.U_SINGLE);  //下划线

        //创建样式对象
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setFont(font); //设置字体样式
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);  //文字居中

        //创建一行  参数：行下标(下标从0开始)
        Row row1 = sheet.createRow(0);
        //设置行高
        row1.setHeight((short) (20*20));
        //创建单元格   参数：单元格下标(下标从0开始)
        Cell cell1 = row1.createCell(0);
        //给单元格设置内容
        cell1.setCellValue("应学App系统反馈数据");
        //设置字体样式
        cell1.setCellStyle(cellStyle1);

        //设置标题内容
        String[] title={"ID","标题","内容","用户id"};

        //创建第二行
        Row row2 = sheet.createRow(1);

        //遍历标题内容
        for (int i = 0; i < title.length; i++) {

            //创建一个单元格
            Cell cell = row2.createCell(i);
            //设置内容
            cell.setCellValue(title[i]);
        }

        //查询数据数据
        List<Feedback> feedbacks = feedbackMapper.selectAll();

        //创建日期对象
        DataFormat dataFormat = workbook.createDataFormat();
        //设置日期格式
        short format = dataFormat.getFormat("yyyy-MM-dd");

        //创建样式对象
        CellStyle cellStyle = workbook.createCellStyle();
        //将日期样式交给样式对象
        cellStyle.setDataFormat(format);

        //遍历数据集合
        for (int i = 0; i < feedbacks.size(); i++) {

            //创建一行
            Row row = sheet.createRow(i+2);
            //创建一个单元格
            /*Cell cell0 = row.createCell(0);
            Feedback feedback = feedbacks.get(0);
            cell0.setCellValue(feedback.getId());*/

            //创建一个单元格并赋值
            row.createCell(0).setCellValue(feedbacks.get(i).getId());
            row.createCell(1).setCellValue(feedbacks.get(i).getTitle());
            row.createCell(2).setCellValue(feedbacks.get(i).getContent());
            row.createCell(3).setCellValue(feedbacks.get(i).getUserId());

        }

        //导出单元格
        try {
            workbook.write(new FileOutputStream(new File("D://2008Excel.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testinport(){
        try {
            //获取一个Excel文档
            Workbook workbook = new HSSFWorkbook(new FileInputStream(new File("D://2008Excel.xls")));

            //获取工作表
            Sheet sheet = workbook.getSheet("反馈数据");

            //获取最后一行(最后一行数据行下标)  5
            int lastRowNum = sheet.getLastRowNum();

            //获取除了标题行以后的所有数据行
            for (int i = 2; i <= lastRowNum; i++) {
                //获取行
                Row row = sheet.getRow(i);

                //获取行并获取内容
                /*Cell cell = row.getCell(0);
                String id = cell.getStringCellValue();*/

                //
                /* 数据类型与API
                * String:  StringCellValue
                * Date:    DateCellValue
                * int:     NumericCellValue
                * double:  NumericCellValue
                * boolean: BooleanCellValue
                * */
                String id = row.getCell(0).getStringCellValue();
                String title = row.getCell(1).getStringCellValue();
                String content = row.getCell(2).getStringCellValue();
                String userId = row.getCell(3).getStringCellValue();

                Feedback feedback = new Feedback(id, title, content,userId);

                System.out.println("====导入数据== "+feedback);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
