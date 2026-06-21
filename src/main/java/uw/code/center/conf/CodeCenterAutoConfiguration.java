package uw.code.center.conf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uw.auth.service.conf.AuthServiceAutoConfiguration;
import uw.code.center.template.TemplateHelper;
import uw.dao.conf.DaoAutoConfiguration;

/**
 * uw-code-center 自动配置。
 * <p>
 * 在 DAO 与认证自动配置就绪后，通过 {@link CommandLineRunner} 在启动完成时触发
 * {@link TemplateHelper#init()} 加载 FreeMarker 模板缓存。
 * </p>
 *
 * @since 2018-05-17
 */
@Configuration
@AutoConfigureAfter({DaoAutoConfiguration.class, AuthServiceAutoConfiguration.class})
public class CodeCenterAutoConfiguration implements WebMvcConfigurer {

    /**
     * 注册启动后初始化模板缓存的 Runner。
     *
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner configTemplateHelper() {
        return args -> {
            TemplateHelper.init();
        };
    }

}
