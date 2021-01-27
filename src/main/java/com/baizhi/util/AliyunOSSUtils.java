package com.baizhi.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class AliyunOSSUtils {

    // Endpoint以杭州为例，其它Region请按实际情况填写。 访问域名和数据中心  beijing=北京
    private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    private static String accessKeyId = "LTAI4GBmKmLsQZD6vq2whua6";
    private static String accessKeySecret ="H51c9X3qQAGApFAAoTTfmc0zmbEQJJ";


    /*
    * 文件上传
    * 参数：
    *   headImg(MultipartFile): 文件
    *   bucketName(String): 存储空间名
    *   objectName(String): 文件名
    * */
    public static void uploadFile(MultipartFile headImg,String bucketName, String objectName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 上传Byte数组。
        byte[] bytes = null;
        try {
            //将MultipartFile 类型的headImg文件转为字节数组
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
     * 视频截取帧并上传
     * 参数：
     *   bucketName(String): 存储空间名
     *   videoName(String): 要截取的视频名
     *   coverName(String): 保存的封面名
     * */
    public static URL interceptPhotoAndUpload(String bucketName, String videoName, String coverName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_1000,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, videoName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        System.out.println(signedUrl);

        // 上传网络流。
        InputStream inputStream = null;
        try {
            inputStream = new URL(signedUrl.toString()).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.putObject(bucketName, coverName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        return signedUrl;
    }

    /*
     * 删除文件
     * 参数：
     *   bucketName(String): 存储空间名
     *   objectName(String): 文件名
     * */
    public static void deleteFile(String bucketName, String objectName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void main(String[] args) {
        //视频截取
        /*URL url = interceptPhoto("yingx-2006", "video/1611198471483-火花.mp4");

        try {
            //文件上传
            uploadFileNetIO("yingx-2006","video/1611198471483-火花.jpg",url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        interceptPhotoAndUpload("yingx-wzl","video/1611198471483-火花.mp4","video/1611198471483-火花.jpg");
    }
}
