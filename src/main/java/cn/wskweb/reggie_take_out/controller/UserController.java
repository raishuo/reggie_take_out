package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.entity.User;
import cn.wskweb.reggie_take_out.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.ValidateCodeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code ={}", code);
            // 调用阿里云提供的API完成短信发送
//            com.itheima.reggie.utils.SMSUtils.sendMessage("瑞吉外卖","",phone, code);

            // 将需要生成的验证码保存到Session中
            session.setAttribute(phone, code);

            return R.success("短信发送成功");
        }

        return R.error("短信发送失败");
    }


    @PostMapping("login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();

        String codeInSession = session.getAttribute(phone).toString();

        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> userServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userServiceLambdaQueryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(userServiceLambdaQueryWrapper);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            return R.success(user);
        }


        return R.error("登录失败");
    }
}
