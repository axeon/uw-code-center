//此处定义菜单项。
package uw.code.center.controller.ops.codegen;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.UserType;

/**
 * 主要是提供注解支持用。
 */
@RestController
public class $PackageInfo$ {
    @MscPermDeclare(user = UserType.OPS)
    @Operation(summary = "代码生成", description = "代码生成")
    @GetMapping("/ops/codegen")
    public void info() {

    }

}
