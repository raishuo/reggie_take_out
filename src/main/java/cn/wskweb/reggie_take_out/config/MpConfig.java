package cn.wskweb.reggie_take_out.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @创建人: wsk
 * @描述: MybatisPlus的分页插件
 * @className（类名称）: MpConfig
 * @创建时间: 21:06 2022/4/2
 * @返回值: null
 */
@Configuration
public class MpConfig {


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //1 创建MybatisPlusInterceptor拦截器对象
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        //2 添加分页拦截器
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }

}
