package com.demo.server.controller;

import com.demo.server.bean.Diary;
import com.demo.server.bean.ResultMsg;
import com.demo.server.service.DiaryService;
import com.demo.utils.ImageUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

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
        resultMsg = diaryService.updateDiary(diary, Long.parseLong(id));
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
            String path = ImageUtils.uploadDiaryImage(file, request);
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
