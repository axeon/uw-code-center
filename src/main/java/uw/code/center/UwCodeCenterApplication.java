package uw.code.center;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import uw.common.app.AppBootStrap;

@SpringBootApplication
@EnableDiscoveryClient
public class UwCodeCenterApplication {

    public static void main(String[] args) {
        AppBootStrap.run(UwCodeCenterApplication.class, args);
    }

}
