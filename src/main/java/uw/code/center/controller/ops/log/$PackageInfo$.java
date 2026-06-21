//此处定义菜单项。
package uw.code.center.controller.ops.log;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.UserType;

/**
 * 包级菜单占位控制器。
 * <p>定义该包对应的父级菜单节点（通过 {@code @GetMapping} 暴露的路径），
 * 供 UW 平台权限/导航系统注册菜单，方法体本身无业务逻辑。</p>
 */
@RestController
public class $PackageInfo$ {
    @GetMapping("/ops/log")
    @Operation(summary = "操作日志", description = "操作日志")
    @MscPermDeclare(user = UserType.OPS)
    public void info() {
    }


}