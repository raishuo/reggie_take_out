package cn.wskweb.reggie_take_out.filter;


import cn.wskweb.reggie_take_out.common.BaseContext;
import cn.wskweb.reggie_take_out.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @创建人: wsk
 * @className（类名称）: LoginCheckFilter
 * @描述: 检查用户是否完成登录（过滤器）
 * @创建时间: 9:39 2022/4/2
 * @返回值 null
 */


// @WebFilter注解详细可见 ： https://www.cnblogs.com/ooo0/p/10360952.html
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    // AntPathMatcher路径匹配器 详细见 ： https://blog.csdn.net/qq_16992475/article/details/122877202
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获得本次请求的URI
        String uri = request.getRequestURI();

        String[] urls = new String[]{       // 放行路径
                "/employee/login", "/employee/logout", "/backend/**", "/front/**", "/user/sendMsg", "/user/login"};

        // 判断本次请求是否需要处理
        boolean check = check(urls, uri);// 调用路径匹配方法


        // 如果不需要处理，直接放行
        if (check) {
            filterChain.doFilter(request, response);    // 拦截放行
            return;
        }


        // 判断登录状态，如果已经登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            // getAttribute("employee") 为登录时传入Session中的值
            Long employee = (Long) request.getSession().getAttribute("employee");
            BaseContext.setThreadLocal(employee);   // 使用工具类将登录id存入工具类
            filterChain.doFilter(request, response);    // 拦截放行
            return;
        }



        // 判断移动端登录状态，如果已经登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {    // getAttribute("user") 为登录时传入Session中的值
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setThreadLocal(userId);   // 使用工具类将登录id存入工具类
            filterChain.doFilter(request, response);    // 拦截放行
            return;
        }


        // 如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));  // backend/js/request.js 决定

        log.info("拦截到请求：{}", uri);
    }


    /**
     * @方法名: check
     * @描述: 路径匹配，检查本次请求是否需要放行
     * @param(传入参数): uris 放行路径集合
     * @param(传入参数): uri  本次访问路径
     * @return: boolean
     */
    public boolean check(String[] uris, String uri) {
        for (String url : uris) {
            boolean match = PATH_MATCHER.match(url, uri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
