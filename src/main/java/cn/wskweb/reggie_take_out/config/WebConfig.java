package cn.wskweb.reggie_take_out.config;

import cn.wskweb.reggie_take_out.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


/**
 * @创建人: wsk
 * @描述: TODO
 * @创建时间: 16:06 2022/4/1
 * @返回值 null
 */

@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * @方法名: addResourceHandlers
     * @描述: 设置静态资源映射
     * @问题: 当资源文件下的静态资源不为默认值时进行以下操作
     * @返回值: null
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 路径带 "/backend/**" 将映射到 "classpath:/backend/" 目录
        log.info("开始静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }


    /**
     * @方法名: extendMessageConverters
     * @描述: 扩展mvc转换器
     * @param(传入参数):
     * @return:
     */

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置对象转换去，底层使Jackson将Java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将上面的消息转换器追加到mvc的转换器集合中
        converters.add(0, messageConverter);
    }
}
