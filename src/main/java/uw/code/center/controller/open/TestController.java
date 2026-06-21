package uw.code.center.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.common.util.DateUtils;
import uw.common.util.EnumUtils;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * 测试接口
 */
@RestController
@RequestMapping("/open/test")
@Tag(name = "测试接口")
@Profile({"debug","dev"})
public class TestController {

    /**
     * 回显传入的日期，用于测试日期参数反序列化。
     *
     * @param date 日期参数
     * @return 原样回显的日期
     */
    @GetMapping("/echo")
    public Date echo(@RequestParam(required = false) Date date) {
        return date;
    }

    /**
     * 返回当前服务器时间（ISO 毫秒格式）。
     *
     * @return 当前时间字符串
     */
    @GetMapping("/now")
    public String now() {
        return DateUtils.dateToString(new Date(), DateUtils.DATE_MILLIS_ISO);
    }

    /**
     * 返回 JVM 默认时区显示名。
     *
     * @return 时区显示名
     */
    @GetMapping("/timezone")
    public String timezone() {
        return TimeZone.getDefault().getDisplayName();
    }

    /**
     * 抛出测试异常，用于验证全局异常处理。
     */
    @GetMapping("/exception")
    public void exception() {
        throw new RuntimeException("测试异常");
    }
}
