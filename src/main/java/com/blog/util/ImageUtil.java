package com.blog.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

@Service
public class ImageUtil {
    //存放图片的绝对路径
    private final static String SAVE_IMAGE_PATH = "./upload_image/";
    private File lastFile;
    /**
     * 上传文章封面和上传文章内容的事务标志位
     */
    public boolean transaction = false;
    private String imageUrl = "";
    @Value("${server.port}")
    private int port;

    /**
     * 返回文件后缀
     */
    public String getImageExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();//获取原文件名
        int index = fileName.indexOf(".");
        return fileName.substring(index, fileName.length());
    }

    /**
     * 保存图片
     */
    @Async("asyncThreadPoolTaskExecutor")
    public Future<Boolean> saveImage(MultipartFile mFile, File file) {
        //查看文件夹是否存在，不存在则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            //使用此方法保存必须要绝对路径且文件夹必须已存在,否则报错
            mFile.transferTo(file.getAbsoluteFile());
            lastFile = file;
            return new AsyncResult<>(true);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return new AsyncResult<>(false);
        }
    }

    /**
     * 新文件名
     */
    public String getNewFileName(String suffix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        return date + UUID.randomUUID() + suffix;
    }

    /**
     * 返回图片保存地址
     */
    public String getNewImagePath(String name) {
        return SAVE_IMAGE_PATH + name;
    }

    public String getImageUrl(String newFileName) throws UnknownHostException {
        //保存数据库的图片路径为  url
        imageUrl = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/image/get?path=" + newFileName;
        return imageUrl;
    }

    /**
     * 当发布文章回滚时，调用此方法删除插入的图片
     */
    @Async("asyncThreadPoolTaskExecutor")
    public void deleteLastImage() {
        if (transaction && lastFile.exists()) {
            imageUrl = "";
            lastFile.delete();//不知为啥调用deleteOnExist无效
        }
    }

    /**
     * 根据Passage对象的cover属性获取其路径
     */
    public String getImagePath(String cover) {
        return cover.substring(cover.indexOf("="));
    }
}