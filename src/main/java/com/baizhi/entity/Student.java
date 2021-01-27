package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Excel(name = "ID")
    private String id;

    @Excel(name = "名字")
    private String name;

    @Excel(name = "头像",type = 2)
    private String headImg;

    @Excel(name = "用户年龄")
    private Integer age;

    @Excel(name = "生日",width = 20,format = "yyyy年MM月dd日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


}