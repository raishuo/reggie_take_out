package cn.wskweb.reggie_take_out.common;


/**
 * @创建人: wsk
 * @描述: 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @className（类名称）: BaseContext
 * @创建时间: 19:25 2022/4/3
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }



    public static Long getThreadLocal() {
        return threadLocal.get();
    }
}
