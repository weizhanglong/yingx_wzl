package com.baizhi.dao;

import com.baizhi.entity.Video;
import java.util.List;

import com.baizhi.po.VideoPO;
import tk.mybatis.mapper.common.Mapper;

public interface VideoMapper extends Mapper<Video> {
    List<VideoPO> queryByReleaseTime();
}