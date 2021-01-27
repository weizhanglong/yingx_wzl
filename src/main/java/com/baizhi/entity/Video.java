package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video implements Serializable {
    @Id
    private String id;

    private String title;

    private String description;

    @Column(name = "videopath")
    private String videoPath;

    @Column(name = "coverpath")
    private String coverPath;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createtime")
    private Date createTime;

    @Column(name = "categoryid")
    private String categoryId;

    @Column(name = "userid")
    private String userId;

    @Column(name = "groupid")
    private String groupId;

    @Transient
    private Integer playCount; //播放次数

    @Transient
    private Integer likeCount; //点赞次数




}