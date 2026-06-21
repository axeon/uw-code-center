package uw.code.center;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import uw.common.app.AppBootStrap;

/**
 * uw-code-center（代码生成中心）Spring Boot 启动入口。
 * <p>启用 Nacos 服务发现，通过 {@link AppBootStrap} 统一引导启动。</p>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UwCodeCenterApplication {

    /**
     * 应用启动入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        AppBootStrap.run(UwCodeCenterApplication.class, args);
    }

}
