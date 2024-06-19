package uw.code.center.controller.ops.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.app.common.dto.SysCritLogQueryParam;
import uw.app.common.entity.SysCritLog;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;


/**
 * 关键日志管理。
 */
@RestController
@RequestMapping("/ops/log/critLog")
@Tag(name = "关键日志", description = "关键日志")
@MscPermDeclare(type = UserType.OPS)
public class SysCritLogController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表OPS关键日志。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "关键日志查询", description = "列表关键日志")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysCritLog> list(SysCritLogQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logRef( SysCritLog.class );
        return dao.list( SysCritLog.class, queryParam );
    }

}