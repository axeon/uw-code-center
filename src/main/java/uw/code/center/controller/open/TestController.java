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

    @GetMapping("/echo")
    public Date echo(@RequestParam(required = false) Date date) {
        return date;
    }

    @GetMapping("/now")
    public String now() {
        return DateUtils.dateToString(new Date(), DateUtils.DATE_MILLIS_ISO);
    }

    @GetMapping("/timezone")
    public String timezone() {
        return TimeZone.getDefault().getDisplayName();
    }

    @GetMapping("/exception")
    public void exception() {
        throw new RuntimeException("测试异常");
    }
}
