package uw.code.center.conf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uw.auth.service.conf.AuthServiceAutoConfiguration;
import uw.code.center.template.TemplateHelper;
import uw.dao.conf.DaoAutoConfiguration;

import java.util.List;

/**
 * SaasAppAutoConfiguration
 *
 * 
 * @since 2018-05-17
 */
@Configuration
@AutoConfigureAfter({DaoAutoConfiguration.class, AuthServiceAutoConfiguration.class})
public class CodeCenterAutoConfiguration implements WebMvcConfigurer {


    /**
     * 移除XML消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(x -> x instanceof MappingJackson2XmlHttpMessageConverter);
    }

    /**
     * 注册代码生成器。
     *
     * @return
     */
    @Bean
    public CommandLineRunner configTemplateHelper() {
        return args -> {
            TemplateHelper.init();
        };
    }

}
