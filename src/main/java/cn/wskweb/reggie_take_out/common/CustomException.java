package cn.wskweb.reggie_take_out.common;


/**
 * @创建人: wsk
 * @描述: 自定义异常类
 * @className（类名称）: CustomException
 * @创建时间: 12:16 2022/4/5
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
