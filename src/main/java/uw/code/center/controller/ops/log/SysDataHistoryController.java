package uw.code.center.controller.ops.log;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uw.app.common.dto.SysDataHistoryQueryParam;
import uw.app.common.entity.SysDataHistory;
import uw.auth.service.AuthServiceHelper;
import uw.auth.service.annotation.MscPermDeclare;
import uw.auth.service.constant.ActionLog;
import uw.auth.service.constant.AuthType;
import uw.auth.service.constant.UserType;
import uw.dao.DaoFactory;
import uw.dao.DataList;
import uw.dao.TransactionException;


/**
 * 数据历史管理。
 */
@RestController
@RequestMapping("/ops/log/dataHistory")
@Tag(name = "数据历史", description = "数据历史")
@MscPermDeclare(type = UserType.OPS)
public class SysDataHistoryController {

    DaoFactory dao = DaoFactory.getInstance();

    /**
     * 列表数据历史。
     *
     * @param queryParam
     * @return
     * @throws TransactionException
     */
    @GetMapping("/list")
    @Operation(summary = "数据历史查询", description = "列表数据历史")
    @MscPermDeclare(type = UserType.OPS, auth = AuthType.PERM, log = ActionLog.REQUEST)
    public DataList<SysDataHistory> list(SysDataHistoryQueryParam queryParam) throws TransactionException {
        AuthServiceHelper.logRef( SysDataHistory.class );
        return dao.list( SysDataHistory.class, queryParam );
    }

}