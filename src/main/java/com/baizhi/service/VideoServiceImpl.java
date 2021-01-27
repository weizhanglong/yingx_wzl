package com.baizhi.service;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.dao.LikeMapper;
import com.baizhi.dao.PlayMapper;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.*;
import com.baizhi.po.VideoPO;
import com.baizhi.util.AliyunOSSUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service("videoService")
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class VideoServiceImpl implements VideoService{

    @Resource
    VideoMapper vm;

    @Resource
    HttpSession session;

    @Resource
    PlayMapper playMapper;

    @Resource
    LikeMapper likeMapper;

    @AddCache
    @Override
    public HashMap<String, Object> queryOneByPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();

        //当前页   page
        map.put("page",page);

        //总条数   records
        VideoExample example = new VideoExample();
        int records = vm.selectCountByExample(example);
        map.put("records",records);

        //总页数   total
        //总页数=总条数/每页展示条数   有余数加一页
        Integer total=records/rows==0?records/rows:records/rows+1;
        map.put("total",total);

        //数据    rows   参数：略过几条，要几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        List<Video> videos = vm.selectByRowBounds(new Video(), rowBounds);

        //便利视频集合
        for (Video video : videos) {

            //获取播放次数 yx_play  根据视频播放id查询视频播放数据
            //获取视频id
            String id = video.getId();
            //设置查询条件
            PlayExample playExample = new PlayExample();
            playExample.createCriteria().andVideoIdEqualTo(id);
            //查询播放数据
            Play play = playMapper.selectOneByExample(playExample);

            if(play!=null){
                //将查询出来的播放次数放入视频对象中
                video.setPlayCount(play.getPlayCount());
            }else{
                //将查询出来的播放次数放入视频对象中
                video.setPlayCount(0);
            }

            //获取点赞次数
            //设置点赞条件对象
            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andVideoIdEqualTo(id);
            int count = likeMapper.selectCountByExample(likeExample);
            video.setLikeCount(count); //设置点赞次数

        }

        map.put("rows",videos);

        return map;
    }

    @AddLog(description = "添加视频信息")
    @Override
    public String add(Video video) {

        video.setId(UUID.randomUUID().toString());
        video.setCreateTime(new Date());
        video.setStatus("0");
        video.setCategoryId("1");
        video.setUserId("1");

        //   C:\fakepath\11.jpg
        System.out.println("video  service "+video);
        //执行添加
        vm.insertSelective(video);

        //将id返回
        return video.getId();
    }

    /*
     * 1.文件上传阿里云
     * 2.通过java截取视频第一帧图片
     * 3.上传封面图片
     * 4.删除本地封面图
     * 5.修改数据
     * */
    @Override
    public void uploadVdieosAliyuns(MultipartFile videoPath, String id, HttpServletRequest request) {

        String bucketName="yingx-wzl";

        //1.文件上传至阿里云

        //获取文件名
        String filename = videoPath.getOriginalFilename();
        //文件名拼接时间戳
        String newName=new Date().getTime()+"-"+filename;
        //设置文件上传的文件夹和文件名
        String videoName="video/"+newName;

        /*
         * 文件上传
         * 参数：
         *   headImg(MultipartFile): 文件
         *   bucketName(String): 存储空间名
         *   objectName(String): 文件名
         * */
        AliyunOSSUtils.uploadFile(videoPath,bucketName,videoName);

        //2.截取视频第一帧作为封面并上传
        //截取视频名
        String[] split = newName.split("\\.");
        //拼接封面名
        String coverName="cover/"+split[0]+".jpg";

        /*
         * 视频截取帧并上传
         * 参数：
         *   bucketName(String): 存储空间名
         *   videoName(String): 要截取的视频名
         *   coverName(String): 保存的封面名
         * */
        AliyunOSSUtils.interceptPhotoAndUpload(bucketName,videoName,coverName);

        //3.修改图片路径
        //修改的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();
        video.setCoverPath("http://yingx-wzl.oss-cn-beijing.aliyuncs.com/"+coverName); //设置封面
        video.setVideoPath("http://yingx-wzl.oss-cn-beijing.aliyuncs.com/"+videoName); //设置视频地址

        //修改
        vm.updateByExampleSelective(video,example);
    }

    @AddLog(description = "修改视频信息")
    @Override
    public void update(Video video) {
        System.out.println("修改：" + video);
        vm.updateByPrimaryKeySelective(video);
    }

    @AddLog(description = "删除视频信息")
    @Override
    public HashMap<String, Object> delete(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {

            //设置条件
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            //根据id查询视频数据
            Video videos = vm.selectOneByExample(example);

            //删除数据
            vm.deleteByExample(example);

            //删除本地文件
            //1.获取文件上传的路径  根据相对路径获取绝对路径
            String realPath = session.getServletContext().getRealPath("/upload/video/"+videos.getVideoPath());
            File file = new File(realPath);
            //判断是一个文件，并且文件存在
            if(file.isFile()&&file.exists()){
                //删除文件
                boolean isDel = file.delete();
                System.out.println("删除："+isDel);
            }

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    @Override
    public HashMap<String, Object> deleteAliyun(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //设置条件
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            //1.根据id查询视频数据
            Video videos = vm.selectOneByExample(example);

            //2.删除数据
            vm.deleteByExample(example);

            /*
             * 3.删除文件
             * 参数：
             *   bucketName(String): 存储空间名
             *   objectName(String): 文件名
             * */
            //字符串替换
            String videoName = videos.getVideoPath().replace("http://yingx-wzl.oss-cn-beijing.aliyuncs.com/", "");
            String coverName = videos.getCoverPath().replace("http://yingx-wzl.oss-cn-beijing.aliyuncs.com/", "");

            //删除封面
            AliyunOSSUtils.deleteFile("yingx-wzl",videoName);
            //删除视频
            AliyunOSSUtils.deleteFile("yingx-wzl",coverName);

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    @Override
    public List<VideoPO> queryByReleaseTime() {
        List<VideoPO> videoPOS = vm.queryByReleaseTime();

        for (VideoPO videoPO : videoPOS) {
            //获取播放次数 yx_play  根据视频播放id查询视频播放数据
            //获取视频id
            String id = videoPO.getId();

            //获取点赞次数
            //设置点赞条件对象
            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andVideoIdEqualTo(id);
            int count = likeMapper.selectCountByExample(likeExample);
            videoPO.setLikeCount(count); //设置点赞次数
        }
        return videoPOS;
    }

}
