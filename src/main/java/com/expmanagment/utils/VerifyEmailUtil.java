package com.expmanagment.utils;

import com.alibaba.fastjson.JSONObject;
import com.expmanagment.entity.VerificationCodeEntity;
import com.expmanagment.repo.UserRepo;
import com.expmanagment.repo.VerificationCodeRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import com.expmanagment.entity.UserEntity;
import com.expmanagment.exception.UserNotFoundException;

import com.expmanagment.exception.AlreadyExistException;
import com.expmanagment.exception.MyNotFoundException;
import com.expmanagment.exception.ExpiredException;
import com.expmanagment.exception.NotCorrectException;

@Component
@AllArgsConstructor
@Slf4j
public class VerifyEmailUtil {
    private final SendEmailUtil sendEmailUtil;
    private final TemplateEngine templateEngine;

    private final VerificationCodeRepo verificationCodeRepo;
    private final UserRepo userRepo;

    // 随机生成大写英文字母
    private char getRandomCapitalLetter() {
        int i = (int)(Math.random() * 25);
        int j = i + 'A';
        char letter = (char) j;
        return letter;
    }

    public String getEmailCode() {
        StringBuilder code = new StringBuilder();
        int dir;
        for (int i = 0; i < 8; i++) {
            dir = (Math.random() > 0.5) ? 1 : 0;
            if (dir == 1) {
                //拼数字
                code.append((int) (Math.random() * 9));
            } else {
                //拼字母
                code.append(getRandomCapitalLetter());
            }
        }
        return code.toString();
    }

    public String getPhoneCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 9));
        }
        return code.toString();
    }

    /**
     * 发送验证码至相应邮箱
     * @param to 收件人
     * @param title 主题
     * @param verifyCode 内容
     */

    public void sendCode2Email(String to, String title, String verifyCode) {
        //把验证码放到thymeleaf模板中
        Context context = new Context();
        context.setVariable("verifyCode", verifyCode);
        String emailContent = templateEngine.process("emailTemplate", context);
        //发送邮件
        sendEmailUtil.sendHtmlMail(to, title, emailContent);
    }

    /**
     * 发送邮件并把验证码放入session
     * @param request
     * @param email
     * @return
     */
    public JSONObject sendMail(HttpServletRequest request, String email)
    {
        String verifyCode = getPhoneCode();
        sendCode2Email(email, "SimpleTest", verifyCode);
        HttpSession session = request.getSession(false);
        if (session == null) {
            //创建session，并拿到JSESSIONID
            session = request.getSession();
        }
        //设置session有效时间，默认是1800s
        session.setMaxInactiveInterval(900);
        //将验证码放入session中
        session.setAttribute("verifyCode", verifyCode);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功发送");
        return json;
    }

    public JSONObject sendVerificationEmail(String email, Integer userId) {
        String verifyCode = getPhoneCode();
        sendCode2Email(email, "【实验教学管理系统】账户激活", verifyCode);
        if (verificationCodeRepo.existsById(userId)) {
            verificationCodeRepo.deleteById(userId);
        }
        VerificationCodeEntity codeRecord = new VerificationCodeEntity();
        codeRecord.setUserId(userId);
        codeRecord.setCode(verifyCode);
        // 30分钟后到期
        Timestamp expiresTime = Timestamp.valueOf(LocalDateTime.now().plusMinutes(30));
        log.info("失效时间：" + expiresTime);
        codeRecord.setExpiresAt(expiresTime);
        verificationCodeRepo.save(codeRecord);


        JSONObject sendingResult = new JSONObject();
        sendingResult.put("code", 200);
        sendingResult.put("msg", "成功发送验证邮件");
        return sendingResult;
    }

    /**
     * 检查验证码
     * @param inputCode
     * @param request
     * @return
     */
    public JSONObject checkCode(String inputCode, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String verifyCode = (String) session.getAttribute("verifyCode");
        if (inputCode.equals(verifyCode)){
            //主动使session无效
            session.invalidate();
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "验证码正确");
            return json;
        }
        JSONObject json = new JSONObject();
        json.put("code", 500);
        json.put("message", "验证码错误");
        return json;
    }

    public JSONObject checkVerificationCode(String code, Integer userId) {
        Optional<VerificationCodeEntity> record = verificationCodeRepo.findById(userId);
        if (record.isEmpty()) {
            throw new MyNotFoundException("没有发送验证邮件的记录");
        }
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (now.after(record.get().getExpiresAt())) {
            throw new ExpiredException("验证码已过期");
        } else if (!record.get().getCode().equals(code)) {
            throw new NotCorrectException("验证码不正确");
        } else {
            Optional<UserEntity> userOptional = userRepo.findById(userId);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("该用户不存在");
            } else if (userOptional.get().getIsActivated() == 1) {
                throw new AlreadyExistException("该用户已激活");
            } else {
                userOptional.get().setIsActivated(1);
                userRepo.save(userOptional.get());

                // 删除record的记录
                verificationCodeRepo.deleteById(userId);
                JSONObject verificationResult = new JSONObject();
                verificationResult.put("code", 200);
                verificationResult.put("msg", "激活成功");
                return verificationResult;
            }
        }
    }


    /**
     *
     */
    public boolean checkVerificationCode(String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String verifyCode = (String)session.getAttribute("verifyCode");
        log.info("用户提供的验证码：" + code);
        log.info("正确的验证码： " + verifyCode);
        if (code.equals(verifyCode)) {
            log.info("用户激活邮箱：验证码验证成功");
            session.invalidate();
            return true;
        } else {
            log.info("用户激活邮箱：验证码验证失败");
            return false;
        }
    }
}
