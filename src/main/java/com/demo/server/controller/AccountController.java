package com.demo.server.controller;

import com.demo.server.bean.ResultMsg;
import com.demo.server.bean.User;
import com.demo.server.service.AccountService;
import com.demo.utils.CipherUtil;
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
@Controller("accountController")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String register(@RequestParam(value = "email") String encodedEmail,
                           @RequestParam(value = "password") String encodedPassword,
                           @RequestParam(value = "nickName") String nickName,
                           @RequestParam(value = "gender") boolean gender,
                           @RequestParam(value = "avatar", required = false) CommonsMultipartFile file,
                           HttpServletRequest request) {
        ResultMsg resultMsg;
        User user = new User();
        if (file != null) {
            try {
                String avatar = ImageUtil.uploadAvatarImage(file, request);
                user.setAvatar(avatar);
            } catch (Exception ex) {
                resultMsg = new ResultMsg();
                resultMsg.setCode(104);
                Gson gson = new Gson();
                return gson.toJson(resultMsg);
            }
        }
        try {
            String email = CipherUtil.decodeData(encodedEmail);
            String password = CipherUtil.decodeData(encodedPassword);
            user.setNickName(nickName);
            user.setGender(gender);
            user.setEmail(email);
            user.setPassword(password);
        } catch (Exception ex) {
            resultMsg = new ResultMsg();
            resultMsg.setCode(109);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }
        resultMsg = accountService.register(user);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String login(@RequestParam(value = "email") String encodedEmail,
                        @RequestParam(value = "password") String encodedPassword) {
        ResultMsg resultMsg;
        String email, password;
        try {
            email = CipherUtil.decodeData(encodedEmail);
            password = CipherUtil.decodeData(encodedPassword);
        } catch (Exception ex) {
            resultMsg = new ResultMsg();
            resultMsg.setCode(109);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }
        resultMsg = accountService.login(email, password);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String resetPassword(@RequestHeader(value = "Authorization", required = false) String token,
                        @RequestParam(value = "password") String encodedPassword,
                                @RequestParam(value = "newPassword") String encodedNewPassword) {
        ResultMsg resultMsg;
        String password, newPassword;
        try {
            password = CipherUtil.decodeData(encodedPassword);
            newPassword = CipherUtil.decodeData(encodedNewPassword);
        } catch (Exception ex) {
            resultMsg = new ResultMsg();
            resultMsg.setCode(109);
            Gson gson = new Gson();
            return gson.toJson(resultMsg);
        }
        resultMsg = accountService.resetPassword(token, password, newPassword);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }
}
