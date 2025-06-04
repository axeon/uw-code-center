package uw.code.center.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.common.util.EnumUtils;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * 公共搜索枚举接口
 */
@RestController
@RequestMapping("/open/enum")
@Tag(name = "枚举类管理")
@Profile({"test", "dev"})
public class EnumController {

    private static final String BASE_PACKAGE = "uw.code.center.constant";

    @GetMapping("/getAllEnumMap")
    @Operation(summary = "获取所有枚举", description = "获取所有枚举")
    public Map<String, Object> getAllEnumMap() {
        return EnumUtils.getEnumMap(BASE_PACKAGE);
    }


    @GetMapping("/echo")
    public Date echo(@RequestParam(required = false) Date date) {
        return date;
    }

    @GetMapping("/now")
    public Date now() {
        return new Date();
    }


    @GetMapping("/timezone")
    public String timezone() {
        return TimeZone.getDefault().toString();
    }

}
