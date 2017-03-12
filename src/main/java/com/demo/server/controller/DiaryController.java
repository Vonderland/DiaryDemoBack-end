package com.demo.server.controller;

import com.demo.server.bean.Diary;
import com.demo.server.bean.ResultMsg;
import com.demo.server.service.DiaryService;
import com.demo.utils.CipherUtil;
import com.demo.utils.ImageUtil;
import com.demo.utils.TokenUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * Created by Vonderland on 2017/1/29.
 */
@Controller("diaryController")
public class DiaryController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String test() {
        return TokenUtil.getUidFromToken(TokenUtil.generateToken(123)) + "";
    }
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String forgetPassword() {
        String result;
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        try {
            // 创建邮件对象
            Message msg = new MimeMessage(session);
            msg.setSubject("重置密码");
            // 设置邮件内容
            String randomPassWord;
            for (int i = 0; i < 6; i ++) {

            }
            msg.setText("您的密码已被重置为");
            // 设置发件人
            msg.setFrom(new InternetAddress("us_diary_service@163.com"));

            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect("us_diary_service", "87569530imayday");
            // 发送邮件
            transport.sendMessage(msg, new Address[] {new InternetAddress("")});
            // 关闭连接
            transport.close();
            result = "success";
        } catch (Exception e) {
            System.out.println(e);
            result = "failure";
        }
        return result;
    }

    @RequestMapping(value = "/allDiaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getAllDiaries() {
        ResultMsg resultMsg = diaryService.getAllDiaries();
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/diaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getDiaries(@RequestParam(value = "size", required = false, defaultValue = "20") String size,
                             @RequestParam(value = "timeCursor", required = false, defaultValue = "-1") String timeCursor) {
        long cursor = Long.parseLong(timeCursor);
        if (cursor < 0) {
            cursor = System.currentTimeMillis();
        }
        ResultMsg resultMsg = diaryService.getDiaries(Integer.parseInt(size), cursor);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/addDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addDiary(@RequestParam(value = "title") String title,
                           @RequestParam(value = "description") String description,
                           @RequestParam(value = "eventTime") String eventTime,
                           @RequestParam(value = "picture", required = false) CommonsMultipartFile file,
                           HttpServletRequest request) {
        ResultMsg resultMsg;
        Diary diary;

        try {
            diary = generateDiary(title, description, file, eventTime, request, true);
        } catch (Exception ex) {
            resultMsg = new ResultMsg();
            resultMsg.setCode(104);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }

        resultMsg = diaryService.addDiary(diary);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/updateDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateDiary(@RequestParam(value = "id") String id,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "description") String description,
                              @RequestParam(value = "picture", required = false) CommonsMultipartFile file,
                              @RequestParam(value = "eventTime") String eventTime,
                              @RequestParam(value = "pictureChanged") String pictureChanged,//0：没改变，1：改变
                              HttpServletRequest request) {
        ResultMsg resultMsg;
        Diary diary;
        try {
            diary = generateDiary(title, description, file, eventTime, request, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMsg = new ResultMsg();
            resultMsg.setCode(104);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }
        diary.setId(Long.parseLong(id));
        if (Integer.parseInt(pictureChanged) == 0 ){
            resultMsg = diaryService.updateDiaryNotChangePic(diary, Long.parseLong(id));

        } else {
            resultMsg = diaryService.updateDiary(diary, Long.parseLong(id));
        }
        Gson gson = new Gson();
        return gson.toJson(resultMsg);

    }

    @RequestMapping(value = "/deleteDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteDiary(@RequestParam(value = "id") String id) {
        ResultMsg resultMsg = diaryService.deleteDiary(Long.parseLong(id));
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    private Diary generateDiary(String title, String description, CommonsMultipartFile file, String eventTime,
                               HttpServletRequest request, boolean add) throws Exception{
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setDescription(description);
        diary.setEventTime(Long.parseLong(eventTime));

        if (file != null) {
            String path = ImageUtil.uploadDiaryImage(file, request);
            diary.setUrl(path);
        }

        //TODO:注意，这里可能会有时区不一致的问题
        Long now = System.currentTimeMillis();
        if (add) {
            diary.setCreateTime(now);
        }
        diary.setUpdateTime(now);
        return diary;
    }
}
