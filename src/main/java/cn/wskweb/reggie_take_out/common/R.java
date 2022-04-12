package cn.wskweb.reggie_take_out.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


/**
 * @创建人: wsk
 * @描述: 通用返回结果，服务器响应的数据最终都会封装成此对象
 * @创建时间: 16:56 2022/4/1
 * @返回值 r
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map<String, Object> map = new HashMap<>(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
