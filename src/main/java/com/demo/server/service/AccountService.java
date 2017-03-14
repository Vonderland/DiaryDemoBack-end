package com.demo.server.service;

import com.demo.server.bean.Authorization;
import com.demo.server.bean.ResultMsg;
import com.demo.server.bean.User;
import com.demo.server.dao.AuthDao;
import com.demo.server.dao.UserDao;
import com.demo.utils.CipherUtil;
import com.demo.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Service("accountService")
public class AccountService {
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;

    public ResultMsg register(User user) {
        ResultMsg resultMsg = new ResultMsg();
        if (user == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("user is null");
            return resultMsg;
        }
        User existUser = userDao.selectUserByEmail(user.getEmail());
        if (existUser != null) {
            resultMsg.setCode(106);
            resultMsg.setMessage("the email address has been used");
            return resultMsg;
        }
        try {
            int rowCount = userDao.insertUser(user);
            System.out.println("rowCount = " + rowCount);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                long uid = user.getUid();
                System.out.println("uid = " + uid);
                String token = TokenUtil.generateToken(uid);
                Authorization existAuth = authDao.selectAuthByUid(uid);
                Authorization auth = new Authorization();
                auth.setUid(uid);
                auth.setToken(token);
                if (existAuth == null) {
                    int count = authDao.insertAuth(auth);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                } else {
                    int count = authDao.updateAuth(uid, token);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("register " + e);
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg login(String email, String password) {
        ResultMsg resultMsg = new ResultMsg();
        User user = userDao.selectUserByEmail(email);
        if (user == null) {
            resultMsg.setCode(107);
        } else {
            if (password.equals(user.getPassword())) {
                Long uid = user.getUid();
                Authorization auth = authDao.selectAuthByUid(uid);
                if (auth == null) {
                    auth = new Authorization();
                    auth.setUid(uid);
                    String token = TokenUtil.generateToken(uid);
                    auth.setToken(token);
                    int count = authDao.insertAuth(auth);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setData(auth);
                }
            } else {
                resultMsg.setCode(110);
            }
        }
        return resultMsg;
    }

    public ResultMsg resetPassword(String token, String password, String newPassword) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        }
        if (password.equals(user.getPassword())) {
            int rowCount = userDao.updatePassword(uid, newPassword);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                String newToken = TokenUtil.generateToken(uid);
                Authorization auth = new Authorization();
                auth.setToken(newToken);
                auth.setUid(uid);
                int count = authDao.updateAuth(uid, newToken);
                if (count == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setData(auth);
                }
            }
        } else {
            resultMsg.setCode(110);
        }
        return resultMsg;
    }

    public ResultMsg forgetPassword(String email) {
        User user = userDao.selectUserByEmail(email);
        ResultMsg resultMsg = new ResultMsg();
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        }
        String randomPassWord = TokenUtil.generateRandomPassword();
        String encoded = CipherUtil.encodeDataMD5(randomPassWord);
        if (encoded.equals("")) {
            resultMsg.setCode(111);
            return resultMsg;
        }
        long uid = user.getUid();
        int rowCount = userDao.updatePassword(uid, encoded);
        if (rowCount == 0) {
            resultMsg.setCode(102);
        } else {
            String newToken = TokenUtil.generateToken(uid);
            Authorization auth = new Authorization();
            auth.setToken(newToken);
            auth.setUid(uid);
            int count = authDao.updateAuth(uid, newToken);
            if (count == 0) {
                resultMsg.setCode(102);
            } else {
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

                    msg.setText("您的密码已被重置为以下八位数密码：" + randomPassWord
                            + "。登录后请尽快修改密码，以防账号被盗。");
                    // 设置发件人
                    msg.setFrom(new InternetAddress("us_diary_service@163.com"));

                    Transport transport = session.getTransport();
                    // 连接邮件服务器
                    transport.connect("us_diary_service", "diary2017");
                    // 发送邮件
                    transport.sendMessage(msg, new Address[] {new InternetAddress(email)});
                    // 关闭连接
                    transport.close();
                    resultMsg.setCode(100);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    resultMsg.setCode(111);
                }

            }
        }
        return resultMsg;
    }
    private boolean checkTokenInvalidation(String token, ResultMsg resultMsg) {
        if (token == null) {
            resultMsg.setCode(108);
            return false;
        }
        long uid = TokenUtil.getUidFromToken(token);
        Authorization authorization = authDao.selectAuthByUid(uid);
        if (authorization == null) {
            resultMsg.setCode(107);
            return false;
        } else if (!token.equals(authorization.getToken())) {
            resultMsg.setCode(105);
            return false;
        }
        return true;
    }
}
