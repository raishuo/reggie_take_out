package cn.wskweb.reggie_take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @创建人: wsk
 * @描述: 全局异常处理
 * @className（类名称）: GlobalExceptionHandler
 * @创建时间: 14:43 2022/4/2
 * @返回值: null
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})   // 拦截带此注解的类
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * @方法名: exceptionHandler
     * @描述: 异常处理方法 Controller遇到此类异常统一处理（SQLIntegrityConstraintViolationException）
     * @param(传入参数):
     * @return:
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        // 判断元素是否在异常信息中
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");// 按空格分割异常信息
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }


    /**
     * @方法名:  exceptionHandler
     * @描述: 捕获自己抛出的业务类异常
     * @param:
     * @return:
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {


        return R.error(ex.getMessage());
    }
}
