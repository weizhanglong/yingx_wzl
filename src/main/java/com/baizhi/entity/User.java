package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    private String id;

    private String phone;

    private String wechat;

    @Column(name = "headimg")
    private String headImg;

    private String username;

    private String sign;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createtime")
    private Date createTime;

    private String sex;

    private String city;

}