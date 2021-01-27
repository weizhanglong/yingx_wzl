package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {

    @ExcelIgnore
    private String id;

    @Excel(name = "教师名",needMerge = true)
    private String name;

    //一个老师对应多个学生   FeadBack=学生
    @ExcelCollection(name = "教师对应的学生")
    List<Feedback> students;

}
