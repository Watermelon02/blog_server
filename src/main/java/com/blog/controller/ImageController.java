package com.blog.controller;

import com.blog.util.ImageUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/image")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class ImageController {

    @Autowired
    private ImageUtil imageUtil;

    @RequiresRoles(value = {})
    @PostMapping(value = "/upload")
    public String upload_image(@RequestParam("file") MultipartFile mFile) throws UnknownHostException, ExecutionException, InterruptedException {
        imageUtil.transaction = true;
        //获取文件后缀
        String suffixName = imageUtil.getImagePath(mFile);
        //生成新文件名称
        String newFileName = imageUtil.getNewFileName(suffixName);
        //保存文件
        File file = new File(imageUtil.getNewImagePath(newFileName));
        boolean state = imageUtil.saveImage(mFile, file).get();
        if (state) {
            return imageUtil.getImageUrl(newFileName);
        } else {
            return "";
        }
    }

    @GetMapping("/get")
    public String showImage(@RequestParam("path") String path, HttpServletResponse response) {
        File file = new File("./upload_image/" + path);
        byte[] bytes = new byte[1024];
        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file)) {
            while ((fis.read(bytes)) != -1) {
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}


