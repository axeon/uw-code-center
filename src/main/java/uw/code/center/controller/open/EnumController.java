package uw.code.center.controller.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uw.common.util.EnumUtils;

import java.util.Map;

/**
 * 枚举类管理
 */
@RestController
@RequestMapping("/open/enum")
@Tag(name = "枚举类管理")
@Profile({"debug","dev"})
public class EnumController {

    private static final String BASE_PACKAGE = "uw.code.center.constant";

    /**
     * 获取 {@code uw.code.center.constant} 包下所有枚举的类型映射。
     *
     * @return 枚举类名 → 枚举值映射
     */
    @GetMapping("/getAllEnumMap")
    @Operation(summary = "获取所有枚举", description = "获取所有枚举")
    public Map<String, Map<String, Enum<?>>> getAllEnumMap() {
        return EnumUtils.getEnumMap(BASE_PACKAGE);
    }


}
