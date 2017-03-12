package com.demo.server.controller;

import com.demo.server.bean.ResultMsg;
import com.demo.server.service.ProfileService;
import com.demo.utils.ImageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Controller("profileController")
public class ProfileController {
    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(value = "/userProfile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String userProfile(@RequestHeader("Authorization") String token) {
        ResultMsg resultMsg;
        resultMsg = profileService.getUser(token);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/updateNickName", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNickName(@RequestHeader("Authorization") String token,
                                 @RequestParam(value = "nickName") String nickName) {
        ResultMsg resultMsg;
        resultMsg = profileService.updateNickName(token, nickName);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateAvatar(@RequestHeader("Authorization") String token,
                               @RequestParam(value = "avatar", required = false) CommonsMultipartFile file,
                               HttpServletRequest request) {
        ResultMsg resultMsg;
        String avatar = "";
        if (file != null) {
            try {
                avatar = ImageUtil.uploadDiaryImage(file, request);
            } catch (Exception ex) {
                resultMsg = new ResultMsg();
                resultMsg.setCode(104);
                Gson gson = new Gson();
                return gson.toJson(resultMsg);
            }
        }
        resultMsg = profileService.updateAvatar(token, avatar);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }
}
