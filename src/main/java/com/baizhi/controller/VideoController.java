package com.baizhi.controller;


import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("video")
public class VideoController {

    @Resource
    VideoService vs;

    @ResponseBody
    @RequestMapping("queryAllPage")
    public HashMap<String, Object> queryAllPage(Integer page, Integer rows) {
        HashMap<String, Object> map = vs.queryOneByPage(page, rows);
        return map;
    }


    @ResponseBody
    @RequestMapping("edit")
    public Object edit(Video video, String oper) {

        if (oper.equals("add")) {
            String id = vs.add(video);
            return id;
        }

        if (oper.equals("edit")) {
            vs.update(video);
        }

        if (oper.equals("del")) {
            HashMap<String, Object> map = vs.deleteAliyun(video);
            return map;
        }

        return "";
    }

    @ResponseBody
    @RequestMapping("uploadVdieo")
    public void uploadVdieo(MultipartFile videoPath, String id, HttpServletRequest request) {
        vs.uploadVdieosAliyuns(videoPath, id, request);
    }

}
