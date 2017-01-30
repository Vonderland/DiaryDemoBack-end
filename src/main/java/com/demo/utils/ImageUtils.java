package com.demo.utils;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Vonderland on 2017/1/30.
 */
public class ImageUtils {
    private static final String IMAGE_PATH = "files/image/";
    public static String uploadDiaryImage(CommonsMultipartFile file, HttpServletRequest request) throws Exception {
        String path = IMAGE_PATH + "diaryImage/";
        String contentType = file.getContentType();
        String[] type = contentType.split("/");
        if (!"image".equals(type[0])) {
            throw new Exception("wrong file type");
        }
        // 获得后缀名
        String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        path = path + System.currentTimeMillis() + suffixName;
        System.out.println(request.getServletContext().getRealPath("/") + path);
        // 暂时安排在一个固定的文件夹中存放
        file.transferTo(new File(request.getServletContext().getRealPath("/") + path));

        System.out.println(request.getServletContext().getRealPath("/") + path);
        return path;
    }
}
