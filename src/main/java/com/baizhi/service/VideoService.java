package com.baizhi.service;

import com.baizhi.entity.Video;
import com.baizhi.po.VideoPO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface VideoService {

    //视频分页
    HashMap<String, Object> queryOneByPage(Integer page, Integer rows);

    String add(Video video);

    void uploadVdieosAliyuns(MultipartFile videoPath, String id, HttpServletRequest request);

    void update(Video video);

    HashMap<String, Object> delete(Video video);

    HashMap<String, Object> deleteAliyun(Video video);

    List<VideoPO> queryByReleaseTime();

}
