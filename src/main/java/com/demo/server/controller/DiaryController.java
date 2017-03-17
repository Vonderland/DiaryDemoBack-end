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
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setData(true);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }
    @RequestMapping(value = "/allDiaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getAllDiaries(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = diaryService.getAllDiaries(token);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/diaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getDiaries(@RequestHeader(value = "Authorization", required = false) String token,
                             @RequestParam(value = "size", required = false, defaultValue = "20") String size,
                             @RequestParam(value = "timeCursor", required = false, defaultValue = "-1") String timeCursor) {
        long cursor = Long.parseLong(timeCursor);
        if (cursor < 0) {
            cursor = System.currentTimeMillis();
        }
        ResultMsg resultMsg = diaryService.getDiaries(token, Integer.parseInt(size), cursor);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/addDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addDiary(@RequestHeader(value = "Authorization", required = false) String token,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "description") String description,
                           @RequestParam(value = "eventTime") String eventTime,
                           @RequestParam(value = "isPrivate", required = false) boolean isPrivate,
                           @RequestParam(value = "picture", required = false) CommonsMultipartFile file,
                           HttpServletRequest request) {
        ResultMsg resultMsg;
        Diary diary;

        try {
            diary = generateDiary(title, description, file, eventTime, request, true, isPrivate);
        } catch (Exception ex) {
            resultMsg = new ResultMsg();
            resultMsg.setCode(104);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }

        resultMsg = diaryService.addDiary(token, diary);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/updateDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateDiary(@RequestHeader(value = "Authorization", required = false) String token,
                              @RequestParam(value = "id") String id,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "description") String description,
                              @RequestParam(value = "isPrivate", required = false) boolean isPrivate,
                              @RequestParam(value = "picture", required = false) CommonsMultipartFile file,
                              @RequestParam(value = "eventTime") String eventTime,
                              @RequestParam(value = "pictureChanged") String pictureChanged,//0：没改变，1：改变
                              HttpServletRequest request) {
        ResultMsg resultMsg;
        Diary diary;
        try {
            diary = generateDiary(title, description, file, eventTime, request, false, isPrivate);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMsg = new ResultMsg();
            resultMsg.setCode(104);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }
        diary.setId(Long.parseLong(id));
        if (Integer.parseInt(pictureChanged) == 0 ){
            resultMsg = diaryService.updateDiaryNotChangePic(token, diary, Long.parseLong(id));

        } else {
            resultMsg = diaryService.updateDiary(token, diary, Long.parseLong(id));
        }
        Gson gson = new Gson();
        return gson.toJson(resultMsg);

    }

    @RequestMapping(value = "/deleteDiary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteDiary(@RequestHeader(value = "Authorization", required = false) String token,
                              @RequestParam(value = "id") String id) {
        ResultMsg resultMsg = diaryService.deleteDiary(token, Long.parseLong(id));
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    private Diary generateDiary(String title, String description, CommonsMultipartFile file, String eventTime,
                               HttpServletRequest request, boolean add, boolean isPrivate) throws Exception{
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setDescription(description);
        diary.setEventTime(Long.parseLong(eventTime));
        diary.setPrivate(isPrivate);

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
